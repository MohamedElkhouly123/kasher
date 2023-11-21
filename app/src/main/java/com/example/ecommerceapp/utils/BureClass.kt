package com.example.ecommerceapp.utils

import android.annotation.TargetApi
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.view.ViewTreeObserver
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel


object BureClass {
    private fun applyBlur(generalDataSendedModel: GeneralDataSendedModel) {
        generalDataSendedModel.card?.getViewTreeObserver()
            ?.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    generalDataSendedModel.card?.getViewTreeObserver()?.removeOnPreDrawListener(this)
                    generalDataSendedModel.card?.buildDrawingCache()
                    val bmp: Bitmap = generalDataSendedModel.card?.getDrawingCache()!!
                    blur(bmp, generalDataSendedModel)
                    return true
                }
            })
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun blur(bkg: Bitmap, generalDataSendedModel: GeneralDataSendedModel) {
        val startMs = System.currentTimeMillis()
        val radius = 20f
        val overlay = Bitmap.createBitmap(
            generalDataSendedModel.card?.getMeasuredWidth() as Int,
            generalDataSendedModel.card?.getMeasuredHeight() as Int, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(overlay)
        canvas.translate((-generalDataSendedModel.card?.getLeft()!!).toFloat(), (-generalDataSendedModel.card?.getTop()!!).toFloat())
//        canvas.drawBitmap(bkg, 0, 0, null)
        val rs = RenderScript.create(generalDataSendedModel.activity)
        val overlayAlloc = Allocation.createFromBitmap(
            rs, overlay
        )
        val blur = ScriptIntrinsicBlur.create(
            rs, overlayAlloc.element
        )
        blur.setInput(overlayAlloc)
        blur.setRadius(radius)
        blur.forEach(overlayAlloc)
        overlayAlloc.copyTo(overlay)
        generalDataSendedModel.card?.setBackground(
            BitmapDrawable(
                generalDataSendedModel.activity?.getResources(), overlay
            )
        )
        rs.destroy()
//        generalDataSendedModel.card?.setText(System.currentTimeMillis() - startMs.toString() + "ms")
    }
}