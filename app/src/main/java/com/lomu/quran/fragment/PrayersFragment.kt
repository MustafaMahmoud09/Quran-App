package com.lomu.quran.fragment



import com.lomu.quran.R
import com.lomu.quran.adapter.PrayersRecycler
import com.lomu.quran.manager.ManageData
import kotlinx.android.synthetic.main.fragment_prayers.*


class PrayersFragment : BaseFragment(R.layout.fragment_prayers) {

    override fun setup() {
         setTimeToDay()
         adapter()
    }//end setup()

    private fun setTimeToDay() {
        val dateToDay=ManageData.getDatToDay()
        textTimeH.text=dateToDay!!.timeH
        textTimeM.text=dateToDay.timeM
    }

    private fun adapter() {
        recyclerPrayerId.adapter=PrayersRecycler(ManageData.getDatePrayer())
    }

}//end class PrayersFragment