package com.mh.circutversionupdate.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.mh.circutversionupdate.adapter.AddItemAdapter
import com.mh.circutversionupdate.databinding.ActivityItemDetailByItsNameActivtyBinding
import com.mh.circutversionupdate.modal.Item
import com.mh.circutversionupdate.R


class ItemDetailByItsNameActivty : AppCompatActivity() {
    private lateinit var binding: ActivityItemDetailByItsNameActivtyBinding
    private lateinit var database: DatabaseReference
    private lateinit var itemList: ArrayList<Item>
    private lateinit var adapter: AddItemAdapter
    private lateinit var progressDialog: ProgressDialog
    private var position: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullscreen()
        binding = ActivityItemDetailByItsNameActivtyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        innit()
        clickListener()
    }

    private fun clickListener() {
        binding.backArrow.setOnClickListener {
            finish()
        }
        binding.searchItem.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchItem(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun innit() {
        progressDialog = ProgressDialog(this, R.style.AppCompatAlertDialogStyle)
        progressDialog.setMessage("Please Wait")

        if (intent != null) {
            position = intent.getIntExtra("position", -1)
            position += 1
            if (position == 1) {
                binding.itemName.text = "RAMs"
            } else if (position == 2) {
                binding.itemName.text = "SSD"
            } else if (position == 3) {
                binding.itemName.text = "CPU"
            } else if (position == 4) {
                binding.itemName.text = "GPU"
            } else if (position == 5) {
                binding.itemName.text = "Hard Disks"
            } else if (position == 6) {
                binding.itemName.text = "Batteries"
            } else if (position == 7) {
                binding.itemName.text = "Coolings"
            } else if (position == 8) {
                binding.itemName.text = "Cables"
            }
            progressDialog.show()
            itemList = ArrayList()
            database = FirebaseDatabase.getInstance().getReference("Item")
            val childListener = object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    progressDialog.dismiss()
                    val contact = snapshot.getValue(Item::class.java);

                    if (snapshot.hasChildren()) {

                        binding.noItemFound.visibility = View.GONE

                        if (contact != null) {
                            itemList.add(contact)
                        } else {
                            progressDialog.dismiss()
                        }
                        adapter = AddItemAdapter(
                            itemList,
                            this@ItemDetailByItsNameActivty,
                            ::addItemToDetail

                        )
                        binding.recyclarviewItem.layoutManager =
                            LinearLayoutManager(
                                this@ItemDetailByItsNameActivty,
                                RecyclerView.VERTICAL,
                                false
                            )
                        binding.recyclarviewItem.adapter = adapter

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
                    binding.noItemFound.visibility = View.VISIBLE
                    progressDialog.dismiss()
                }

            }
            if (itemList.isEmpty())
            {
                progressDialog.dismiss()
            }
            database.child(position.toString()).addChildEventListener(childListener);



        }

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
                        searchItem, this@ItemDetailByItsNameActivty,::addItemToDetail
                    )
                    binding.recyclarviewItem.layoutManager =
                        LinearLayoutManager(
                            this@ItemDetailByItsNameActivty,
                            RecyclerView.VERTICAL,
                            false
                        )
                    binding.recyclarviewItem.adapter = adapter


                }
                else
                {
                    val adapter = AddItemAdapter(
                        searchItem, this@ItemDetailByItsNameActivty,::addItemToDetail
                    )
                    binding.recyclarviewItem.layoutManager =
                        LinearLayoutManager(
                            this@ItemDetailByItsNameActivty,
                            RecyclerView.VERTICAL,
                            false
                        )
                    binding.recyclarviewItem.adapter = adapter
                    binding.noItemFound.visibility = View.VISIBLE
                }
            }
        }

    }

    private fun addItemToDetail(pos: Int) {


    }

    private fun fullscreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }
}