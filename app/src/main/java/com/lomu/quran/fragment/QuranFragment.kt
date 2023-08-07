package com.lomu.quran.fragment


import android.content.Context
import com.google.android.material.tabs.TabLayoutMediator
import com.lomu.quran.R
import com.lomu.quran.adapter.FragmentAdapter
import com.lomu.quran.manager.Constant
import com.lomu.quran.manager.ManageData
import kotlinx.android.synthetic.main.fragment_quran.*
import kotlinx.android.synthetic.main.fragment_quran.textSaved


class QuranFragment : BaseFragment(R.layout.fragment_quran) {

    override fun setup() {
          ayahSaved()
          fragmentIntoViewPager()
    }//end setup()

    private fun ayahSaved() {
         val shared=requireActivity().getSharedPreferences(Constant.SHARED_NAME_AYAH_SAVED, Context.MODE_PRIVATE)
         textSaved.text= shared?.getString(Constant.DATA_AYAH_SAVED,"")
    }

    private fun fragmentIntoViewPager() {

        val list= listOf(getString(R.string.surah),getString(R.string.juz),getString(R.string.my_list))

        viewPagerFragmentId.adapter= FragmentAdapter(this,ManageData.getFragmentList())

        TabLayoutMediator(tabQuranId,viewPagerFragmentId) {tab,position->
            tab.text=list[position]
        }.attach()

    }


}//end class QuranFragment