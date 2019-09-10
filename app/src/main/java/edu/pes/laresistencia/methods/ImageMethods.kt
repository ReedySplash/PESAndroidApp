package edu.pes.laresistencia.methods

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.support.v4.graphics.drawable.RoundedBitmapDrawable
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.util.DisplayMetrics


class ImageMethods {

    companion object {

        fun resize(image: Bitmap, minWidth: Int, minHeight: Int): Bitmap {
            if (minHeight > 0 && minWidth > 0) {
                val ratioBitmap = image.width.toFloat() / image.height.toFloat()
                var finalWidth = minWidth
                var finalHeight = minHeight
                if (image.height > image.width)
                    finalHeight = (minHeight.toFloat() / ratioBitmap).toInt()
                else finalWidth = (minWidth.toFloat() / ratioBitmap).toInt()
                return Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true)
            } else return image
        }

        fun resizeDPI(image: Bitmap, context: Activity): Bitmap {
            val metrics: DisplayMetrics = DisplayMetrics()
            context.windowManager.defaultDisplay.getMetrics(metrics)
            val width: Int = image.width
            val height: Int = image.height

            val scaleWidth: Float = metrics.scaledDensity
            val scaleHeight: Float = metrics.scaledDensity
            val matrix: Matrix = Matrix()
            matrix.postScale(scaleWidth, scaleHeight)
            return Bitmap.createBitmap(image, 0, 0, width, height, matrix, true)
        }

        fun makeImageDraw(originalPicture: Bitmap, context: Context): RoundedBitmapDrawable {
            //Variable that will be necessary
            var paint = Paint()
            val srcBitmapWidth: Int = originalPicture.width
            val srcBitmapHeight: Int = originalPicture.height
            val borderWidth: Int = 12
            //val shadowWidth: Int = 10
            val dstBitmapWidth: Int = Math.min(srcBitmapHeight, srcBitmapWidth) + borderWidth * 2
            //Bitmap to draw original picture, border and shadow
            var dstBitmap: Bitmap = Bitmap.createBitmap(dstBitmapWidth, dstBitmapWidth, Bitmap.Config.ARGB_8888)

            var canvas = Canvas(dstBitmap)
            canvas.drawColor(Color.TRANSPARENT) //Solid color

            //Draw the original picture and saves empty space for border and shadow
            canvas.drawBitmap(originalPicture,
                    (dstBitmapWidth - srcBitmapWidth) / 2.0f,
                    (dstBitmapWidth - srcBitmapHeight) / 2.0f,
                    null)

            //Paint the border
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = borderWidth.toFloat() * 2.0f
            paint.color = Color.BLACK

            //Draw the border
            canvas.drawCircle(canvas.width / 2.0f, canvas.height / 2.0f, canvas.width / 2.0f, paint)

            //Paint the shadow
            //paint.color = Color.LTGRAY
            //paint.strokeWidth = shadowWidth.toFloat()

            //Draw the shadow
            //canvas.drawCircle(canvas.width / 2.0f, canvas.height / 2.0f, canvas.width / 2.0f, paint)

            //Create a circle image and add it to the imageview
            var roundedBitmapDrawable: RoundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.resources, dstBitmap)
            roundedBitmapDrawable.isCircular = true
            roundedBitmapDrawable.setAntiAlias(true)
            return roundedBitmapDrawable
        }

        fun getRoundedBitmap(context: Context, dstBitmap: Bitmap): RoundedBitmapDrawable
        {
            var roundedBitmapDrawable: RoundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.resources, dstBitmap)
            roundedBitmapDrawable.isCircular = true
            roundedBitmapDrawable.setAntiAlias(true)
            return roundedBitmapDrawable
        }
    }
}