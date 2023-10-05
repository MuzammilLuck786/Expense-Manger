package com.pm.expensemanager.fragment.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pm.expensemanager.adapters.All_IncomeExpenseAdapter
import com.pm.expensemanager.adapters.NoteClickDeleteInterface
import com.pm.expensemanager.databinding.FragmentExpenseBinding
import com.pm.expensemanager.databinding.FragmentIncomeBinding
import com.pm.expensemanager.expensemodel.*

class IncomeFragment : Fragment(),NoteClickDeleteInterface {
    private lateinit var allIncomeexpenseadapter: All_IncomeExpenseAdapter
    private lateinit var expenseViewModel: ExpenseViewModel
    private var binding: FragmentIncomeBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentIncomeBinding.inflate(inflater,container,false)

        val dbHelper = DBHelperExpense.getDB(requireContext())
        val expenseDao = dbHelper.expenseDao()
        val expenseRepository = ExpenseRepository(expenseDao)
        val expenseViewModelFactory = ExpenseViewModelFactory(expenseRepository)
        expenseViewModel = ViewModelProvider(this, expenseViewModelFactory)[ExpenseViewModel::class.java]

        binding!!.rvIncome.layoutManager= LinearLayoutManager(requireContext())
        allIncomeexpenseadapter= All_IncomeExpenseAdapter(requireContext(),this)
        binding!!.rvIncome.adapter=allIncomeexpenseadapter

        val status="Income"
        expenseViewModel.getIncomeOrExpenseList(status).observe(viewLifecycleOwner) { list ->
            list?.let {
                // on below line we are updating our list.
                allIncomeexpenseadapter.updateList(it)
            }
        }
        val statusIncome="Income"

        expenseViewModel.getTotalIncome(statusIncome).observe(viewLifecycleOwner){
            var total=0.0
            for (totalExpense in it){
                var amount=totalExpense.amount.toDoubleOrNull() ?: 0.0
                total += amount
            }
            binding!!.tvIncome.text = "Total Income: ${total.toString()} PKR"
        }

        return binding!!.root
    }

    override fun onDeleteIconClick(incomeExpenseEntity: IncomeExpenseEntity) {
        // in on note click method we are calling delete
        // method from our view modal to delete our not.
        expenseViewModel.deleteExpenseById(incomeExpenseEntity.id)
        // displaying a toast message
        Toast.makeText(requireContext(), "${incomeExpenseEntity.status} Deleted", Toast.LENGTH_LONG).show()
    }
}