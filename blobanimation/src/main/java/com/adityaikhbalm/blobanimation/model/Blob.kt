package com.adityaikhbalm.blobanimation.model

import android.animation.ValueAnimator
import android.animation.ValueAnimator.INFINITE
import android.animation.ValueAnimator.REVERSE
import android.graphics.*
import android.view.animation.Interpolator
import androidx.core.animation.doOnRepeat
import com.adityaikhbalm.blobanimation.util.PointUtil
import com.adityaikhbalm.blobanimation.util.RandomUtil

class Blob(
    private val blobConfig: Configuration,
    private val onViewUpdate: () -> Unit
) {

    companion object {
        const val RADIAN_MULTIPLIER: Double = 2f * Math.PI
    }

    private val startPoints: ArrayList<PointF> = arrayListOf()
    private val endPoints: ArrayList<PointF> = arrayListOf()

    private var _latestPoints: ArrayList<PointF> = arrayListOf()

    private val percentageAnimator: ValueAnimator = ValueAnimator().apply {
        setFloatValues(0f, 100f)
        interpolator = blobConfig.shapeAnimationInterpolator
        duration = blobConfig.shapeAnimationDuration
        repeatCount = INFINITE
        repeatMode = REVERSE
    }

    private val latestPath: Path = Path()

    init {
        updateRandomizedValues()
        recreatePath()
        percentageAnimator.addUpdateListener {
            recreatePath()
            onViewUpdate.invoke()
        }
        percentageAnimator.doOnRepeat {
            if (!blobConfig.shouldAnimateShape) {
                return@doOnRepeat
            }
            if (percentageAnimator.animatedValue as Float > 50f) {
                updateRandomizedValues(startPoints)
            } else {
                updateRandomizedValues(endPoints)
            }
        }
        percentageAnimator.start()
    }

    private fun updateRandomizedValues() {
        updateRandomizedValues(startPoints)
        updateRandomizedValues(endPoints)
    }

    private fun updateRandomizedValues(points: ArrayList<PointF>) {
        points.clear()
        val baseTheta: Double = RADIAN_MULTIPLIER / blobConfig.pointCount
        var previousAngle = 0f
        var currentAngle: Float
        for (i in 0 until blobConfig.pointCount) {
            val offsetR = blobConfig.radius +
                    ((RandomUtil.getMultiplier() * blobConfig.maxOffset)
                        .coerceIn(-blobConfig.radius, blobConfig.radius))
            currentAngle = ((i * baseTheta) + (RandomUtil.getFloat() * baseTheta)).toFloat()
            currentAngle = if (currentAngle - previousAngle > baseTheta / 3) {
                currentAngle
            } else {
                currentAngle + (baseTheta.toFloat() / 3f)
            }
            points.add(
                PointUtil.getPointOnCircle(
                    offsetR,
                    currentAngle,
                    blobConfig.blobCenterPosition
                )
            )
            previousAngle = currentAngle
        }
    }

    private fun recreatePath(path: Path = latestPath, points: ArrayList<PointF> = getPoints()) {
        path.rewind()
        path.apply {
            var p0: PointF = points[points.size - 1]
            var p1: PointF = points[0]
            moveTo((p0.x + p1.x) / 2, (p0.y + p1.y) / 2)
            p0 = p1
            for (i in 1 until points.count()) {
                p1 = points[i]
                quadTo(p0.x, p0.y, (p0.x + p1.x) / 2f, (p0.y + p1.y) / 2f)
                p0 = p1
            }
            quadTo(p0.x, p0.y, (p0.x + points[0].x) / 2f, (p0.y + points[0].y) / 2f)
            close()
        }
    }

    private fun getPoints(): ArrayList<PointF> {
        if (startPoints.size != blobConfig.pointCount || endPoints.size != blobConfig.pointCount) {
            return _latestPoints
        }
        _latestPoints = arrayListOf<PointF>().apply {
            val animationMultiplier: Float =
                ((if (blobConfig.shouldAnimateShape) percentageAnimator.animatedValue
                else 100f) as Float) / 100f
            for (i in 0 until blobConfig.pointCount) {
                add(
                    PointUtil.getIntermediatePoint(
                        startPoints[i], endPoints[i], animationMultiplier
                    )
                )
            }
        }
        return _latestPoints
    }

    fun drawPath(canvas: Canvas?, bitmap: Bitmap?) {
        bitmap?.let {
            val mShader = BitmapShader(it, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            blobConfig.paint.shader = mShader
        }
        canvas?.drawPath(latestPath, blobConfig.paint)
    }

    data class Configuration(
        var pointCount: Int,
        var radius: Float,
        var maxOffset: Float,
        var shouldAnimateShape: Boolean,
        var shapeAnimationDuration: Long,
        var shapeAnimationInterpolator: Interpolator,
        var blobCenterPosition: PointF,
        var paint: Paint
    )
}