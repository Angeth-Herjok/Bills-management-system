package com.example.bills_management_system.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.example.bills_management_system.model.Bill
import com.example.bills_management_system.viewmodel.BillzViewModel
import com.example.bills_management_system.R
import com.example.bills_management_system.databinding.ActivityAddBillsBinding
import java.util.UUID

class AddBillsActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBillsBinding
    val billViewModel: BillzViewModel by viewModels()
    var selectedDate = 0
    var selectedMonth = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBillsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFreqSpinner()
        setDueDateSpinner()

        binding.btnSaveBill.setOnClickListener {
            val selectedFrequency = binding.spFrequency.selectedItem.toString()
            val billName = binding.etName.text.toString()
            val billAmount = binding.etAmount.text.toString().toDouble()
            val selectedDueDate: String = when (selectedFrequency) {
                "Annual" -> {
                    val datePicker = binding.dpDueDate
                    "${datePicker.year}-${datePicker.month + 1}-${datePicker.dayOfMonth}"
                }
                else -> binding.spFrequency.selectedItem.toString()
//                DateTimeUtils.createDateFromDayAndMonth(selectedFrequency,setDueDateSpinner).substring(5)

            }

            val bill = Bill(
                billId = UUID.randomUUID().toString(),
                name = billName,
                amount = billAmount,
                frequency = selectedFrequency,
                dueDate = selectedDueDate,
                userId = "USER_ID",
                synced = false
            )
            billViewModel.saveBill(bill)
            finish()
            navigateToSummaryFragment()
        }
    }
    fun setupFreqSpinner(){
        val adapter = ArrayAdapter.createFromResource(this, R.array.frequencies,android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spFrequency.adapter = adapter
    }
    fun View.show(){
        this.visibility = View.VISIBLE
    }
    fun View.hide(){
        this.visibility = View.GONE
    }
    private fun setDueDateSpinner() {
        binding.spFrequency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedFrequency = binding.spFrequency.selectedItem.toString()

                val dueDateAdapter = when (binding.spFrequency.selectedItem.toString()) {
                    "Weekly" -> {
                        val daysInWeek = 1..7
                        ArrayAdapter(
                            this@AddBillsActivity,
                            android.R.layout.simple_spinner_item,
                            daysInWeek.toList()
                        )
                    }

                    "Monthly" -> {
                        val daysInMonth = 1..31
                        ArrayAdapter(
                            this@AddBillsActivity,
                            android.R.layout.simple_spinner_item,
                            daysInMonth.toList()
                        )
                    }

                    "Quarterly" -> {
                        val daysInQuarter = 1..90
                        ArrayAdapter(
                            this@AddBillsActivity,
                            android.R.layout.simple_spinner_item,
                            daysInQuarter.toList()
                        )
                    }

                    "Annual" -> {
                        binding.spFrequency.visibility = View.GONE
                        binding.dpDueDate.visibility = View.VISIBLE
                        val daysInYear = 1..365
                        ArrayAdapter(
                            this@AddBillsActivity,
                            android.R.layout.simple_spinner_item,
                        )

                    }

                    else -> {
                        ArrayAdapter(
                            this@AddBillsActivity,
                            android.R.layout.simple_spinner_item,
                            arrayOf(1, 2, 3, 4, 5, 6, 7)
                        )

                    }
                }
                dueDateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spFrequency.adapter = dueDateAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    fun navigateToSummaryFragment(){
        val fragment = SummaryFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(androidx.fragment.R.id.fragment_container_view_tag,fragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }

}
