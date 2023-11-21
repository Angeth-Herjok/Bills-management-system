package com.example.bills_management_system.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.graphics.vector.SolidFill
import com.example.bills_management_system.databinding.FragmentSummaryBinding
import com.example.bills_management_system.model.BillsSummary
import com.example.bills_management_system.utils.Utils
import com.example.bills_management_system.viewmodel.BillsViewModel


class SummaryFragment : Fragment() {
    var binding: FragmentSummaryBinding?=null
    val billsViewModel: BillsViewModel by viewModels()
        lateinit var summaryChartView: AnyChartView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSummaryBinding.inflate(layoutInflater,container,false)
        return binding?.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        binding?.fabAddBill?.setOnClickListener {
            startActivity(Intent(requireContext(), AddBillsActivity::class.java))
        }
        billsViewModel.getMonthlySummary()
        showMonthlySummary()
    }
    fun showMonthlySummary(){
        billsViewModel.summaryLiveData.observe(this){summary->
            binding?.tvPaidAmt?.text= Utils.formatCurrency(summary.paid)
            binding?.tvOverdueAmt?.text= Utils.formatCurrency(summary.overdue)
            binding?.tvPendingAmt?.text= Utils.formatCurrency(summary.upcoming)
            binding?.tvTotalAmt?.text= Utils.formatCurrency(summary.total)
            ShowChart(summary)
        }
    }
    fun ShowChart(summary: BillsSummary){
        val entries = mutableListOf<DataEntry>()
        entries.add(ValueDataEntry("Paid",summary.paid))
        entries.add(ValueDataEntry("Upcoming",summary.upcoming))
        entries.add(ValueDataEntry("Overdue",summary.overdue))
        val pieChart = AnyChart.pie()
        pieChart.data(entries)
        pieChart.innerRadius(80)
        pieChart.palette().itemAt(0, SolidFill("#76ABDF",100))
        pieChart.palette().itemAt(1, SolidFill("#F89880",100))
        pieChart.palette().itemAt(2, SolidFill("#84AEAC",100))
        binding?.summaryChart?.setChart(pieChart)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding=null
    }
}




