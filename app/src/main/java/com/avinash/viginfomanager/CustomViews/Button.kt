package com.avinash.viginfomanager.CustomViews

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.avinash.viginfomanager.R
import com.avinash.viginfomanager.databinding.CustomButtonBinding

class Button(context: Context?, attrs: AttributeSet, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr) {
    private var rootView: CustomButtonBinding =
        CustomButtonBinding.inflate(LayoutInflater.from(context), this, true)

    //Call super(attrs) to send the attributes to the view group or else the attributes will not be applied
    //And also null pointer exception will be thrown for id's

    init {
        LayoutTransition().apply {
            enableTransitionType(LayoutTransition.CHANGING)
            setDuration(250)
            rootView.main.layoutTransition = this
        }
    }

    override fun performClick(): Boolean {
        Log.d("CustomButton", "performClick called")
        super.performClick()
        return true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                rootView.main.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start()
                return true
            }

            MotionEvent.ACTION_UP -> {
                rootView.main.animate().scaleX(1f).scaleY(1f).setDuration(100).withEndAction {
                    performClick()
                }.start()
                return true
            }

            MotionEvent.ACTION_CANCEL -> {
                rootView.main.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    constructor(context: Context?, attrs: AttributeSet) : this(context, attrs, 0) {
        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.Button)
        val title = typedArray?.getString(R.styleable.Button_text)
        setButtonTitle(title ?: "")
        val color = typedArray?.getColor(
            R.styleable.Button_color,
            ContextCompat.getColor(context, R.color.black)
        )
        if (color != null)
            setButtonColor(color)

        val textColor = typedArray?.getColor(
            R.styleable.Button_textColor,
            ContextCompat.getColor(context, R.color.white)
        )

        if (textColor != null)
            setTextColor(textColor)

        val image = typedArray?.getResourceId(R.styleable.Button_image, 0)
        if (image != null && image != 0)
            setImage(image)

        val showImage = typedArray?.getBoolean(R.styleable.Button_showImage, false)
        if (showImage == false)
            showImage(false)

        val imageTint = typedArray?.getColor(
            R.styleable.Button_imageTint,
            ContextCompat.getColor(context, R.color.tpt)
        )

        if (imageTint != null && imageTint!= ContextCompat.getColor(context,R.color.tpt))
            rootView.customBtnIcon.imageTintList = ColorStateList.valueOf(imageTint)

        typedArray?.recycle()
    }

    private fun setButtonTitle(title: String) {
        rootView.customBtnText.text = title
    }

    private fun setButtonColor(color: Int) {
        val drawable = ContextCompat.getDrawable(context, R.drawable.custom_button_bg)
        drawable?.setTint(color)
        rootView.main.background = drawable
    }

    private fun setTextColor(color: Int) {
        rootView.customBtnText.setTextColor(color)
    }

    private fun setImage(image: Int) {
        rootView.customBtnIcon.setImageResource(image)
    }

    private fun showImage(show: Boolean) {
        rootView.customBtnIcon.visibility = if (show) VISIBLE else GONE
    }
}