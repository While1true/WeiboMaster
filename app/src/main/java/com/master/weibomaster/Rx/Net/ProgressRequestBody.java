package com.master.weibomaster.Rx.Net;

import android.graphics.Point;

import com.master.weibomaster.Rx.MyObserver;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by 不听话的好孩子 on 2018/3/12.
 */

public final class ProgressRequestBody extends RequestBody {
    /**
     * 实际请求体
     */
    private RequestBody requestBody;
    /**
     * 上传回调接口
     */
    private MyObserver callback;
    /**
     * 包装完成的BufferedSink
     */
    private BufferedSink bufferedSink;

    private MyObserver.Progress progress;

    public ProgressRequestBody(RequestBody requestBody, MyObserver callback) {
        super();
        this.requestBody = requestBody;
        this.callback = callback;
        progress=new MyObserver.Progress();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        bufferedSink = Okio.buffer(sink(sink)); //写入
        requestBody.writeTo(bufferedSink); //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();
    }

    // /** * 写入，回调进度接口 * @param sink Sink * @return Sink */
    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) { //当前写入字节数
            long bytesWritten = 0L; //总字节长度，避免多次调用contentLength()方法
            long contentLength = 0L;
            long lastwritetime = System.currentTimeMillis();
            long lastwrite=0l;
            long speed=0;
            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) { //获得contentLength的值，后续不再调用
                    contentLength = contentLength();
                    progress.setTotal(contentLength);
                } //增加当前写入的字节数
                long l = System.currentTimeMillis();
                if(l-lastwritetime>=500){
                    speed=(bytesWritten-lastwrite)*500/(l-lastwritetime);
                    lastwrite=bytesWritten;
                    lastwritetime = l;
                    // 回调
                    callback.onProgress(progress);
                }
                progress.setSpeed(speed);


                progress.setCurrent(bytesWritten);

                if(bytesWritten== progress.getTotal()){
                    callback.onProgress(progress);
                }
            }
        };
    }
}
