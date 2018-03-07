package com.master.weibomaster.Util

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.provider.Settings.Secure
import com.master.weibomaster.App
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


/**
 * Created by 不听话的好孩子 on 2018/3/7.
 */
object DeviceUtils {
    val deviceID:String by lazy { getUniqueId(App.app) }

    fun getUniqueId(context: Context): String {
        val androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)
        val id = androidID + Build.SERIAL
        try {
            return toMD5(id)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return id
        }

    }


    @Throws(NoSuchAlgorithmException::class)
    private fun toMD5(text: String): String {
        //获取摘要器 MessageDigest
        val messageDigest = MessageDigest.getInstance("MD5")
        //通过摘要器对字符串的二进制字节数组进行hash计算
        val digest = messageDigest.digest(text.toByteArray())

        val sb = StringBuilder()
        for (i in digest.indices) {
            //循环每个字符 将计算结果转化为正整数;
            val digestInt = digest[i].toInt() and 0xff
            //将10进制转化为较短的16进制
            val hexString = Integer.toHexString(digestInt)
            //转化结果如果是个位数会省略0,因此判断并补0
            if (hexString.length < 2) {
                sb.append(0)
            }
            //将循环结果添加到缓冲区
            sb.append(hexString)
        }
        //返回整个结果
        return sb.toString()
    }
}