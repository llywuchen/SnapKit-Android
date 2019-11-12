package com.lly.snp.example

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lly.snp.ConstraintLayout
import com.lly.snp.snp

open class TableView(context: Context) {

    open val view = View.inflate(context,R.layout.list_view,null)

    open val list : RecyclerView
        get() {return  view.findViewById<RecyclerView>(R.id.listView) as RecyclerView
        }

    var adapter: TableViewAdapter
        get() {return list.adapter as TableViewAdapter}
        set(value) {list.adapter = value}

}

open class TableViewCell:ConstraintLayout{
    var position = 0
    constructor(context: Context, viewType: Int = 0):super(context){}

    open fun bindData(data:Any?){}
}

class TViewHolder : RecyclerView.ViewHolder {
    val view:ConstraintLayout
    var cell:TableViewCell? = null
    constructor(container: View):super(container){
        view = container!!.findViewById<ConstraintLayout>(R.id.view)
    }

    fun congfigCell(cell:TableViewCell){
        this.cell = cell
        view.addSubview(cell)
        view.makeSubConstraints(cell.snp,{
            it.edgesTo(this.view)
        })
    }

}

abstract class TableViewAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    val context = context

    abstract fun getCell(viewType:Int):TableViewCell

    abstract fun tableViewNumberOfRows():Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val Container= View.inflate(context,R.layout.list_view_item,null)
        val  holder = TViewHolder(Container)
        holder.view.contourHeightOf { tableViewHeightForViewType(viewType).toInt() }
        val cell = getCell(viewType)
        cell.setOnClickListener{
            tableViewDidSelectRowAt(cell.position)
        }
        holder.congfigCell(cell)
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h = holder as TViewHolder
        h.cell?.position = position
        h.cell?.bindData(null)
    }

    override fun getItemCount(): Int {
        return tableViewNumberOfRows()
    }

    open fun tableViewDidSelectRowAt(position: Int){

    }

    open fun tableViewHeightForViewType(viewType:Int): Float{
        return 45.0f.dip
    }


}