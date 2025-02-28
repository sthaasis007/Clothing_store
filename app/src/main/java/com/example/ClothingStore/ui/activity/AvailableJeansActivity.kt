package com.example.ClothingStore.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ClothingStore.R

class AvailableJeansActivity : AppCompatActivity() {

    private lateinit var productsContainer: LinearLayout
    private lateinit var jeansNameEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var sizeEditText: EditText
    private lateinit var colorEditText: EditText
    private lateinit var addProductButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_available_jeans)

        // Find Views
        jeansNameEditText = findViewById(R.id.jeansNameEditText)
        priceEditText = findViewById(R.id.priceEditText)
        sizeEditText = findViewById(R.id.sizeEditText)
        colorEditText = findViewById(R.id.colorEditText)
        addProductButton = findViewById(R.id.addProductButton)
        productsContainer = findViewById(R.id.productsContainer)

        // Add Product Button Click
        addProductButton.setOnClickListener { addProduct() }
    }

    private fun addProduct() {
        // Get input values
        val name = jeansNameEditText.text.toString().trim()
        val price = priceEditText.text.toString().trim()
        val size = sizeEditText.text.toString().trim()
        val color = colorEditText.text.toString().trim()

        if (name.isEmpty() || price.isEmpty() || size.isEmpty() || color.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create Product Layout
        val productLayout = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
            setPadding(10, 10, 10, 10)
        }

        // Product Details TextView
        val productTextView = TextView(this).apply {
            text = "Name: $name\nPrice: $$price\nSize: $size\nColor: $color"
            textSize = 16f
        }

        // Update Button
        val updateButton = Button(this).apply {
            text = "Update"
            setOnClickListener {
                priceEditText.setText(price)
                Toast.makeText(this@AvailableJeansActivity, "Modify price & click Add", Toast.LENGTH_SHORT).show()
            }
        }

        // Delete Button
        val deleteButton = Button(this).apply {
            text = "Delete"
            setOnClickListener {
                productsContainer.removeView(productLayout)
                Toast.makeText(this@AvailableJeansActivity, "Product Deleted", Toast.LENGTH_SHORT).show()
            }
        }

        // Add Views to Product Layout
        productLayout.addView(productTextView)
        productLayout.addView(updateButton)
        productLayout.addView(deleteButton)

        // Add Product Layout to Container
        productsContainer.addView(productLayout)

        // Clear Input Fields
        jeansNameEditText.text.clear()
        priceEditText.text.clear()
        sizeEditText.text.clear()
        colorEditText.text.clear()

        Toast.makeText(this, "Product Added!", Toast.LENGTH_SHORT).show()
    }
}
