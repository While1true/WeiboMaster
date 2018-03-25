package com.master.weibomaster.Rx.Net;

import com.master.weibomaster.Rx.MyObserver;
import com.master.weibomaster.Rx.Utils.RxBus;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * ResponseBody for download
 * Created by JokAr on 16/5/11.
 */
public class ProgressDownloadBody extends ResponseBody {

    private ResponseBody responseBody;
    private BufferedSource bufferedSource;
    private MyObserver.Progress progress;

    public ProgressDownloadBody(ResponseBody responseBody,
                                String url) {
        this.responseBody = responseBody;
        progress = new MyObserver.Progress();
        progress.setFile(url);
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return sourceX(source);
    }

    // /** * 写入，回调进度接口 * @param sink Sink * @return Sink */
    private Source sourceX(Source sink) {
        return new ForwardingSource(sink) { //当前写入字节数
            long bytesWritten = 0L; //总字节长度，避免多次调用contentLength()方法
            long contentLength = 0L;

            long lastwritetime = System.currentTimeMillis();
            long lastwrite=0l;
            long speed=0;
            @Override
            public long read(Buffer source, long byteCount) throws IOException {
                long bytesRead = super.read(source, byteCount);
                if (contentLength == 0) { //获得contentLength的值，后续不再调用
                    contentLength = contentLength();
                    progress.setTotal(contentLength);
                } //增加当前写入的字节数
                bytesWritten += byteCount;

                long l = System.currentTimeMillis();
                if(l-lastwritetime>=500){
                    speed=(bytesWritten-lastwrite)*500/(l-lastwritetime);
                    lastwrite=bytesWritten;
                    lastwritetime = l;
                    // 回调
                    RxBus.getDefault().post(progress);
                }
                progress.setSpeed(speed);

                progress.setCurrent(bytesWritten);

                if(bytesWritten==progress.getTotal()){
                    // 回调
                    RxBus.getDefault().post(progress);
                }


                return bytesRead;
            }
        };
    }
}