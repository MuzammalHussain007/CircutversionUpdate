package com.mh.circutversionupdate.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.mh.circutversionupdate.adapter.AddItemAdapter
import com.mh.circutversionupdate.modal.Item
import com.mh.circutversionupdate.R
import com.mh.circutversionupdate.databinding.ActivityAddItemBinding

class AddItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddItemBinding
    private lateinit var database: DatabaseReference
    private lateinit var itemList: ArrayList<Item>
    private  var itemType : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        innit()
        binding.backArrow.setOnClickListener {
            finish()
        }


    }

    private fun innit() {
        itemList = ArrayList()
        database = FirebaseDatabase.getInstance().getReference("Item")

        binding.searchItem.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchItem(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        val arraySpinner = arrayOf(
            "Category",
            "Rams",
            "SSD",
            "CPU",
            "GPU",
            "Hard Disks",
            "Batteries",
            "Cooling",
            "Cables"
        )
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item, arraySpinner
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGender.adapter = adapter

        binding.spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                (view as TextView).setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_baseline_arrow_drop_down_24,
                    0
                );

                when (position) {
                    1 -> {
                        itemList.clear()
                        itemType = 1
                        showErrorIfListisEmpty()
                        setItemToRecyclarView()
                    }
                    2 -> {

                        itemList.clear()
                        itemType = 2
                        showErrorIfListisEmpty()
                        setItemToRecyclarView()
                    }
                    3 -> {

                        itemList.clear()
                        itemType = 3
                        showErrorIfListisEmpty()
                        setItemToRecyclarView()
                    }
                    4 -> {

                        itemList.clear()
                        itemType = 4
                        showErrorIfListisEmpty()
                        setItemToRecyclarView()
                    }
                    5 -> {

                        itemList.clear()
                        itemType = 5
                        showErrorIfListisEmpty()
                        setItemToRecyclarView()
                    }
                    6 -> {

                        itemList.clear()
                        itemType = 6
                        showErrorIfListisEmpty()
                        setItemToRecyclarView()
                    }
                    7 -> {

                        itemList.clear()
                        itemType = 7
                        showErrorIfListisEmpty()
                        setItemToRecyclarView()
                    }
                    8->{

                        itemList.clear()
                        itemType = 8
                        showErrorIfListisEmpty()
                        setItemToRecyclarView()
                    }
                }



            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // another interface callback
            }
        }

    }

    private fun showErrorIfListisEmpty(){
        val adapter = AddItemAdapter(
            itemList, this@AddItemActivity, ::addItemToDetail
        )
        binding.recyclarviewItem.layoutManager =
            LinearLayoutManager(
                this@AddItemActivity,
                RecyclerView.VERTICAL,
                false
            )
        binding.recyclarviewItem.adapter = adapter
        binding.noItemFound.visibility = View.VISIBLE
    }

    private fun searchItem(itemName: String) {
        val searchItem: ArrayList<Item> = ArrayList()
        if(!itemList.isEmpty())
        {
            for(i in 0 until itemList.size)
            {
                if(itemList[i].itemName.toLowerCase().contains(itemName.toLowerCase().toString()))
                {
                    Log.d("sadss__","sadasa")
                    searchItem.add(itemList[i])
                    val adapter = AddItemAdapter(
                        searchItem, this@AddItemActivity,::addItemToDetail
                    )
                    binding.recyclarviewItem.layoutManager =
                        LinearLayoutManager(
                            this@AddItemActivity,
                            RecyclerView.VERTICAL,
                            false
                        )
                    binding.recyclarviewItem.adapter = adapter


                }
                else
                {
                    val adapter = AddItemAdapter(
                        searchItem, this@AddItemActivity,::addItemToDetail
                    )
                    binding.recyclarviewItem.layoutManager =
                        LinearLayoutManager(
                            this@AddItemActivity,
                            RecyclerView.VERTICAL,
                            false
                        )
                    binding.recyclarviewItem.adapter = adapter
                    binding.noItemFound.visibility = View.VISIBLE
                }
            }
        }

    }


    private fun setItemToRecyclarView() {
        val childListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val contact = snapshot.getValue(Item::class.java);
                if (snapshot.hasChildren()) {
                    Log.d("auiod_______", "snapshot.hasChildren()")

                    if (contact != null) {
                        Log.d("auiod_______", "snapshot.contact()")
                        binding.noItemFound.visibility = View.GONE

                        if (contact!=null) {

                            itemList.add(contact)
                            val adapter = AddItemAdapter(
                                itemList, this@AddItemActivity, ::addItemToDetail
                            )
                            binding.recyclarviewItem.layoutManager =
                                LinearLayoutManager(
                                    this@AddItemActivity,
                                    RecyclerView.VERTICAL,
                                    false
                                )
                            binding.recyclarviewItem.adapter = adapter
                        }


                    } else {
                        binding.noItemFound.visibility = View.VISIBLE

                    }
                } else {
                    binding.noItemFound.visibility = View.VISIBLE
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        }

        database.child(itemType.toString()).addChildEventListener(childListener);
    }



    private fun addItemToDetail(position : Int){

        val intent = Intent(this , ItemDescriptionActivity::class.java)
        intent.putExtra("data",itemList[position])
        intent.putExtra("inputType",1)
        startActivity(intent)

    }
}