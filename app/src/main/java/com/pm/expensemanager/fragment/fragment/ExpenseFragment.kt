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
import com.pm.expensemanager.expensemodel.*

class ExpenseFragment : Fragment(), NoteClickDeleteInterface {
    private var binding: FragmentExpenseBinding? = null
    private lateinit var allIncomeexpenseadapter: All_IncomeExpenseAdapter
    private lateinit var expenseViewModel: ExpenseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentExpenseBinding.inflate(inflater,container,false)

        val dbHelper = DBHelperExpense.getDB(requireContext())
        val expenseDao = dbHelper.expenseDao()
        val expenseRepository = ExpenseRepository(expenseDao)
        val expenseViewModelFactory = ExpenseViewModelFactory(expenseRepository)
        expenseViewModel = ViewModelProvider(this, expenseViewModelFactory)[ExpenseViewModel::class.java]

        binding!!.rvExpense.layoutManager=LinearLayoutManager(requireContext())
        allIncomeexpenseadapter= All_IncomeExpenseAdapter(requireContext(),this)
        binding!!.rvExpense.adapter=allIncomeexpenseadapter

        val status="Expense"
        expenseViewModel.getIncomeOrExpenseList(status).observe(viewLifecycleOwner) { list ->
            list?.let {
                allIncomeexpenseadapter.updateList(it)
            }
        }

        val statusExpense="Expense"
        expenseViewModel.getTotalIncome(statusExpense).observe(viewLifecycleOwner){
            var total=0.0
            for (totalExpense in it){
                var amount=totalExpense.amount.toDoubleOrNull() ?: 0.0
                total += amount
            }
            binding!!.tvExpense.text = "Total Expense: ${total.toString()} PKR"
        }

        return binding!!.root
    }

    override fun onDeleteIconClick(incomeExpenseEntity: IncomeExpenseEntity) {
        expenseViewModel.deleteExpenseById(incomeExpenseEntity.id)
        // displaying a toast message
        Toast.makeText(requireContext(), "${incomeExpenseEntity.status} Deleted", Toast.LENGTH_LONG).show()
    }
}