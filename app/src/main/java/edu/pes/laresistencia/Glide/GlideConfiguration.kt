package edu.pes.laresistencia.Glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.module.GlideModule


class GlideConfiguration : GlideModule {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        // Apply options to the builder here.
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888)
    }

    override fun registerComponents(context: Context, glide: Glide) {
        // register ModelLoaders here.
    }
}