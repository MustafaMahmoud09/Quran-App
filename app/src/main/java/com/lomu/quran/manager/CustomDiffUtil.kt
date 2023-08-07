package com.lomu.quran.manager

import androidx.recyclerview.widget.DiffUtil
import com.lomu.quran.auxiliary_data.TitleSurah
import java.util.*

class CustomDiffUtil(private val oldList:LinkedList<TitleSurah>,private val newList:LinkedList<TitleSurah>) : DiffUtil.Callback(){

    override fun getOldListSize()=oldList.size

    override fun getNewListSize()=newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int)=oldList[oldItemPosition].number==newList[newItemPosition].number

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int)=true
}