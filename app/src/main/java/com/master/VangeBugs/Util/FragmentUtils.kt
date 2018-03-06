package com.master.VangeBugs.Util

import android.app.FragmentTransaction
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

/**
 * Created by 不听话的好孩子 on 2018/2/1.
 */
class FragmentUtils {
    companion object {
        fun showAddFragment(activity: FragmentActivity, id: Int, fragment: Fragment) {
            activity.supportFragmentManager
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .add(id, fragment)
                    .addToBackStack(null)
                    .commit()
        }

        fun showAddFragment(activity: FragmentActivity, fragment: Fragment) {
            showAddFragment(activity, android.R.id.content, fragment)
        }



        fun showReplaceFragment(activity: FragmentActivity, id: Int, fragment: Fragment) {
            activity.supportFragmentManager
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(id, fragment)
                    .addToBackStack(null)
                    .commit()
        }

        fun showReplaceFragment(activity: FragmentActivity, fragment: Fragment) {
            showReplaceFragment(activity, android.R.id.content, fragment)
        }

        fun addFragment(activity: FragmentActivity, fragment: Fragment) {
            activity.supportFragmentManager
                    .beginTransaction()
                    .add(fragment, fragment.tag)
                    .commit()
        }

        fun showFragment(activity: FragmentActivity, fragment: Fragment) {
            activity.supportFragmentManager
                    .beginTransaction()
                    .show(fragment)
                    .commit()
        }

        fun hideFragment(activity: FragmentActivity, fragment: Fragment) {
            activity.supportFragmentManager
                    .beginTransaction()
                    .hide(fragment)
                    .commit()
        }

        fun popBackStack(activity: FragmentActivity) {
            activity.supportFragmentManager
                    .popBackStack()
        }




        fun showFragmentReplaceFragment(activity: Fragment, id: Int, fragment: Fragment) {
            activity.fragmentManager
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(id, fragment)
                    .addToBackStack(null)
                    .commit()
        }

        fun showFragmentReplaceFragment(activity: Fragment, fragment: Fragment) {
            showFragmentReplaceFragment(activity, android.R.id.content, fragment)
        }
        fun showFragmentAddFragment(activity: Fragment, id: Int, fragment: Fragment) {
            activity.fragmentManager
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                    .add(id, fragment)
                    .addToBackStack(null)
                    .commit()
        }

        fun showFragmentAddFragment(activity: Fragment, fragment: Fragment) {
            showFragmentAddFragment(activity, android.R.id.content, fragment)
        }

    }
}