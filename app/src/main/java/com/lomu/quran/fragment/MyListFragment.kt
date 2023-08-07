package com.lomu.quran.fragment

import com.lomu.quran.R
import com.lomu.quran.adapter.SurahTitleRecycler
import com.lomu.quran.manager.ManageData
import kotlinx.android.synthetic.main.fragment_my_list.*

class MyListFragment : BaseFragment(R.layout.fragment_my_list){
    override fun onResume() {
        super.onResume()
        setup()
    }

    override fun setup() {
     adapter()
    }//end setup()

    private fun adapter() {
        recyclerMyList.adapter= context?.let {
            SurahTitleRecycler(ManageData.getMyList(),
                it,false)
        }
    }

}//end class MyListFragment