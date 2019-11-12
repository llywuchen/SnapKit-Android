package com.lly.snp.example

import android.content.Context
import android.widget.TextView
import com.lly.snp.snp

class EmListAdapter(mContext: Context) :TableViewAdapter(mContext){
    private val cellClassList :Array<()->TableViewCell> = arrayOf({EmItemView(0,context)},{EmItemCusView(1,context)})

    override fun getCell(viewType:Int):TableViewCell{
        return cellClassList[viewType]()
    }

    override fun tableViewNumberOfRows() : Int {
        return 50
    }

    override fun tableViewHeightForViewType(viewType: Int): Float {
        return if (viewType>0) 75.0f.dip else 35.0f.dip
    }

    override fun getItemViewType(position: Int): Int {
        return  if (position %2 == 0) 0 else 1
    }

    override fun tableViewDidSelectRowAt(position: Int) {
        print("did sel:"+ position.toString())
    }
}



class EmItemView(viewType:Int = 0,context: Context) :TableViewCell(context) {
    var name:TextView = TextView(context)

    override fun configSubViews() {
        super.configSubViews()
        name.apply {
            this.setBackgroundColor(0x125432.color)
        }
        addSubview(name)
    }

    override fun configCs() {
        super.configCs()
        name.snp.makeConstraints {
            it.leftTo(this).widthTo(this).topTo(this).bottomTo(this)
        }
    }

    override fun bindData(data: Any?) {
        super.bindData(data)
        name.text = String.format("EmItemView : %d",position)
    }

}

class EmItemCusView(viewType:Int = 0,mContext: Context) :TableViewCell(mContext) {
    var name:TextView = TextView(mContext)

    override fun configSubViews() {
        super.configSubViews()
        name.apply {
            this.setBackgroundColor(0x137482.color)
        }
        addSubview(name)
    }

    override fun configCs() {
        super.configCs()
        name.snp.makeConstraints {
            it.rightTo(this).widthTo(this).topTo(this).bottomTo(this)
        }
    }

    override fun bindData(data: Any?) {
        super.bindData(data)
        name.text = String.format("EmItemCusView : %d",position)
    }

}