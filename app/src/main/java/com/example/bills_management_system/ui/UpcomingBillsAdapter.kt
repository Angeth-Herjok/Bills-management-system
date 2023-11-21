package com.example.bills_management_system.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bills_management_system.databinding.UpcomingBillsListItemsBinding
import com.example.bills_management_system.model.UpcomingBill
import com.example.bills_management_system.utils.DateTimeUtils

class UpcomingBillsAdapter(var upcomingBills:List<UpcomingBill>, val onClickBill: OnClickBill):
    Adapter<UpcomingBillsViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingBillsViewHolder {
        val binding = UpcomingBillsListItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UpcomingBillsViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return upcomingBills.size
    }
    override fun onBindViewHolder(holder: UpcomingBillsViewHolder, position: Int) {
        val upcomingBill= upcomingBills.get(position)
        holder.binding.apply {
            cbUpcoming.isChecked=upcomingBill.paid
            cbUpcoming.text=upcomingBill.name
            tvAmount.text = upcomingBill.amount.toString()
            tvAmount.text = DateTimeUtils.formatCurrency(upcomingBill.amount)
            tvDueDate.text = DateTimeUtils.formatDateReadable(upcomingBill.dueDate)
        }
        holder.binding.cbUpcoming.setOnClickListener {
            onClickBill.checkPaidBill(upcomingBill)
        }
    }
}

class UpcomingBillsViewHolder(var binding: UpcomingBillsListItemsBinding) :ViewHolder(binding.root)

interface  OnClickBill{
    fun checkPaidBill(upcomingBill: UpcomingBill)
}