package com.lomu.quran.fragment

import com.lomu.quran.R
import com.lomu.quran.adapter.AzkarTitleRecycler
import com.lomu.quran.manager.ManageData
import kotlinx.android.synthetic.main.fragment_azkar.*

class AzkarFragment : BaseFragment(R.layout.fragment_azkar){

    override fun setup() {

        adapter()

    }

    private fun adapter() {

        recyclerTypeAzkarId.adapter= context?.let { AzkarTitleRecycler(ManageData.getAzkar(), it) }

    }

}