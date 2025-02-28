package com.example.ClothingStore.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ClothingStore.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.UUID

class AvailableJacketActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var productsContainer: LinearLayout
    private lateinit var jacketNameEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var sizeEditText: EditText
    private lateinit var colorEditText: EditText
    private lateinit var addProductButton: Button
    private var updatingProductId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_available_jacket)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().getReference("products")

        // Find Views
        jacketNameEditText = findViewById(R.id.jacketNameEditText)
        priceEditText = findViewById(R.id.priceEditText)
        sizeEditText = findViewById(R.id.sizeEditText)
        colorEditText = findViewById(R.id.colorEditText)
        addProductButton = findViewById(R.id.addProductButton)
        productsContainer = findViewById(R.id.productsContainer)

        // Add Product Button Click
        addProductButton.setOnClickListener { addOrUpdateProduct() }
    }

    private fun addOrUpdateProduct() {
        val name = jacketNameEditText.text.toString().trim()
        val price = priceEditText.text.toString().trim()
        val size = sizeEditText.text.toString().trim()
        val color = colorEditText.text.toString().trim()

        if (name.isEmpty() || price.isEmpty() || size.isEmpty() || color.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val productId = updatingProductId ?: UUID.randomUUID().toString()
        val product = mapOf(
            "id" to productId,
            "name" to name,
            "price" to price,
            "size" to size,
            "color" to color
        )

        database.child(productId).setValue(product).addOnSuccessListener {
            if (updatingProductId == null) {
                displayProduct(productId, name, price, size, color)
            } else {
                updateDisplayedProduct(productId, name, price, size, color)
                updatingProductId = null
                addProductButton.text = "Add Product"
            }
            Toast.makeText(this, "Product Saved!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to save product", Toast.LENGTH_SHORT).show()
        }

        jacketNameEditText.text.clear()
        priceEditText.text.clear()
        sizeEditText.text.clear()
        colorEditText.text.clear()
    }

    private fun displayProduct(id: String, name: String, price: String, size: String, color: String) {
        val productLayout = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
            setPadding(10, 10, 10, 10)
            tag = id
        }

        val productTextView = TextView(this).apply {
            text = "Name: $name\nPrice: $$price\nSize: $size\nColor: $color"
            textSize = 16f
            tag = "text_$id"
        }

        val updateButton = Button(this).apply {
            text = "Update"
            setOnClickListener {
                jacketNameEditText.setText(name)
                priceEditText.setText(price)
                sizeEditText.setText(size)
                colorEditText.setText(color)
                updatingProductId = id
                addProductButton.text = "Update Product"
            }
        }

        val deleteButton = Button(this).apply {
            text = "Delete"
            setOnClickListener {
                database.child(id).removeValue().addOnSuccessListener {
                    productsContainer.removeView(productLayout)
                    Toast.makeText(this@AvailableJacketActivity, "Product Deleted", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this@AvailableJacketActivity, "Failed to delete product", Toast.LENGTH_SHORT).show()
                }
            }
        }

        productLayout.addView(productTextView)
        productLayout.addView(updateButton)
        productLayout.addView(deleteButton)

        productsContainer.addView(productLayout)
    }

    private fun updateDisplayedProduct(id: String, name: String, price: String, size: String, color: String) {
        val productLayout = productsContainer.findViewWithTag<LinearLayout>(id)
        val productTextView = productLayout?.findViewWithTag<TextView>("text_$id")
        productTextView?.text = "Name: $name\nPrice: $$price\nSize: $size\nColor: $color"
    }
}