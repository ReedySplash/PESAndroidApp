package edu.pes.laresistencia.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Parcel
import android.os.Parcelable
import android.util.Base64
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

enum class Gender
{
    MALE,
    FEMALE,
    OTHER
}

class User
{
    companion object {
        var name: String? = null
        var surname: String? = null
        var gender: Gender? = null
        var birthDate: String? = null
        var email: String? = null
        var country: String? = null

        //parcel

        //ours
        fun setData(info: ArrayList<String>)
        {
            name = info[0]
            surname = info[1]
            gender = Gender.valueOf(info[2])
            birthDate = info[3]
            email = info[4]
            country = info[5]
        }

        fun getData(): ArrayList<String>
        {
            var array: ArrayList<String> = ArrayList()
            array.add(name!!)
            array.add(surname!!)
            array.add(gender.toString())
            array.add(birthDate!!)
            array.add(email!!)
            array.add(country!!)
            return array
        }
    }

}
