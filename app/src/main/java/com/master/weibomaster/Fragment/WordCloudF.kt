package com.master.weibomaster.Fragment

import android.Manifest
import android.os.Bundle
import com.bumptech.glide.Glide
import com.master.VangeBugs.Api.ApiImpl
import com.master.weibomaster.Model.Artical
import com.master.weibomaster.R
import com.master.weibomaster.Rx.MyObserver
import com.master.weibomaster.Util.DeviceUtils
import com.master.weibomaster.Util.FileUtils
import coms.pacs.pacs.BaseComponent.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.word_could_fragment.*
import java.io.File
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import com.master.weibomaster.Rx.RxSchedulers
import com.tbruyelle.rxpermissions2.RxPermissions
import coms.pacs.pacs.Utils.mtoString
import coms.pacs.pacs.Utils.toast


/**
 * Created by 不听话的好孩子 on 2018/3/8.
 */
class WordCloudF : BaseFragment() {
    var artical: Artical? = null

    override fun getLayoutId() = R.layout.word_could_fragment


    override fun init(savedInstanceState: Bundle?) {
        wordspreedAnimator.addLifeOwner(this)
        wordspreedAnimator.text=artical?.content.mtoString()
        stateLayout?.setBackgroundResource(R.color.colorf0f0f0)
//        stateLayout?.showLoading()
        RxPermissions(activity).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe {
                    if(!it){
                        "没有读写权限功能可能受限".toast()
                    }
                }

    }

    override fun loadData() {
        ApiImpl.apiImpl.generatePic(artical!!.id, artical!!.content, DeviceUtils.deviceID)
                .map({

                    Glide.with(context)
                            .downloadOnly()
                            .load(it?.data)
                            .submit().get()
                }
                )
                .compose(RxSchedulers.compose())
                .subscribe(object : MyObserver<File>(this) {
                    override fun onNext(t: File) {
                        super.onNext(t)
                        loadPic(t)
                        stateLayout?.showItem()
                        wordspreedAnimator.stop()
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        stateLayout?.ShowError()
                        wordspreedAnimator.stop()
                    }
                })
    }

    private fun loadPic(pic: File) {
        Glide.with(imageview).load(pic).into(imageview)

        content.text = artical?.content + "\n" + artical?.come + "\n" + artical?.timestr

        share.setOnClickListener {
            FileUtils.send(context, pic, "image/jpeg")
        }

        wordshare.setOnClickListener {
            val saveImageToGallery = FileUtils.saveImageToGallery(context, getViewBitmap(imgLayout), artical!!.id.toString() + ".jpg")
            FileUtils.send(context, saveImageToGallery, "image/jpeg")

        }
    }

    fun getViewBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}