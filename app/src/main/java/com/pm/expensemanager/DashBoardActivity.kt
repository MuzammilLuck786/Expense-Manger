package com.pm.expensemanager

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputEditText
import com.pm.expensemanager.databinding.ActivityDashBoardBinding

import com.pm.expensemanager.expensemodel.*
import com.pm.expensemanager.fragment.fragment.TabAdapter
import com.pm.expensemanager.others.Validation
import com.pm.expensemanager.user.UserProfile
import com.pm.expensemanager.usermodel.DBHelperUser
import com.pm.expensemanager.usermodel.UserViewModel
import com.pm.expensemanager.usermodel.UserViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.exp

class DashBoardActivity : AppCompatActivity() {
    lateinit var binding:ActivityDashBoardBinding
    lateinit var tabAdapter: TabAdapter
    var totalIncome:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager2=binding.viewPager2
        val tabLayout=binding.tabLayout
        tabAdapter = TabAdapter(this)
        viewPager2.setAdapter(tabAdapter)

        TabLayoutMediator(tabLayout,viewPager2) { tab, position ->
            when(position){
                0 -> tab.text= "All"
                1 -> tab.text= "Income"
                2 -> tab.text= "Expense"
            }

        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager2.setCurrentItem(tab.position)
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        binding.viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.getTabAt(position)?.select()
            }
        })
        
        binding.floatActinBtnAdd.setOnClickListener {
            val intent=Intent(this@DashBoardActivity,AddIcomeAndExpese::class.java)
            startActivity(intent)
        }

        binding.profile.setOnClickListener {
            val intent=Intent(this, UserProfile::class.java)
            startActivity(intent)
        }


    }
}