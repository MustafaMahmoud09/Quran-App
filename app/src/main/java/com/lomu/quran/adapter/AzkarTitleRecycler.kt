package com.lomu.quran.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.lomu.quran.R
import com.lomu.quran.activity.ZekrActivity
import com.lomu.quran.auxiliary_data.Azkar
import com.lomu.quran.manager.Constant
import java.util.*

class AzkarTitleRecycler(private val list:LinkedList<Azkar>, private val context: Context):RecyclerView.Adapter<AzkarTitleRecycler.Holder>() {

    class Holder(view:View) : RecyclerView.ViewHolder(view){

        val numberZekr:TextView=view.findViewById(R.id.numberZekrId)
        val nameZekr:TextView=view.findViewById(R.id.NameZekrId)
        val parentContainer:CardView=view.findViewById(R.id.parentTypeZekr)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        Holder(LayoutInflater.from(parent.context).inflate(R.layout.type_zekr_in_recycler,parent,false))

    override fun onBindViewHolder(holder: Holder, position: Int) {

        setData(holder,position)
        callback(holder,position)

    }

    private fun callback(holder: Holder, position: Int) {

        holder.parentContainer.setOnClickListener {

            actionAfterClickOnZekr(position)

        }

    }

    private fun actionAfterClickOnZekr(position: Int) {

            val numberItem=list[position].id

            val intent = Intent(context, ZekrActivity::class.java)
            intent.putExtra(Constant.ID_ZEKR_INTENT_KEY, numberItem)

            context.startActivity(intent)

    }

    private fun setData(holder: Holder, position: Int) {

        val item=list[position]

        holder.apply {

            numberZekr.text=item.id.toString()
            nameZekr.text=item.category

        }

    }

    override fun getItemCount()=list.size
}