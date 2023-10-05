package com.pm.expensemanager.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pm.expensemanager.AddIcomeAndExpese
import com.pm.expensemanager.databinding.AllIncomeExpenseListBinding
import com.pm.expensemanager.expensemodel.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class All_IncomeExpenseAdapter(
    private val context: Context,
    val noteClickDeleteInterface: NoteClickDeleteInterface,
) : RecyclerView.Adapter<All_IncomeExpenseAdapter.ViewHolder>() {

    private val allIncomeExpenseList = ArrayList<IncomeExpenseEntity>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): All_IncomeExpenseAdapter.ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = AllIncomeExpenseListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: All_IncomeExpenseAdapter.ViewHolder, position: Int) {
        val incomeExpenseList = allIncomeExpenseList

        // Make sure incomeExpenseList is not null before accessing elements
        incomeExpenseList?.let {
            val currentItem = it[position]
            holder.bind(currentItem)
        }

        holder.itemView.setOnLongClickListener {
            // on below line we are calling a note click
            // interface and we are passing a position to it.
            noteClickDeleteInterface.onDeleteIconClick(allIncomeExpenseList.get(position))
            true
        }

        // on below line we are adding click listener
        // to our recycler view item.
        holder.itemView.setOnClickListener {
            val intent= Intent(context,AddIcomeAndExpese::class.java)
            intent.putExtra("IncomeExpense",allIncomeExpenseList[position])
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return allIncomeExpenseList.size
    }

    inner class ViewHolder(private val binding: AllIncomeExpenseListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: IncomeExpenseEntity) {
            binding.incomeTextView.text = "Amount: ${item.amount.toString()}"
            binding.statusTextView.text = "Status: ${item.status}"
            binding.reasonTextView.text = "Reason: ${item.reason}"
            binding.descriptionTextView.text = "Description: ${item.description}"
            binding.dateTextView.text = "Date: ${item.date}"
        }
    }

    // below method is use to update our list of notes.
    fun updateList(newList: List<IncomeExpenseEntity>) {
        // on below line we are clearing
        // our notes array list
        allIncomeExpenseList.clear()
        // on below line we are adding a
        // new list to our all notes list.
        allIncomeExpenseList.addAll(newList)
        // on below line we are calling notify data
        // change method to notify our adapter.
        notifyDataSetChanged()
    }
}

interface NoteClickDeleteInterface {
    // creating a method for click
    // action on delete image view.
    fun onDeleteIconClick(incomeExpenseEntity: IncomeExpenseEntity)
}



