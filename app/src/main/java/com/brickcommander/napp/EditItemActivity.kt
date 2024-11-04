package com.brickcommander.napp


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.brickcommander.napp.logic.Calculate
import com.brickcommander.napp.model.Item

class EditItemActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var buyEditText: EditText
    private lateinit var sellEditText: EditText
    private lateinit var totalEditText: EditText
    private lateinit var remainingEditText: EditText
    private lateinit var saveButton: Button
    private var item: Item? = null
    private var itemPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_item_activity)

        Log.d("EditItemActivity", "onCreate called")

        nameEditText = findViewById(R.id.nameEditText)
        buyEditText = findViewById(R.id.buyEditText)
        sellEditText = findViewById(R.id.sellEditText)
        totalEditText = findViewById(R.id.totalEditText)
        remainingEditText = findViewById(R.id.remainingEditText)
        saveButton = findViewById(R.id.saveButton)

        // Retrieve the work item from the intent
        itemPosition = intent.getIntExtra("work_item", -1)
        Log.d("EditItemActivity", "itemPosition: $itemPosition")
        if(itemPosition == -1) {
            item = Item()
        } else if(itemPosition >= Data.itemList.size) {
            Log.d("EditItemActivity", "Invalid item position: $itemPosition")
            finish()
        } else {
            item = Data.itemList[itemPosition]
        }

        // Populate the fields with existing data
        item?.let {
            nameEditText.setText(it.getName())
            buyEditText.setText(it.getBuyingPrice().toString())
            sellEditText.setText(it.getSellingPrice().toString())
            totalEditText.setText(it.getTotalCount().toString())
            remainingEditText.setText(it.getRemainingCount().toString())
        }

        // Save the changes when the button is clicked
        saveButton.setOnClickListener {
            saveChanges()
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish() // Close the activity after saving
        }
    }

    private fun saveChanges() {
        // Update the workItem with new values
        item?.apply {
            setName(nameEditText.text.toString())
            setBuyingPrice(buyEditText.text.toString().toIntOrNull() ?: 0)
            setSellingPrice(sellEditText.text.toString().toIntOrNull() ?: 0)
            setTotalCount(totalEditText.text.toString().toIntOrNull() ?: 0)
            setRemainingCount(remainingEditText.text.toString().toIntOrNull() ?: 0)
        }

        val calculate = Calculate()
        calculate.updateItem(item!!, itemPosition)
    }
}
