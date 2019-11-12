package com.lly.snp

import android.view.View

interface LayoutContainer {
    val view: Geometry
}

interface LayoutConstraint:AxisSolver {
    fun widthTo(
        provider: LayoutContainer.() -> Int
    ): LayoutConstraint

    fun widthTo(
        provider: Int
    ): LayoutConstraint

    fun heightTo(
        provider: LayoutContainer.() -> Int
    ): LayoutConstraint

    fun heightTo(
        provider: Int
    ): LayoutConstraint

    fun rightTo(
        provider: LayoutContainer.() -> Int
    ): LayoutConstraint

    fun rightTo(
        provider: Int
    ): LayoutConstraint

    fun leftTo(
        provider: LayoutContainer.() -> Int
    ): LayoutConstraint

    fun leftTo(
        provider: Int
    ): LayoutConstraint

    fun bottomTo(
        provider: LayoutContainer.() -> Int
    ): LayoutConstraint

    fun bottomTo(
        provider: Int
    ): LayoutConstraint

    fun topTo(
        provider: LayoutContainer.() -> Int
    ): LayoutConstraint

    fun topTo(
        provider: Int
    ): LayoutConstraint

    fun centerXTo(
        provider: LayoutContainer.() -> Int
    ): LayoutConstraint

    fun centerXTo(
        provider: Int
    ): LayoutConstraint

    fun centerYTo(
        provider: LayoutContainer.() -> Int
    ): LayoutConstraint

    fun centerYTo(
        provider: Int
    ): LayoutConstraint



    fun offset(offset:Int)

    fun widthTo(
        provider: View
    ): LayoutConstraint {
        return widthTo(provider.snp.width)
    }

    fun heightTo(
        provider: View
    ): LayoutConstraint {
        return heightTo(provider.snp.height)
    }

    fun leftTo(
        provider: View
    ): LayoutConstraint {
        return leftTo(provider.snp.left)
    }

    fun rightTo(
        provider: View
    ): LayoutConstraint {
        return rightTo(provider.snp.right)
    }

    fun topTo(
        provider: View
    ): LayoutConstraint {
        return topTo(provider.snp.top)
    }

    fun bottomTo(
        provider: View
    ): LayoutConstraint {
        return bottomTo(provider.snp.bottom)
    }

    fun centerXTo(
        provider: View
    ): LayoutConstraint {
        return centerXTo(provider.snp.centerX)
    }

    fun centerYTo(
        provider: View
    ): LayoutConstraint {
        return centerYTo(provider.snp.centerY)
    }

    fun centerTo(
        provider: View
    ): LayoutConstraint{
        return centerXTo(provider).centerYTo(provider)
    }

    fun edgesTo(
        provider: View
    ): LayoutConstraint{
        return leftTo(provider).widthTo(provider).topTo(provider).heightTo(provider)
    }

}