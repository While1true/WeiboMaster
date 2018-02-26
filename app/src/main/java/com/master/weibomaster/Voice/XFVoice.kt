package com.master.weibomaster.Voice

import android.os.Environment
import com.iflytek.cloud.*
import com.master.weibomaster.App
import coms.pacs.pacs.Utils.log

object XFVoice {

    val mTts by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { SpeechSynthesizer.createSynthesizer(App.app, {log(it.toString())}) }

    fun setDefaultParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null)
        // 根据合成引擎设置相应参数
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD)
        // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan")
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3")
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true")
        //设置合成语速
        mTts.setParameter(SpeechConstant.SPEED,"60")
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, "50")
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, "50")
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav")
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory().toString() + "/msc/tts.wav")
    }

    fun readerString(voiceStr: String, listener: SynthesizerListener) {
        mTts.startSpeaking(voiceStr, listener)
    }
}