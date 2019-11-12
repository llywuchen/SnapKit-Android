package com.lly.snp

import android.view.View
import android.view.View.MeasureSpec
import kotlin.math.abs
import com.lly.snp.ConstraintLayout.LayoutSpec

internal inline fun unwrapIntLambda(
    crossinline lambda: (Int) -> Int
): (Int) -> Int =
    { lambda(it) }

internal inline fun unwrapLayoutIntLambda(
    crossinline lambda: LayoutContainer.() -> Int
): LayoutContainer.() -> Int =
    { lambda() }

internal inline fun unwrapFloatLambda(
    crossinline lambda: LayoutContainer.() -> Float
): LayoutContainer.() -> Int =
    { lambda().toInt() }

interface AxisSolver {

    /**
     * Represents the left or top point of a component in pixels relative to the parent layout's top / left
     * Calling this method may trigger work.
     */
    fun minX(): Int
    fun minY(): Int

    /**
     * Represents the center x or y point of a component in pixels relative to the parent layout's top / left
     * Calling this method may trigger work.
     */
    fun midX(): Int
    fun midY(): Int

    /**
     * Represents the text baseline of a component with text on the y axis relative parent layout's top.
     * Calling this method may trigger work.
     *
     * If component does not have text - or is representing the x axis this will resolve to 0. This will probably change.
     */
    fun baselineX(): Int
    fun baselineY(): Int

    /**
     * Represents the right or bottom point of a component in pixels relative to the parent layout's top / left
     * Calling this method may trigger work.
     */
    fun maxX(): Int
    fun maxY(): Int

    /**
     * Represents the width or height of a component in pixels. This may be solved without any of the axis point being
     * solved or vice-versa
     * Calling this method may trigger work.
     */
    fun rangeX(): Int
    fun rangeY(): Int

    fun onAttach(parent: LayoutSpec)
    fun onRangeResolvedX(range: Int, baselineRange: Int)
    fun onRangeResolvedY(range: Int, baselineRange: Int)

    fun measureSpecX(): Int
    fun measureSpecY(): Int
    fun clear()

}

class SnpAxisSolver() : LayoutConstraint {

    enum class Point {
        Min,
        Mid,
        Baseline,
        Max
    }

    private enum class OffsetType {
        None,P0x,P0y,P1x,P1y
    }

    private lateinit var parent: LayoutSpec

    private val p0x = PositionConstraint()//(point, lambda)
    private val p0y = PositionConstraint()//(point, lambda)
    private val p1x = PositionConstraint()
    private val p1y = PositionConstraint()
    private val sizex = Constraint()
    private val sizey = Constraint()

    private var minx = Int.MIN_VALUE
    private var miny = Int.MIN_VALUE
    private var midx = Int.MIN_VALUE
    private var midy = Int.MIN_VALUE
    private var baselinex = Int.MIN_VALUE
    private var baseliney = Int.MIN_VALUE
    private var maxx = Int.MIN_VALUE
    private var maxy = Int.MIN_VALUE

    private var offType :OffsetType = OffsetType.None

    private var rangex = Int.MIN_VALUE
    private var rangey = Int.MIN_VALUE
    private var baselineRangex = Int.MIN_VALUE
    private var baselineRangey = Int.MIN_VALUE

    override fun minX(): Int {
        if (minx == Int.MIN_VALUE) {
            if (p0x.point == Point.Min) {
                minx = p0x.resolve()
            } else {
                parent.measureSelf()
                resolveAxis()
            }
        }
        return minx
    }

    override fun minY(): Int {
        if (miny == Int.MIN_VALUE) {
            if (p0y.point == Point.Min) {
                miny = p0y.resolve()
            } else {
                parent.measureSelf()
                resolveAxis()
            }
        }
        return miny
    }

    override fun midX(): Int {
        if (midx == Int.MIN_VALUE) {
            if (p0x.point == Point.Mid) {
                midx = p0x.resolve()
            } else {
                parent.measureSelf()
                resolveAxis()
            }
        }
        return midx
    }

    override fun midY(): Int {
        if (midy == Int.MIN_VALUE) {
            if (p0y.point == Point.Mid) {
                midy = p0y.resolve()
            } else {
                parent.measureSelf()
                resolveAxis()
            }
        }
        return midy
    }

    override fun baselineX(): Int {
        if (baselinex == Int.MIN_VALUE) {
            if (p0x.point == Point.Baseline) {
                baselinex = p0x.resolve()
            } else {
                parent.measureSelf()
                resolveAxis()
            }
        }
        return baselinex
    }

    override fun baselineY(): Int {
        if (baseliney == Int.MIN_VALUE) {
            if (p0y.point == Point.Baseline) {
                baseliney = p0y.resolve()
            } else {
                parent.measureSelf()
                resolveAxis()
            }
        }
        return baseliney
    }

    override fun maxX(): Int {
        if (maxx == Int.MIN_VALUE) {
            if (p0x.point == Point.Max) {
                maxx = p0x.resolve()
            } else {
                parent.measureSelf()
                resolveAxis()
            }
        }
        return maxx
    }

    override fun maxY(): Int {
        if (maxy == Int.MIN_VALUE) {
            if (p0y.point == Point.Max) {
                maxy = p0y.resolve()
            } else {
                parent.measureSelf()
                resolveAxis()
            }
        }
        return maxy
    }

    override fun rangeX(): Int {
        if (rangex == Int.MIN_VALUE) {
            parent.measureSelf()
        }
        return rangex
    }

    override fun rangeY(): Int {
        if (rangey == Int.MIN_VALUE) {
            parent.measureSelf()
        }
        return rangey
    }

    private fun resolveAxis() {
        resolveAxisX()
        resolveAxisY()
    }

    private fun resolveAxisX() {
        check(rangex != Int.MIN_VALUE)
        check(baselineRangex != Int.MIN_VALUE)

        val hV = rangex / 2
        when (p0x.point) {
            Point.Min -> {
                minx = p0x.resolve()
                midx = minx + hV
                baselinex = minx + baselineRangex
                maxx = minx + rangex
            }
            Point.Mid -> {
                midx = p0x.resolve()
                minx = midx - hV
                baselinex = minx + baselineRangex
                maxx = midx + hV
            }
            Point.Baseline -> {
                baselinex = p0x.resolve()
                minx = baselinex - baselineRangex
                midx = minx + hV
                maxx = minx + rangex
            }
            Point.Max -> {
                maxx = p0x.resolve()
                midx = maxx - hV
                minx = maxx - rangex
                baselinex = minx + baselineRangex
            }
        }
    }
    private fun resolveAxisY() {
        check(rangey != Int.MIN_VALUE)
        check(baselineRangey != Int.MIN_VALUE)

        val hV = rangey / 2
        when (p0y.point) {
            Point.Min -> {
                miny = p0y.resolve()
                midy = miny + hV
                baseliney = miny + baselineRangey
                maxy = miny + rangey
            }
            Point.Mid -> {
                midy = p0y.resolve()
                miny = midy - hV
                baseliney = miny + baselineRangey
                maxy = midy + hV
            }
            Point.Baseline -> {
                baseliney = p0y.resolve()
                miny = baseliney - baselineRangey
                midy = miny + hV
                maxy = miny + rangey
            }
            Point.Max -> {
                maxy = p0y.resolve()
                midy = maxy - hV
                miny = maxy - rangey
                baseliney = miny + baselineRangey
            }
        }
    }

    override fun onAttach(parent: LayoutSpec) {
        this.parent = parent
        p0x.onAttachContext(parent)
        p0y.onAttachContext(parent)
        p1x.onAttachContext(parent)
        p1y.onAttachContext(parent)
        sizex.onAttachContext(parent)
        sizey.onAttachContext(parent)
    }

    override fun onRangeResolvedX(range: Int, baselineRange: Int) {
        this.rangex = range
        this.baselineRangex = baselineRange
    }

    override fun onRangeResolvedY(range: Int, baselineRange: Int) {
        this.rangey = range
        this.baselineRangey = baselineRange
    }

    override fun measureSpecX(): Int {
        return if (p1x.isSet) {
            View.MeasureSpec.makeMeasureSpec(abs(p0x.resolve() - p1x.resolve()), MeasureSpec.EXACTLY)
        } else if (sizex.isSet) {
            View.MeasureSpec.makeMeasureSpec(sizex.resolve(), MeasureSpec.EXACTLY)
        } else {
            0
        }
    }

    override fun measureSpecY(): Int {
        return if (p1y.isSet) {
            View.MeasureSpec.makeMeasureSpec(abs(p0y.resolve() - p1y.resolve()), MeasureSpec.EXACTLY)
        } else if (sizey.isSet) {
            View.MeasureSpec.makeMeasureSpec(sizey.resolve(), MeasureSpec.EXACTLY)
        } else {
            0
        }
    }

    override fun clear() {
        minx = Int.MIN_VALUE
        midx = Int.MIN_VALUE
        baselinex = Int.MIN_VALUE
        maxx = Int.MIN_VALUE
        rangex = Int.MIN_VALUE
        baselineRangex = Int.MIN_VALUE
        p0x.clear()
        p1x.clear()
        sizex.clear()

        miny = Int.MIN_VALUE
        midy = Int.MIN_VALUE
        baseliney = Int.MIN_VALUE
        maxy = Int.MIN_VALUE
        rangey = Int.MIN_VALUE
        baselineRangey = Int.MIN_VALUE
        p0y.clear()
        p1y.clear()
        sizey.clear()
    }

    override fun leftTo(
        provider: LayoutContainer.() ->Int
    ): LayoutConstraint {
        if (p0x.lambda == null) {
            p0x.point = Point.Min
            p0x.lambda = unwrapLayoutIntLambda(provider)
            offType = OffsetType.P0x
        }else{
            p1x.point = Point.Min
            p1x.lambda = unwrapLayoutIntLambda(provider)
            offType = OffsetType.P1x
        }
        return this
    }

    override fun leftTo(
        provider: Int
    ): LayoutConstraint {
        return leftTo {provider}
    }

    override fun centerXTo(provider: LayoutContainer.() -> Int): LayoutConstraint {
        if (p0x.lambda == null) {
            p0x.point = Point.Mid
            p0x.lambda = unwrapLayoutIntLambda(provider)
            offType = OffsetType.P0x
        }else{
            p1x.point = Point.Mid
            p1x.lambda = unwrapLayoutIntLambda(provider)
            offType = OffsetType.P1x
        }
        return this
    }

    override fun centerXTo(provider: Int): LayoutConstraint {
        return  centerXTo { provider }
    }

    override fun topTo(
        provider: LayoutContainer.() ->Int
    ): LayoutConstraint {
        if (p0y.lambda == null) {
            p0y.point = Point.Min
            p0y.lambda = unwrapLayoutIntLambda(provider)
            offType = OffsetType.P0y
        }else{
            p1y.point = Point.Min
            p1y.lambda = unwrapLayoutIntLambda(provider)
            offType = OffsetType.P1y
        }
        return this
    }

    override fun topTo(
        provider: Int
    ): LayoutConstraint {
        return  topTo {provider}
    }

    override fun centerYTo(provider: LayoutContainer.() -> Int): LayoutConstraint {
        if (p0y.lambda == null) {
            p0y.point = Point.Mid
            p0y.lambda = unwrapLayoutIntLambda(provider)
            offType = OffsetType.P0y
        }else{
            p1y.point = Point.Mid
            p1y.lambda = unwrapLayoutIntLambda(provider)
            offType = OffsetType.P1y
        }
        return this
    }

    override fun centerYTo(provider: Int): LayoutConstraint {
        return centerYTo { provider }
    }

    override fun rightTo(
        provider: LayoutContainer.() ->Int
    ): LayoutConstraint {
        if (p0x.lambda == null) {
            p0x.point = Point.Max
            p0x.lambda = unwrapLayoutIntLambda(provider)
            offType = OffsetType.P0x
        }else {
            p1x.point = Point.Max
            p1x.lambda = unwrapLayoutIntLambda(provider)
            offType = OffsetType.P1x
        }
        return this
    }

    override fun rightTo(
        provider: Int
    ): LayoutConstraint {
        return rightTo {provider}
    }

    override fun bottomTo(
        provider: LayoutContainer.() ->Int
    ): LayoutConstraint {
        if (p0y.lambda == null) {
            p0y.point = Point.Max
            p0y.lambda = unwrapLayoutIntLambda(provider)
            offType = OffsetType.P0y
        }else{
            p1y.point = Point.Max
            p1y.lambda = unwrapLayoutIntLambda(provider)
            offType = OffsetType.P1y
        }
        return this
    }

    override fun bottomTo(
        provider: Int
    ): LayoutConstraint {
        return bottomTo {provider}
    }

    override fun widthTo(
        provider: LayoutContainer.() ->Int
    ): LayoutConstraint {
        sizex.lambda = unwrapLayoutIntLambda(provider)
        return this
    }

    override fun widthTo(provider: Int): LayoutConstraint {
        return widthTo {provider}
    }


    override fun heightTo(
        provider: LayoutContainer.() ->Int
    ): LayoutConstraint {
        sizey.lambda = unwrapLayoutIntLambda(provider)
        return this
    }

    override fun heightTo(
        provider: Int
    ): LayoutConstraint {
        return heightTo {provider}
    }

    override fun offset(offset: Int) {
        if(offType == OffsetType.P0x){
            p0x.offset = offset
        }else if(offType == OffsetType.P0y){
            p0y.offset = offset
        }else if(offType == OffsetType.P1x){
            p1x.offset = offset
        }else if(offType == OffsetType.P1y){
            p1y.offset = offset
        }
    }

}