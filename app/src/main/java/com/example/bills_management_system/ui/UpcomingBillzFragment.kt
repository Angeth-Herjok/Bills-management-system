package com.example.bills_management_system.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bills_management_system.databinding.FragmentUpcomingBillzBinding
import com.example.bills_management_system.model.UpcomingBill
import com.example.bills_management_system.utils.Constants
import com.example.bills_management_system.viewmodel.BillsViewModel



class UpcomingBillzFragment : Fragment() , OnClickBill {

    var binding : FragmentUpcomingBillzBinding? = null
    val billViewModel: BillsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpcomingBillzBinding.inflate(inflater, container, false)
        return binding?.root

    }

    override fun onResume() {
        super.onResume()
        getUpcomingBills()
        billViewModel.createRecurringBills()
    }
    fun getUpcomingBills(){
        billViewModel.getUpcomingBillsByFreq(Constants.WEEKLY)
            .observe(this){weeklyBills->
                val adapter = UpcomingBillsAdapter(weeklyBills, this)
                binding?.rvWeekly?.layoutManager = LinearLayoutManager(requireContext())
                binding?.rvWeekly?.adapter = adapter
            }

        billViewModel.getUpcomingBillsByFreq(Constants.MONTHLY)
            .observe(this){monthlyBills->
                val adapter = UpcomingBillsAdapter(monthlyBills, this)
                binding?.rvMonthly?.layoutManager = LinearLayoutManager(requireContext())
                binding?.rvMonthly?.adapter = adapter
            }

        billViewModel.getUpcomingBillsByFreq(Constants.ANNUAL)
            .observe(this){annualBills->
                val adapter = UpcomingBillsAdapter(annualBills, this)
                binding?.rvAnnually?.layoutManager = LinearLayoutManager(requireContext())
                binding?.rvAnnually?.adapter = adapter
            }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun checkPaidBill(upcomingBill: UpcomingBill) {
        upcomingBill.paid = true
        billViewModel.updateUpcomingBill(upcomingBill)

    }


}

