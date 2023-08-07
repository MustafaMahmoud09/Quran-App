package com.lomu.quran.fragment

import com.lomu.quran.R
import com.lomu.quran.adapter.SurahsSoundRecycler
import com.lomu.quran.manager.ManageData
import kotlinx.android.synthetic.main.fragment_sound.*

class SoundFragment : BaseFragment(R.layout.fragment_sound){
    override fun setup() {

        adapter()

    }

   private fun adapter(){

       recyclerSoundId.adapter= context?.let { SurahsSoundRecycler(ManageData.getSurahTitle(), it) }

   }

}