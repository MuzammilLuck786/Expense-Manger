package com.pm.expensemanager.fragment.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pm.expensemanager.fragment.fragment.AllFragment
import com.pm.expensemanager.fragment.fragment.ExpenseFragment
import com.pm.expensemanager.fragment.fragment.IncomeFragment

class TabAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllFragment()
            1 -> IncomeFragment()
            2 -> ExpenseFragment()
            else -> AllFragment()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}
