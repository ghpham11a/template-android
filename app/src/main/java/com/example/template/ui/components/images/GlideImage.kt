package com.example.template.ui.components.images

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

@Composable
fun GlideImage(
    model: Any,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageRequest = remember {
        Glide.with(context)
            .asBitmap()
            .load(model)
    }

    val imageState = remember { mutableStateOf<Bitmap?>(null) }

    DisposableEffect(model) {
        val target = object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                imageState.value = resource
            }
            override fun onLoadCleared(placeholder: Drawable?) {
                imageState.value = null
            }
        }
        imageRequest.into(target)
        onDispose { Glide.with(context).clear(target) }
    }

    imageState.value?.let { bitmap ->
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = contentDescription,
            modifier = modifier
        )
    }
}