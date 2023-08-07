package com.lomu.quran.activity

import android.os.Bundle
import com.lomu.quran.R
import com.lomu.quran.adapter.AzkarRecycler
import com.lomu.quran.manager.Constant
import com.lomu.quran.manager.ManageData
import kotlinx.android.synthetic.main.activity_zekr.*


class ZekrActivity : BaseActivity(R.layout.activity_zekr) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNameZekr()
        adapter()
    }

    private fun getIdZek():Int ?{

        return intent.extras?.getInt(Constant.ID_ZEKR_INTENT_KEY,0)

    }

    private fun adapter() {

        val item=ManageData.getAzkar()[getIdZek()!!-1]

        recyclerShowAzkarId.adapter=AzkarRecycler(item.array,this)

    }

    private fun setNameZekr() {

        val item=ManageData.getAzkar()[ getIdZek()!!-1]

        nameZekrId.text=item.category

    }

    override fun setup() {

        callback()

    }

    private fun callback() {

        btnBackZekr.setOnClickListener {
            finish()
        }

    }
}