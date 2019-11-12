package com.lly.snp.example

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.lly.snp.*
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ContainView(this))
    }
}

class ContainView(context:Context):ConstraintLayout(context){
    private lateinit var name:TextView
    private lateinit var nameX:TextView
    private lateinit var nameY:TextView
    private lateinit var avatar:ImageView
    private lateinit var scrollView: View

    override fun configSubViews() {
        super.configSubViews()
        name = TextView(context).apply {
            text = "SnapKit Example"
            setTextColor(0x2A4F6E.color)
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            setBackgroundColor(0xBAFCF6.color)
            setTextSize(TypedValue.COMPLEX_UNIT_DIP,18f)
        }
        this.addSubview(name)

        nameX = TextView(context).apply {
            text = "centerX"
            setTextColor(0x000000.color)
            textAlignment = View.TEXT_ALIGNMENT_TEXT_START
            setBackgroundColor(0xFFAA5A39.color)
            setTextSize(TypedValue.COMPLEX_UNIT_DIP,18f)
        }
        this.addSubview(nameX)

        nameY = TextView(context).apply {
            text = "centerY"
            setTextColor(0xFFFFFF.color)
            textAlignment = View.TEXT_ALIGNMENT_TEXT_END
            setBackgroundColor(0xFFAA5A39.color)
            setTextSize(TypedValue.COMPLEX_UNIT_DIP,18f)
        }
        this.addSubview(nameY)

        avatar = ImageView(context).apply {
            setImageResource(R.drawable.avpick_ok)
        }
        this.addSubview(avatar)

        scrollView = TableView(context).apply {
            this.list.adapter = EmListAdapter(context)
            this.view.setBackgroundColor(0xFFFFFF.color)
            this.list.layoutManager = LinearLayoutManager(context)
        }.view
        this.addSubview(scrollView)
    }

    override fun configCs() {
        super.configCs()
        name.snp.makeConstraints {
            it.leftTo(this).offset(10.dip)
            it.topTo(this).offset(50.dip)
            it.rightTo(this).offset(-10.dip)
            it.heightTo (55.dip)
        }
        nameX.snp.makeConstraints{
            it.topTo(name.snp.bottom).offset(10.dip)
            it.heightTo(name).centerXTo(name)
            it.widthTo(100.dip)
        }
        nameY.snp.makeConstraints {
            it.heightTo(30.dip).centerYTo(nameX).leftTo(nameX.snp.right).offset(10.dip)
            it.widthTo(80.dip)
        }
        avatar.snp.makeConstraints {
            it.centerTo(this)
            it.widthTo(50.dip).heightTo(50.dip)
        }

        scrollView.snp.makeConstraints {
            it.leftTo(this).widthTo(this).topTo(avatar.snp.bottom).bottomTo(this)
        }
    }

}


val Int.dip: Int get() = (Application.getContext().resources.displayMetrics.density * this).toInt()
val Float.dip: Float get() = Application.getContext().resources.displayMetrics.density * this

val Int.color: Int get() =  this or 0xFF.shl(24)
val Long.color :Int get() =  this.toInt().color
