package com.lomu.quran.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class FragmentAdapter(fr:Fragment,private val list:List<Fragment>):FragmentStateAdapter(fr){

    override fun getItemCount()=list.size

    override fun createFragment(position: Int)=list[position]

}//end FragmentAdapter