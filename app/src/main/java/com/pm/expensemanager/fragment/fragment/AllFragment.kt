package com.pm.expensemanager.fragment.fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.helper.widget.MotionEffect.TAG
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.pm.expensemanager.AddIcomeAndExpese
import com.pm.expensemanager.R
import com.pm.expensemanager.adapters.All_IncomeExpenseAdapter
import com.pm.expensemanager.adapters.NoteClickDeleteInterface
import com.pm.expensemanager.databinding.FragmentAllBinding
import com.pm.expensemanager.expensemodel.*
import com.pm.expensemanager.others.Validation
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class AllFragment : Fragment(), NoteClickDeleteInterface {
    private var binding: FragmentAllBinding? = null
    private lateinit var allIncomeexpenseadapter: All_IncomeExpenseAdapter
    private lateinit var expenseViewModel: ExpenseViewModel
    var totalIncome= 0.0
    var totalExpense= 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllBinding.inflate(inflater, container, false)

        val dbHelper = DBHelperExpense.getDB(requireContext())
        val expenseDao = dbHelper.expenseDao()
        val expenseRepository = ExpenseRepository(expenseDao)
        val expenseViewModelFactory = ExpenseViewModelFactory(expenseRepository)
        expenseViewModel = ViewModelProvider(this, expenseViewModelFactory)[ExpenseViewModel::class.java]

        expenseViewModel.allIncomeExpenseList.observe(viewLifecycleOwner) { list ->
            list?.let {
                // on below line we are updating our list.
                allIncomeexpenseadapter.updateList(it)
            }
        }



        binding!!.rvAllIncomeExpense.layoutManager=LinearLayoutManager(requireContext())
        binding!!.rvAllIncomeExpense.setHasFixedSize(true)
        allIncomeexpenseadapter= All_IncomeExpenseAdapter(requireContext(),this)
        binding!!.rvAllIncomeExpense.adapter=allIncomeexpenseadapter

        val statusIncome = "Income"
        val statusExpense = "Expense"

        expenseViewModel.getTotalIncome(statusIncome).observe(viewLifecycleOwner) { incomes ->
            var totalIncome = 0.0

            for (income in incomes) {
                val amount = income.amount.toDoubleOrNull() ?: 0.0
                totalIncome += amount
            }

            binding?.profitTextView?.text = "Income: ${totalIncome.toString()} PKR"

            expenseViewModel.getTotalIncome(statusExpense).observe(viewLifecycleOwner) { expenses ->
                var totalExpense = 0.0

                for (expense in expenses) {
                    val amount = expense.amount.toDoubleOrNull() ?: 0.0
                    totalExpense += amount
                }

                binding?.lossTextView?.text = "Expense: ${totalExpense.toString()} PKR"

                if (totalIncome == totalExpense) {
                    binding?.tvCheckProfitLoss?.text = "Your Expenses Are Equals To Income"
                    binding?.layout?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))
                } else if (totalIncome < totalExpense) {
                    binding?.tvCheckProfitLoss?.text = "You're in Loss"
                    binding?.layout?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
                } else if (totalIncome > totalExpense) {
                    binding?.tvCheckProfitLoss?.text = "You're in Profit"
                    binding?.layout?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
                }
            }
        }

        binding!!.tvdateSearch.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val selectedDate = "$year-${month + 1}-$dayOfMonth" // Format as needed
                    binding!!.tvdateSearch.text = "$selectedDate"
                },
                year,
                month,
                day
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis() // Prevent future dates
            datePickerDialog.show()
        }
        binding!!.btnSearchdate.setOnClickListener {
            val date = binding!!.tvdateSearch.text.toString().trim()
            if (date.isEmpty()) {
                Toast.makeText(requireContext(),"Select Date",Toast.LENGTH_LONG).show()
            }
            else{
                expenseViewModel.filterDate(date).observe(viewLifecycleOwner) { list ->
                    list?.let {
                        allIncomeexpenseadapter.updateList(it)
                    }
                }
            }
        }

        binding!!.btnclear.setOnClickListener {
            expenseViewModel.allIncomeExpenseList.observe(viewLifecycleOwner) { list ->
                list?.let {
                    // on below line we are updating our list.
                    allIncomeexpenseadapter.updateList(it)
                }
            }

        }

        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onDeleteIconClick(incomeExpenseEntity: IncomeExpenseEntity) {
        // in on note click method we are calling delete
        // method from our view modal to delete our not.
        expenseViewModel.deleteExpenseById(incomeExpenseEntity.id)
        // displaying a toast message
        Toast.makeText(requireContext(), "${incomeExpenseEntity.status} Deleted", Toast.LENGTH_LONG).show()
    }

}
