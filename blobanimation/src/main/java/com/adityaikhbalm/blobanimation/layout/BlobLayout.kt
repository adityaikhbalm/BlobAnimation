package com.adityaikhbalm.blobanimation.layout

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.util.TypedValue.*
import android.view.View
import android.view.animation.*
import android.view.animation.Interpolator
import androidx.core.content.ContextCompat
import com.adityaikhbalm.blobanimation.R
import com.adityaikhbalm.blobanimation.handler.BlobLayoutHandler
import com.adityaikhbalm.blobanimation.model.Blob
import kotlin.math.min
import kotlin.properties.Delegates

class BlobLayout : View {

    companion object {
        const val DEFAULT_POINT_COUNT: Int = 7
        const val DEFAULT_RADIUS: Float = 400f
        const val DEFAULT_OFFSET: Float = 25f
        const val DEFAULT_ANIMATION_STATE: Boolean = true
        const val DEFAULT_ANIMATION_DURATION = 1500
    }

    private val blobLayoutHandler: BlobLayoutHandler = BlobLayoutHandler()
    private lateinit var blobConfig: Blob.Configuration

    private var blobBackground by Delegates.notNull<Int>()
    private var blobCount by Delegates.notNull<Int>()
    private var blobRadius by Delegates.notNull<Float>()
    private var blobOffset by Delegates.notNull<Float>()
    private var blobState by Delegates.notNull<Boolean>()
    private var blobDuration by Delegates.notNull<Long>()
    private lateinit var blobInterpolator: Interpolator
    private var bitmap: Bitmap? = null
    private val paint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { init(attrs) }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init(attrs) }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) { init(attrs) }

    private fun init(attrs: AttributeSet?) {
        val ta: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.BlobLayout)
        blobBackground = ta.getResourceId(R.styleable.BlobLayout_blobLayout, Color.RED)
        blobCount = ta.getInteger(R.styleable.BlobLayout_blobPointCount, DEFAULT_POINT_COUNT)
        blobRadius = ta.getFloat(R.styleable.BlobLayout_blobRadius, DEFAULT_RADIUS)
        blobOffset = ta.getFloat(R.styleable.BlobLayout_blobOffset, DEFAULT_OFFSET)
        blobState = ta.getBoolean(R.styleable.BlobLayout_blobPlay, DEFAULT_ANIMATION_STATE)
        blobDuration = ta.getInteger(
            R.styleable.BlobLayout_blobDuration, DEFAULT_ANIMATION_DURATION
        ).toLong()
        val typeInterpolator: Int = ta.getResourceId(
            R.styleable.BlobLayout_blobInterpolator, android.R.anim.linear_interpolator
        )
        blobInterpolator = AnimationUtils.loadInterpolator(context, typeInterpolator)
        ta.recycle()

        if (background == null) setBackgroundColor(Color.TRANSPARENT)

        setWillNotDraw(false)
        blobLayoutHandler.setOnViewUpdateListener {
            invalidate()
        }
        invalidate()
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec))

        try {
            val isColor = TypedValue()
            resources.getValue(blobBackground, isColor, true)

            if (isColor.type !in TYPE_FIRST_COLOR_INT..TYPE_LAST_COLOR_INT) {
                val bitmapRes: Bitmap = BitmapFactory.decodeResource(resources, blobBackground)
                bitmap = Bitmap.createScaledBitmap(
                    bitmapRes, measuredWidth, measuredHeight, true
                )
            } else {
                paint.color = ContextCompat.getColor(context, blobBackground)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val minVal = min(measuredWidth, measuredHeight)
        if (blobRadius == DEFAULT_RADIUS) blobRadius = minVal / 2f
        if (blobOffset == DEFAULT_OFFSET) blobOffset = (minVal / 4f) / 8f

        blobConfig = Blob.Configuration(
            pointCount = blobCount,
            radius = blobRadius,
            maxOffset = blobOffset,
            shouldAnimateShape = blobState,
            shapeAnimationDuration = blobDuration,
            shapeAnimationInterpolator = blobInterpolator,
            blobCenterPosition = PointF((measuredWidth/2).toFloat(), (measuredHeight/2).toFloat()),
            paint = paint
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        blobLayoutHandler.addBlob(blobConfig)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (bitmap != null) blobLayoutHandler.onDraw(canvas, bitmap)
        else blobLayoutHandler.onDraw(canvas, null)
    }
}