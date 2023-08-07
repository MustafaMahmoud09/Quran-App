package com.lomu.quran.fragment

import com.lomu.quran.R
import com.lomu.quran.adapter.JuzTitleRecycler
import com.lomu.quran.manager.ManageData
import kotlinx.android.synthetic.main.fragment_juz.*

class JuzFragment: BaseFragment(R.layout.fragment_juz) {

    override fun setup() {

        adapter()

    }//end setup()

    private fun adapter() {
        juzTitleRecycler.adapter= JuzTitleRecycler(ManageData.getJuzTitle(),requireActivity().baseContext)
    }


}//end class JuzFragment