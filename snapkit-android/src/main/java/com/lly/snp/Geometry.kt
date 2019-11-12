package com.lly.snp

interface Geometry {

    val left: Int
    val right: Int
    val width: Int
    val centerX: Int

    val top: Int
    val bottom: Int
    val height: Int
    val centerY: Int

    val snp:Geometry
}

class ParentGeometry(
    val widthConfig: SizeConfig,
    val heightConfig: SizeConfig
) : Geometry {

    override val left: Int = 0
    override val right: Int get() = widthConfig.resolve()
    override val width: Int get() = widthConfig.resolve()
    override val centerX: Int get() = widthConfig.resolve()/2

    override val top: Int = 0
    override val bottom: Int get() = heightConfig.resolve()
    override val height: Int get() = heightConfig.resolve()
    override val centerY: Int get() = heightConfig.resolve()/2

    override val snp: Geometry get() = this

}
