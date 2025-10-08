package com.example.flagschallenge.utility

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

object ImageUtility {

    fun loadImagineGlide(
        imageUrl: Int?,
        placeHolder: Int? = null,
        imageView: ImageView? = null,
        scaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER_CROP
    ) {
        if (imageView == null) {
            return
        }

        Glide.with(imageView.context.applicationContext)
            .setDefaultRequestOptions(
                RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(placeHolder!!)
                    .error(placeHolder)
                    .centerCrop()
            )
            .load(imageUrl)
            .also {
                it.into(imageView)
            }
//                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.scaleType = scaleType
        imageView.requestLayout()

    }

}