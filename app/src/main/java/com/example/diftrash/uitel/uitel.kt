package com.example.diftrash.uitel

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.diftrash.R
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.NonCancellable.start

fun getProgessDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 5f
        centerRadius = 40f
        start()
    }
}

fun ImageView.loadImage(uri: String?, progressDawable: CircularProgressDrawable) {
    val option = RequestOptions().placeholder(progressDawable)
        .error(R.mipmap.ic_launcher)

    Glide.with(context)
        .setDefaultRequestOptions(option)
        .load(requireNotNull(uri))
        .into(this)
}

@BindingAdapter("app:imageUrl")
fun loadImage(view: ImageView, url: String?) {
    if (!url.isNullOrEmpty()) {
        view.loadImage(url, getProgessDrawable(view.context))
    } else {
        view.setImageResource(R.mipmap.ic_launcher)
    }
}
