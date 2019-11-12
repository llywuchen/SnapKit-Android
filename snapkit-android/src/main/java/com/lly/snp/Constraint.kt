package com.lly.snp

import com.lly.snp.SnpAxisSolver.Point

internal open class Constraint {
    private var isResolving: Boolean = false
    private var container: LayoutContainer? = null
    private var value: Int = Int.MIN_VALUE
    var lambda: (LayoutContainer.() -> Int)? = null

    val isSet: Boolean get() = lambda != null

    fun onAttachContext(container: LayoutContainer) {
        this.container = container
    }

    open fun resolve(): Int {
        if (value == Int.MIN_VALUE) {
            val context =
                checkNotNull(container) { "Constraint called before LayoutContainer attached" }
            val lambda = checkNotNull(lambda) { "Constraint not set" }

            try {
                if (isResolving) throw CircularReferenceException()

                isResolving = true
                value = lambda(context)
            } finally {
                isResolving = false
            }
        }
        return value
    }

    fun clear() {
        value = Int.MIN_VALUE
    }
}

internal class PositionConstraint(
    var point: Point = Point.Min,
    lambda: (LayoutContainer.() -> Int)? = null
) : Constraint() {
    var offset: Int = 0
    init {
        this.lambda = lambda
    }

    override fun resolve(): Int {
        return super.resolve() + offset
    }
}