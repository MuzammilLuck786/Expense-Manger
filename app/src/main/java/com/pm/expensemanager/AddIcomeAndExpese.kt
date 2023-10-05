package com.pm.expensemanager

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.pm.expensemanager.databinding.ActivityAddIcomeAndExpeseBinding
import com.pm.expensemanager.databinding.ActivityDashBoardBinding
import com.pm.expensemanager.expensemodel.*
import com.pm.expensemanager.fragment.fragment.AllFragment
import com.pm.expensemanager.others.Validation
import java.text.SimpleDateFormat
import java.util.*

class AddIcomeAndExpese : AppCompatActivity() {
    lateinit var binding : ActivityAddIcomeAndExpeseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddIcomeAndExpeseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val statusList = resources.getStringArray(R.array.Status)
        val adapterStatus = ArrayAdapter(this, android.R.layout.simple_spinner_item, statusList)
        binding.spStatus.adapter= adapterStatus

        val dbHelper= DBHelperExpense.getDB(this@AddIcomeAndExpese)
        val expenseDao = dbHelper.expenseDao()
        val expenseRepository = ExpenseRepository(expenseDao)
        val expenseViewModelFactory = ExpenseViewModelFactory(expenseRepository)
        val expenseViewModel =
            ViewModelProvider(this, expenseViewModelFactory)[ExpenseViewModel::class.java]

        val incomeExpese = intent.getSerializableExtra("IncomeExpense") as IncomeExpenseEntity?
        val position = adapterStatus.getPosition(incomeExpese?.status)

        if (incomeExpese != null){
            binding.edAmount.setText(incomeExpese.amount.toString())
            binding.spStatus.setSelection(position)
            binding.edReason.setText(incomeExpese.reason)
            binding.edDescription.setText(incomeExpese.description)
            binding.edSetDate.setText(incomeExpese.date)
            binding.addIncomeExpense.setText("Update")
        }



        binding.showDatePicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedDate = "$year-${month + 1}-$dayOfMonth" // Format as needed
                    binding!!.edSetDate.text = "$selectedDate"
                },
                year,
                month,
                day
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis() // Prevent future dates
            datePickerDialog.show()
        }

        binding.addIncomeExpense.setOnClickListener {
            if (incomeExpese!=null){
                val amountStr=binding.edAmount.text.toString()
                val amount=amountStr.toDouble()
                val status=binding.spStatus.selectedItem.toString()
                val reason=binding.edReason.text.toString()
                val description=binding.edDescription.text.toString()
                val date=binding.edSetDate.text.toString()
                val selectedStatusPosition = binding.spStatus.selectedItemPosition

                if (!Validation.checkNull(amountStr, binding.edAmount)
                    || !Validation.checkNull(reason,binding.edReason)
                    || !Validation.checkNull(date,binding.edSetDate)  ){
                }
                else if (selectedStatusPosition == 0){
                    Toast.makeText(this@AddIcomeAndExpese, "Select a Valid Status", Toast.LENGTH_LONG).show()
                }
                else {
                    val expenseEntity= IncomeExpenseEntity(incomeExpese.id ,amount = amount, status = status, reason = reason, description = description, date = date  )
                    expenseViewModel.updateExpense(expenseEntity)
                    Toast.makeText(
                        this@AddIcomeAndExpese, "${expenseEntity.status} Updated Successfully", Toast.LENGTH_LONG).show()
                    val intent=Intent(this@AddIcomeAndExpese,DashBoardActivity::class.java)
                    startActivity(intent)
                }



            }
            else{
                val amountStr=binding.edAmount.text.toString()
                val amount=amountStr.toDouble()
                val status=binding.spStatus.selectedItem.toString()
                val reason=binding.edReason.text.toString()
                val description=binding.edDescription.text.toString()
                val date=binding.edSetDate.text.toString()
                val selectedStatusPosition = binding.spStatus.selectedItemPosition

                if (!Validation.checkNull(amountStr, binding.edAmount)
                    || !Validation.checkNull(reason,binding.edReason)
                    || !Validation.checkNull(date,binding.edSetDate)  ){
                }
                else if (selectedStatusPosition == 0){
                    Toast.makeText(this@AddIcomeAndExpese, "Select a Valid Status", Toast.LENGTH_LONG).show()
                }
                else {
                    val expenseEntity= IncomeExpenseEntity(amount = amount, status = status, reason = reason, description = description, date = date  )
                    expenseViewModel.addExpenses(expenseEntity)
                    Toast.makeText(
                        this@AddIcomeAndExpese, "${expenseEntity.status} Saved Successfully", Toast.LENGTH_LONG).show()
                    val intent=Intent(this@AddIcomeAndExpese,DashBoardActivity::class.java)
                    startActivity(intent)
                }



            }

        }

    }
}