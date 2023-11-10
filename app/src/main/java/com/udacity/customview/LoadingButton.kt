package com.udacity.customview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.withStyledAttributes
import com.udacity.R
import kotlin.properties.Delegates

enum class ButtonState(val label: Int) {
    NORMAL(R.string.button_text_click),
    LOADING(R.string.button_text_loading),
    COMPLETED(R.string.button_text_downloaded);
}

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var valueAnimator = ValueAnimator()

    // Button Custom attribute
    private var buttonNormal = 0
    private var buttonLoading = 0
    private var buttonCompleted = 0

    // Progress
    private var progress = 0.0

    private val paint = Paint().apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.NORMAL) { p, old, new ->

        when (new) {
            ButtonState.COMPLETED-> {
                Log.d("LoadingButton", " ButtonState.Completed,Default")
                valueAnimator.cancel()
                invalidate()
            }


            ButtonState.LOADING -> {
                Log.d("LoadingButtonnn", "ButtonState.Loading")
                animation()
            }

            else -> {
                invalidate()
            }
        }

    }

    init {
        isClickable = true
        buttonState = ButtonState.NORMAL

        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            buttonNormal = getColor(R.styleable.LoadingButton_button_normal, 0)
            buttonLoading = getColor(R.styleable.LoadingButton_button_loading, 0)
            buttonCompleted = getColor(R.styleable.LoadingButton_button_completed, 0)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawButton(canvas)
        drawText(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }



    private fun animation() {
        valueAnimator = ValueAnimator.ofFloat(0f, measuredWidth.toFloat())

        val updateListener = ValueAnimator.AnimatorUpdateListener { animated ->
            progress = (animated.animatedValue as Float).toDouble()
            invalidate()
        }

        valueAnimator.duration = 2000
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.addUpdateListener(updateListener)
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                buttonState = ButtonState.NORMAL
            }
        })
        valueAnimator.start()
    }

    private fun drawButton(canvas: Canvas?) {
        paint.color = buttonNormal
        canvas?.drawRect(
            0f, 0f,
            width.toFloat(), height.toFloat(), paint
        )

        if (buttonState == ButtonState.LOADING) {
            paint.color = buttonLoading
            canvas?.drawRect(
                0f, 0f,
                (width * (progress / 100)).toFloat(), height.toFloat(), paint
            )

            val rect = RectF(0f, 0f, 80f, 80f)
            canvas?.save()
            canvas?.translate((width / 2 + 220).toFloat(), 40f)
            paint.color = buttonCompleted
            canvas?.drawArc(rect, 0f, (360 * (progress / 100)).toFloat(), true, paint)
            canvas?.restore()
        }
    }

    private fun drawText(canvas: Canvas?) {
        paint.color = Color.WHITE
        val label = when (buttonState) {
            ButtonState.NORMAL -> context.getString(R.string.button_text_click)
            ButtonState.LOADING -> context.getString(R.string.button_text_loading)
            ButtonState.COMPLETED -> context.getString(R.string.button_text_click)
        }

        canvas?.drawText(label, (width / 2).toFloat(), ((height + 30) / 2).toFloat(), paint)
    }
}