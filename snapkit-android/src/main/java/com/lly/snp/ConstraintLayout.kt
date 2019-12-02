package com.lly.snp

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import kotlin.math.min

private const val WRAP = ViewGroup.LayoutParams.WRAP_CONTENT

open class ConstraintLayout(
    context: Context,
    attrs: AttributeSet? = null
) : ViewGroup(context, attrs) {

    val density: Float = context.resources.displayMetrics.density
    private val widthConfig = SizeConfig()
    private val heightConfig = SizeConfig()
    val geometry = ParentGeometry(widthConfig, heightConfig)
    private var constructed: Boolean = true
    private var initialized: Boolean = false
    private var lastWidthSpec: Int = 0
    private var lastHeightSpec: Int = 0

    private fun initializeLayout() {
        if (!initialized) {
            configSubViews()
            configCs()
            initialized = true
        }
    }

    open fun configSubViews(){}

    open fun configCs(){}

    override fun requestLayout() {
        if (constructed) {
            invalidateAll()
        }
        super.requestLayout()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        initializeLayout()
    }

    override fun onMeasure(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ) {
        initializeLayout()
        if (lastWidthSpec != widthMeasureSpec || lastHeightSpec != heightMeasureSpec) {
            invalidateAll()
        }

        widthConfig.available = MeasureSpec.getSize(widthMeasureSpec)
        heightConfig.available = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(widthConfig.resolve(), heightConfig.resolve())

        lastWidthSpec = widthMeasureSpec
        lastHeightSpec = heightMeasureSpec
    }

    override fun onLayout(
        changed: Boolean,
        l: Int,
        t: Int,
        r: Int,
        b: Int
    ) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val params = child.layoutParams as LayoutSpec
            child.measure(params.ax.measureSpecX(), params.ax.measureSpecY())
            child.layout(
                params.left(), params.top(),
                params.right(), params.bottom()
            )
        }
    }

    private fun invalidateAll() {
        widthConfig.clear()
        heightConfig.clear()
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            (child.layoutParams as? LayoutSpec)?.clear()
        }
    }

    private inline fun View.spec(): LayoutSpec {
        if (parent !== this@ConstraintLayout) {
            throw IllegalArgumentException("Referencing view outside of ViewGroup.")
        }
        return layoutParams as LayoutSpec
    }

    private inline fun <T> View.handleCrd(block: () -> T): T {
        return try {
            block()
        } catch (e: CircularReferenceException) {
            // Want first stacktrace element to be following line.
            // Thread.currentThread().stackTrace does not do this.
            val trace = Throwable().stackTrace
            val current = trace.getOrNull(0)
            val calledBy = trace.getOrNull(1)
            e.add(CircularReferenceException.TraceElement(this, current, calledBy))
            throw e
        }
    }

    fun contourWidthOf(config: (available: Int) -> Int) {
        widthConfig.lambda = unwrapIntLambda(config)
    }

    fun contourHeightOf(config: (available: Int) -> Int) {
        heightConfig.lambda = unwrapIntLambda(config)
    }

    fun View.applyLayout(
        ax: AxisSolver,
        addToViewGroup: Boolean = false
    ) {
        val viewGroup = this@ConstraintLayout
        val spec = LayoutSpec(ax)
        spec.dimen = ViewDimensions(this)
        spec.view = viewGroup.geometry
        layoutParams = spec
        if (addToViewGroup && parent == null) {
            viewGroup.addView(this)
        }
    }

    fun Snp.makeConstraints(
        ax : (ax:LayoutConstraint)->Unit
    ) {
        val axisSolver = SnpAxisSolver()
        ax(axisSolver)

        val spec = ConstraintLayout.LayoutSpec(axisSolver)
        spec.dimen = ViewDimensions(this.sview)
        spec.view = this@ConstraintLayout.geometry
        this.sview.layoutParams = spec
    }

    fun makeSubConstraints(snp:Snp,
        ax : (ax:LayoutConstraint)->Unit
    ) {
        val axisSolver = SnpAxisSolver()
        ax(axisSolver)

        val spec = ConstraintLayout.LayoutSpec(axisSolver)
        spec.dimen = ViewDimensions(snp.sview)
        spec.view = this@ConstraintLayout.geometry
        snp.sview.layoutParams = spec
    }



    fun Snp.remakeConstraints(
        ax : (ax:LayoutConstraint)->Unit
    ) {
        val axisSolver = SnpAxisSolver()
        ax(axisSolver)

        val spec = ConstraintLayout.LayoutSpec(axisSolver)
        spec.dimen = ViewDimensions(this.sview)
        spec.view = this@ConstraintLayout.geometry
        this.sview.layoutParams = spec
    }

    fun Snp.updateConstraints(
        ax : (ax:LayoutConstraint)->Unit
    ) {
        val axisSolver = SnpAxisSolver()
        ax(axisSolver)
        val spec = ConstraintLayout.LayoutSpec(axisSolver)
        spec.dimen = ViewDimensions(this.sview)
        spec.view = this@ConstraintLayout.geometry
        this.sview.layoutParams = spec
    }

    fun addSubview(child:View){
        this.addView(child)
    }

    fun View.updateLayout(
        ax: AxisSolver = spec().ax
    ) {
        val viewGroup = this@ConstraintLayout
        val spec = LayoutSpec(ax)
        spec.dimen = ViewDimensions(this)
        spec.view = viewGroup.geometry
        layoutParams = spec
    }

    /**
     * The left position of the receiver [View]. Guaranteed to return the resolved value or throw.
     * @return the laid-out left position of the [View]
     */
    fun View.left(): Int= handleCrd { spec().left() }

    /**
     * The top position of the receiver [View]. Guaranteed to return the resolved value or throw.
     * @return the laid-out top position of the [View]
     */
    fun View.top(): Int= handleCrd { spec().top() }

    /**
     * The right position of the receiver [View]. Guaranteed to return the resolved value or throw.
     * @return the laid-out right position of the [View]
     */
    fun View.right(): Int= handleCrd { spec().right() }

    /**
     * The bottom position of the receiver [View]. Guaranteed to return the resolved value or throw.
     * @return the laid-out bottom position of the [View]
     */
    fun View.bottom(): Int= handleCrd { spec().bottom() }

    /**
     * The center-x position of the receiver [View]. Guaranteed to return the resolved value or throw.
     * @return the laid-out left center-x of the [View]
     */
    fun View.centerX(): Int= handleCrd { spec().centerX() }

    /**
     * The center-y position of the receiver [View]. Guaranteed to return the resolved value or throw.
     * @return the laid-out center-y position of the [View]
     */
    fun View.centerY(): Int= handleCrd { spec().centerY() }

    /**
     * The baseline position of the receiver [View]. Guaranteed to return the resolved value or throw.
     * @return the laid-out baseline position of the [View]
     *
     * The baseline position will be 0 if the receiver [View] does not have a baseline. The most notable use of baseline
     * is in [TextView] which provides the baseline of the text.
     */
    fun View.baselineX(): Int= handleCrd { spec().baselineX() }
    fun View.baselineY(): Int= handleCrd { spec().baselineY() }

    /**
     * The width of the receiver [View]. Guaranteed to return the resolved value or throw.
     * @return the laid-out width of the [View]
     */
    fun View.width(): Int= handleCrd { spec().width() }

    /**
     * The height of the receiver [View]. Guaranteed to return the resolved value or throw.
     * @return the laid-out height of the [View]
     */
    fun View.height(): Int= handleCrd { spec().height() }

    /**
     * The preferred width of the receiver [View] when no constraints are applied to the view.
     * @return the preferred width of the [View]
     */
    fun View.preferredWidth(): Int= handleCrd { spec().preferredWidth() }

    /**
     * The preferred height of the receiver [View] when no constraints are applied to the view.
     * @return the preferred height of the [View]
     */
    fun View.preferredHeight(): Int= handleCrd { spec().preferredHeight() }

    fun minOf(
        a: Int,
        b: Int
    ): Int= min(a, b).toInt()


    class LayoutSpec(
        internal val ax: AxisSolver
    ) : ViewGroup.LayoutParams(WRAP, WRAP), LayoutContainer {

        override lateinit var view: Geometry
        internal lateinit var dimen: HasDimensions

        init {
            ax.onAttach(this)
        }

        internal fun left(): Int= ax.minX()
        internal fun right(): Int= ax.maxX()
        internal fun centerX(): Int= ax.midX()
        internal fun top(): Int= ax.minY()
        internal fun bottom(): Int= ax.maxY()
        internal fun centerY(): Int= ax.midY()
        internal fun baselineX(): Int= ax.baselineX()
        internal fun baselineY(): Int= ax.baselineY()
        internal fun width(): Int= ax.rangeX()
        internal fun height(): Int= ax.rangeY()

        internal fun preferredWidth(): Int{
            dimen.measure(0, ax.measureSpecY())
            return dimen.width
        }

        internal fun preferredHeight(): Int{
            dimen.measure(ax.measureSpecX(), 0)
            return dimen.height
        }

        internal fun measureSelf() {
            dimen.measure(ax.measureSpecX(), ax.measureSpecY())
            ax.onRangeResolvedX(dimen.width, 0)
            ax.onRangeResolvedY(dimen.height, dimen.baseline)
        }

        internal fun clear() {
            ax.clear()
        }
    }
}