package com.lomu.quran.auxiliary_data

import android.os.Parcel
import android.os.Parcelable

class TitleSurah(
    val number:Int,
    val name: String?,
    val englishName: String?,
    val englishNameTranslation: String?,
    val numberOfAyahs:Int,
    val revelationType: String?
):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun describeContents()=0

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.apply {
            writeInt(number)
            writeString(name)
            writeString(englishName)
            writeString(englishNameTranslation)
            writeInt(numberOfAyahs)
            writeString(revelationType)
        }
    }

    companion object CREATOR : Parcelable.Creator<TitleSurah> {
        override fun createFromParcel(parcel: Parcel): TitleSurah {
            return TitleSurah(parcel)
        }

        override fun newArray(size: Int): Array<TitleSurah?> {
            return arrayOfNulls(size)
        }
    }

}
