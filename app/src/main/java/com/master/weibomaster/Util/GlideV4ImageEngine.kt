package com.master.weibomaster.Util

import com.bumptech.glide.Glide
import android.content.Context
import com.bumptech.glide.request.RequestOptions
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Priority
import com.zhihu.matisse.engine.ImageEngine



/**
 * Created by 不听话的好孩子 on 2018/3/9.
 */

class GlideV4ImageEngine : ImageEngine {

    override fun loadThumbnail(context: Context, resize: Int, placeholder: Drawable, imageView: ImageView, uri: Uri) {
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.placeholder(placeholder)
        requestOptions = requestOptions.override(resize, resize)
        requestOptions = requestOptions.centerCrop()
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .apply(requestOptions)
                .into(imageView)
    }

    override fun loadAnimatedGifThumbnail(context: Context, resize: Int, placeholder: Drawable, imageView: ImageView, uri: Uri) {
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.placeholder(placeholder)
        requestOptions = requestOptions.override(resize, resize)
        requestOptions = requestOptions.centerCrop()
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .apply(requestOptions)
                .into(imageView)
    }

    override fun loadImage(context: Context, resizeX: Int, resizeY: Int, imageView: ImageView, uri: Uri) {
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.priority(Priority.HIGH)
        requestOptions = requestOptions.override(resizeX, resizeY)
        requestOptions = requestOptions.centerCrop()
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .apply(requestOptions)
                .into(imageView)
    }

    override fun loadAnimatedGifImage(context: Context, resizeX: Int, resizeY: Int, imageView: ImageView, uri: Uri) {
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.priority(Priority.HIGH)
        requestOptions = requestOptions.override(resizeX, resizeY)
        requestOptions = requestOptions.centerCrop()
        Glide.with(context)
                .asGif()
                .load(uri)
                .apply(requestOptions)
                .into(imageView)
    }

    override fun supportAnimatedGif(): Boolean {
        return true
    }
}