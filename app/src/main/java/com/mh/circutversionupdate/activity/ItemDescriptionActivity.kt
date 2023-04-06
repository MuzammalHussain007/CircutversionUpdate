package com.mh.circutversionupdate.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mh.circutversionupdate.databinding.ActivityItemDescriptionBinding
import com.mh.circutversionupdate.modal.Item

class ItemDescriptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemDescriptionBinding
    private var quantity: Int = 0
    private var newQuantity: Int = 0
    private var item: Item? = null
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemDescriptionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.backArrow.setOnClickListener {
            finish()
        }
        binding.btnPositive.setOnClickListener {
            quantity = binding.itemCount.text.toString().toInt()
            quantity++
            binding.itemCount.text = quantity.toString()
        }
        binding.btnNegative.setOnClickListener {
            quantity = binding.itemCount.text.toString().toInt()
            if (quantity > 0) {
                quantity--
                binding.itemCount.text = quantity.toString()
            }
        }

        innit()
    }

    private fun innit() {
        if (intent!=null)
        {
            item = intent.getSerializableExtra("data") as? Item
        }
        database = FirebaseDatabase.getInstance().getReference("yourProduct")

        if (item!!.itemImage.isNotEmpty())
        {
            Glide.with(this).load(item!!.itemImage).into(binding.mainImageView)
            binding.imageLayout.visibility = View.GONE

        }
        binding.itemNameDescription.text = item!!.itemName.toString()
        binding.itemDesciprionPrice.text = "$"+item!!.itemPrice.toString()
        binding.itemDescription.text = item!!.itemDescription.toString()
        binding.itemLocation.text = item!!.itemLocation.toString()
        binding.itemCount.text = item!!.itemQuantity.toString()
        binding.itemCount.text = "0"
        binding.pinLocation.setOnClickListener {
            Toast.makeText(applicationContext, "Your Location Pinned", Toast.LENGTH_SHORT).show()
        }

        val type = intent.getIntExtra("inputType", -1)

        if (type == 1) {
            binding.toolbarText.text = "Add items"
            binding.btnEditItem.visibility = View.GONE
            binding.btnAddItem.visibility = View.VISIBLE
        } else if (type == 0) {
            binding.toolbarText.text = "Edit items"
            binding.btnEditItem.visibility = View.VISIBLE
            binding.btnAddItem.visibility = View.GONE
        }

        binding.btnAddItem.setOnClickListener {
            val key = database.push().key
            val user = FirebaseAuth.getInstance().currentUser!!.uid
            newQuantity = quantity


            val item = Item(
                key!!,
                item!!.itemName,
                item!!.itemPrice,
                item!!.itemQuantity,
                item!!.itemDescription,
                item!!.itemLocation,
                item!!.itemType,
                item!!.itemLatitude,
                item!!.itemLongitude,
                item!!.itemImage,
                false,
                newQuantity.toString()
            )
            database.child(user.toString()).child(key).setValue(item).addOnSuccessListener {
                Toast.makeText(applicationContext, "Item Added", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        binding.btnEditItem.setOnClickListener {
            newQuantity = quantity
            val item = Item(
                item!!.itemID,
                item!!.itemName,
                item!!.itemPrice,
                item!!.itemQuantity,
                item!!.itemDescription,
                item!!.itemLocation,
                item!!.itemType,
                item!!.itemLatitude,
                item!!.itemLongitude,
                item!!.itemImage,
                false,
                newQuantity.toString()
            )
            database.child(FirebaseAuth.getInstance().currentUser!!.uid).child(item!!.itemID)
                .setValue(item).addOnSuccessListener {
                    Toast.makeText(applicationContext, "Item Updated", Toast.LENGTH_SHORT).show()
                    finish()

                }
        }


    }

}