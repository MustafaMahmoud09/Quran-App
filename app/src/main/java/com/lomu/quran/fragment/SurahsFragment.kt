package com.lomu.quran.fragment


import com.lomu.quran.R
import com.lomu.quran.manager.ManageData
import com.lomu.quran.adapter.SurahTitleRecycler
import kotlinx.android.synthetic.main.fragment_surahs.*


class SurahsFragment : BaseFragment(R.layout.fragment_surahs) {

    override fun setup() {

        adapter()

    }//end setup()

    private fun adapter() {
        surahTitleRecycler.adapter =
            context?.let {
                 SurahTitleRecycler(ManageData.getSurahTitle(), it,true)
                 }
    }

}//end class SurahFragment