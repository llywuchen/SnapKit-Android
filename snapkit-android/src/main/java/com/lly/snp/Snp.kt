package com.lly.snp

import android.view.View

val View.snp :Snp get() = Snp(this)

class Snp(view :View) {

    var sview :View = view

    val left :LayoutContainer.() -> Int get() = {sview.left}
    val right :LayoutContainer.() -> Int get() = {sview.right}
    val top :LayoutContainer.() -> Int get() = {sview.top}
    val bottom :LayoutContainer.() -> Int get() = {sview.bottom}
    val width :LayoutContainer.() -> Int get() = {sview.width}
    val height :LayoutContainer.() -> Int get() = {sview.height}
    val centerX :LayoutContainer.() -> Int get() = {sview.left + sview.width/2}
    val centerY :LayoutContainer.() -> Int get() = {sview.top + sview.height/2}

}