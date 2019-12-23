package com.dice.themovie.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter (fm: FragmentManager, private val fragmentList: List<Fragment>, private val titleList: List<String>): FragmentPagerAdapter(fm){
    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }
}