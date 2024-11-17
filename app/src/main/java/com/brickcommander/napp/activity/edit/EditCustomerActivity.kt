package com.brickcommander.napp.activity.edit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.brickcommander.napp.R
import com.brickcommander.napp.data.CONSTANTS
import com.brickcommander.napp.data.Data
import com.brickcommander.napp.logic.Calculate
import com.brickcommander.napp.model.Customer
import com.brickcommander.napp.model.Item
import com.brickcommander.napp.utils.Utility

class EditCustomerActivity : AppCompatActivity() {
    companion object {
        const val TAG = "EditCustomerActivity"
    }

    private lateinit var nameEditText: EditText
    private lateinit var mobileEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var customerNameSpinner: Spinner
    private var customer: Customer? = null
    private var customerIdx: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_customer_activity)

        Log.d(TAG, "onCreate called")

        nameEditText = findViewById(R.id.nameEditText)
        mobileEditText = findViewById(R.id.mobileEditText)
        emailEditText = findViewById(R.id.emailEditText)
        addressEditText = findViewById(R.id.addressEditText)
        saveButton = findViewById(R.id.saveButton)
        customerNameSpinner = findViewById(R.id.customerNameSpinner)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, CONSTANTS.NAME.toTypedArray())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        customerNameSpinner.adapter = adapter

        // Retrieve the work item from the intent
        customerIdx = intent.getIntExtra("customer_idx", -1)
        if(customerIdx == -1) {
            customer = Customer()
        } else if(customerIdx >= Data.customerList.size) {
            finish()
        } else {
            customer = Data.customerList[customerIdx]

            // Populate the fields with existing data
            customer?.let {
                nameEditText.setText(it.getName())
                mobileEditText.setText(it.getMobile())
                emailEditText.setText(it.getEmail())
                addressEditText.setText(it.getAddress())
                customerNameSpinner.setSelection(it.getCustomerNameQ())
            }
        }


        customerNameSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Handle item selection here
                val selectedItem = parent.getItemAtPosition(position).toString()
                Log.d(TAG, "customerNameSpinner Selected item: $selectedItem")
                customer?.setCustomerNameQ(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Log.d(TAG, "customerNameSpinner None Selected")
            }
        }

        // Save the changes when the button is clicked
        saveButton.setOnClickListener {
            if(Utility.isInternetAvailable(this)) {
                saveChanges()
                val resultIntent = Intent()
                setResult(Activity.RESULT_OK, resultIntent)
                finish() // Close the activity after saving
            } else {
                Toast.makeText(applicationContext, "Please Connect to Internet", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun revertChanges(oldCustomer: Customer) {
        customer?.apply {
            setName(oldCustomer.getName())
            setMobile(oldCustomer.getMobile())
            setEmail(oldCustomer.getEmail())
            setAddress(oldCustomer.getAddress())
            setCustomerNameQ(oldCustomer.getCustomerNameQ())
        }
    }

    private fun saveChanges() {
        // Update the workItem with new values
        val oldCustomer: Customer? = customer?.copy()
        customer?.apply {
            setName(nameEditText.text.toString())
            setMobile(mobileEditText.text.toString())
            setEmail(emailEditText.text.toString())
            setAddress(addressEditText.text.toString())
        }

        if(customer?.getName()?.isEmpty() == true) {
            Toast.makeText(applicationContext, "Customer Name cannot be empty", Toast.LENGTH_SHORT).show()
            if (oldCustomer != null) {
                revertChanges(oldCustomer)
            }
        } else {
            val calculate = Calculate()
            if(calculate.updateCustomer(customer!!, customerIdx)) {
                Toast.makeText(applicationContext, "Saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "Customer Already Exists", Toast.LENGTH_SHORT).show()
                if (oldCustomer != null) {
                    revertChanges(oldCustomer)
                }
            }
        }
    }
}
