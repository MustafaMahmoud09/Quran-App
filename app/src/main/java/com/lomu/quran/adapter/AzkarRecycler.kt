package com.lomu.quran.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.lomu.quran.R
import com.lomu.quran.auxiliary_data.Zekr
import java.lang.Exception
import java.util.*

class AzkarRecycler(private val list:LinkedList<Zekr>,private val context: Context) :RecyclerView.Adapter<AzkarRecycler.Holder>(){
    private var lastHolder:Holder?=null
    private var check=false
    private var positionVisible=-1

    class Holder(view : View) :RecyclerView.ViewHolder(view)  {

        val parentContainer:CardView=view.findViewById(R.id.parentZekId)
        val numberZekr:TextView=view.findViewById(R.id.numberZekrId)
        val textZekr:TextView=view.findViewById(R.id.zekrTextId)
        val shareZekr:AppCompatImageView=view.findViewById(R.id.shareBtn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_in_zekr_recycler,parent,false))

    override fun onBindViewHolder(holder: Holder, position: Int) {

        setDataZekr(holder,position)
        callBack(holder,position)
        checkVisibleOrNo(holder,position)

    }

    private fun checkVisibleOrNo(holder: Holder,position: Int) {

        if(position==positionVisible){

            holder.shareZekr.visibility=View.VISIBLE
            lastHolder=holder
            check=true

        }else{

            holder.shareZekr.visibility=View.INVISIBLE

        }
    }

    private fun callBack(holder: Holder, position: Int) {

        holder.parentContainer.setOnLongClickListener {

            codeLongClickOnContainer(holder,position)
            true
        }
        holder.parentContainer.setOnClickListener {

            codeClickOnContainer(holder)
        }

        holder.shareZekr.setOnClickListener {

            codeShareZekr(holder,position)
        }

    }

    private fun codeShareZekr(holder: Holder, position: Int) {

        val intent=Intent(Intent.ACTION_SEND)

        intent.putExtra(Intent.EXTRA_TEXT,list[position].text)
        intent.type="text/plain"

        try {

            context.startActivity(intent)

        }catch (ex:Exception){

            Toast.makeText(context,context.getString(R.string.not_application_found),Toast.LENGTH_SHORT).show()

        }
    }

    private fun codeClickOnContainer(holder: AzkarRecycler.Holder) {

        if(holder.shareZekr.isVisible){

            holder.shareZekr.visibility=View.INVISIBLE
            lastHolder=null
            check=false

        }
    }

    private fun codeLongClickOnContainer(holder: Holder,position: Int) {

        if(check){

            lastHolder?.shareZekr?.visibility=View.INVISIBLE
        }

        holder.shareZekr.visibility=View.VISIBLE
        lastHolder=holder
        check=true
        positionVisible=position

    }

    private fun setDataZekr(holder: Holder, position: Int) {

        val item=list[position]

        holder.apply {

            numberZekr.text=item.id.toString()
            textZekr.text=item.text
        }

    }


    override fun getItemCount()=list.size


}