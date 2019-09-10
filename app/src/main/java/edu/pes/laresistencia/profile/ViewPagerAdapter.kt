package edu.pes.laresistencia.profile

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import com.example.api.laresistencia.R

class ViewPagerAdapter(fm: FragmentManager, data: Bundle, context: Context?) : FragmentStatePagerAdapter(fm)
{
    private val bundle: Bundle = data
    private var listTitles: ArrayList<String> = arrayListOf(context?.resources?.getString(R.string.profile_personal_title).toString(), context?.resources?.getString(R.string.profile_medical_title).toString())
    private lateinit var listFragments: ArrayList<Fragment>

    override fun getItem(position: Int): Fragment
    {
        val frag = HealthProfileFragment()
        frag.arguments = bundle
        listFragments = arrayListOf(UserPersonalFragment(), frag)
        return listFragments[position]
    }

    override fun getCount(): Int
    {
        return listTitles.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return listTitles[position]
    }
}