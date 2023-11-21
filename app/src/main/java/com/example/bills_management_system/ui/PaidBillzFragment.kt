package com.example.bills_management_system.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bills_management_system.databinding.FragmentPaidBillzBinding
import com.example.bills_management_system.model.UpcomingBill
import com.example.bills_management_system.viewmodel.BillzViewModel


class PaidBillsFragment : Fragment(),OnClickBill{
    var binding: FragmentPaidBillzBinding? = null
    val billsViewModel: BillzViewModel by  viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaidBillzBinding.inflate(layoutInflater,container,false)
        return binding?.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
    override fun onResume() {
        super.onResume()
        billsViewModel.getPaidBills().observe(this){paidBills->
            val adapter = UpcomingBillsAdapter(paidBills, this)
            binding?.rvpaid?.layoutManager = LinearLayoutManager(requireContext())
            binding?.rvpaid?.adapter = adapter
        }
    }
    override fun checkPaidBill(upcomingBill: UpcomingBill) {
        upcomingBill.paid = !upcomingBill.paid
        billsViewModel.updateUpcomingBill(upcomingBill)
    }
}


