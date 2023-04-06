package com.mh.circutversionupdate.activity

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.mh.circutversionupdate.adapter.CategoryAdapter
import com.mh.circutversionupdate.adapter.ItemAdapter
import com.mh.circutversionupdate.modal.CategoryItem
import com.mh.circutversionupdate.modal.Item
import com.mh.circutversionupdate.utils.AppStorage
import com.mh.circutversionupdate.databinding.ActivityHomeBinding
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import com.mh.circutversionupdate.R

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var categoryItemList: ArrayList<CategoryItem>
    private lateinit var database: DatabaseReference
    private lateinit var itemList: ArrayList<Item>
    private lateinit var adapter: ItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullscreen()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        innit()
        clickListener()
    }

    private fun clickListener() {
        binding.addbtn.setOnClickListener {
            startActivity(Intent(this, AddItemActivity::class.java))
        }
        binding.btnLogout.setOnClickListener {
            AppStorage.init(this)
            AppStorage.setUser(false)
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun fullscreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    private fun innit() {
        progressDialog = ProgressDialog(this, R.style.AppCompatAlertDialogStyle)
        progressDialog.setMessage("Please Wait")
        categoryItemList = ArrayList()
        categoryItemList.add(CategoryItem("1", "Rams", R.drawable.ram))
        categoryItemList.add(CategoryItem("2", "SSD", R.drawable.ssd))
        categoryItemList.add(CategoryItem("3", "CPU", R.drawable.c))
        categoryItemList.add(CategoryItem("4", "GPU", R.drawable.g))
        categoryItemList.add(CategoryItem("5", "Hard Disks", R.drawable.hard))
        categoryItemList.add(CategoryItem("6", "Batteries", R.drawable.battery))
        categoryItemList.add(CategoryItem("7", "Colling", R.drawable.cooling))
        categoryItemList.add(CategoryItem("8", "Cables", R.drawable.cable))

        binding.categoryRecyclarview.layoutManager = GridLayoutManager(this, 4)
        binding.categoryRecyclarview.adapter =
            CategoryAdapter(categoryItemList, this, ::moveToNextActvity)
        progressDialog.show()

        itemList = ArrayList()

        enableLeftRightSwipe()

    }

    override fun onResume() {
        database = FirebaseDatabase.getInstance().getReference("yourProduct")
        progressDialog.dismiss()
        val childListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val contact = snapshot.getValue(Item::class.java);
                progressDialog.show()
                if (snapshot.hasChildren()) {

                    Log.d("auiod_______", "snapshot.hasChildren()")

                    if (contact != null) {
                        Log.d("auiod_______", "snapshot.contact()")
                        progressDialog.dismiss()
                        binding.noItemFound.visibility = View.GONE

                        itemList.add(contact)
                        adapter = ItemAdapter(
                            itemList,
                            this@HomeActivity,
                            ::editItem,
                            ::shareItem,
                            ::locationItem
                        )
                        binding.checkListRecyclarview.layoutManager =
                            GridLayoutManager(this@HomeActivity, 2)

                        binding.checkListRecyclarview.adapter = adapter

                    } else {
                        progressDialog.dismiss()
                        binding.noItemFound.visibility = View.VISIBLE

                    }
                } else {
                    progressDialog.dismiss()
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
        database.child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addChildEventListener(childListener)

        super.onResume()
    }

    private fun editItem(position: Int) {
        val intent = Intent(this, ItemDescriptionActivity::class.java)
        intent.putExtra("data", itemList[position])
        intent.putExtra("inputType", 0)
        startActivity(intent)
    }

    private fun moveToNextActvity(position: Int) {
        var intent = Intent(this, ItemDetailByItsNameActivty::class.java)
        intent.putExtra("position", position)
        startActivity(intent)
    }

    private fun shareItem(position: Int) {
        if (itemList[position].itemName.toString() != "") {
            val sendIntent = Intent(Intent.ACTION_VIEW)
            sendIntent.data = Uri.parse("sms:")
            sendIntent.putExtra("sms_body","Item Name: "+ itemList[position].itemName.toString()+System.getProperty("line.separator")
            +"Item Price "+itemList[position].itemPrice.toString()+System.getProperty("line.separator")+"Item Quantity "+itemList[position].quantityPurchasedByuser)
            startActivity(sendIntent);
        }

    }

    private fun locationItem(position: Int) {

        if (itemList[position].itemLongitude.toFloat() != null && itemList[position].itemLongitude.toFloat() != null) {
            val strUri =
                "http://maps.google.com/maps?q=loc:" + itemList[position].itemLongitude + "," + itemList[position].itemLongitude.toString() + " (" + itemList[position].itemLocation + ")"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
            intent.setClassName(
                "com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity"
            )
            startActivity(intent)

        }

    }

    private fun enableLeftRightSwipe() {

        val callback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                try {
                    val position = viewHolder.adapterPosition
                    if (direction == ItemTouchHelper.LEFT) {
                        database.child(FirebaseAuth.getInstance().currentUser!!.uid)
                            .child(itemList[position].itemID).removeValue()
                        itemList.removeAt(position)
                        adapter.notifyItemRemoved(position)

                        if (itemList.size == 0) {
                            binding.noItemFound.visibility = View.VISIBLE
                        }
                    } else {
                        val item = Item(
                            itemList[position].itemID,
                            itemList[position].itemName,
                            itemList[position].itemPrice,
                            itemList[position].itemQuantity,
                            itemList[position].itemDescription,
                            itemList[position].itemLocation,
                            itemList[position].itemType,
                            itemList[position].itemLatitude,
                            itemList[position].itemLongitude,
                            itemList[position].itemImage,
                            true,
                            itemList[position].quantityPurchasedByuser
                        )
                        database.child(FirebaseAuth.getInstance().currentUser!!.uid)
                            .child(itemList[position].itemID)
                            .setValue(item).addOnSuccessListener {
                                Toast.makeText(
                                    applicationContext,
                                    "Item Purchased",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        adapter.notifyDataSetChanged()
                    }

                } catch (e: Exception) {
                    Log.e("MainActivity", e.message!!)
                }
            }

            // You must use @RecyclerViewSwipeDecorator inside the onChildDraw method
            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addSwipeLeftBackgroundColor(
                        ContextCompat.getColor(
                            this@HomeActivity,
                            R.color.red
                        )
                    )
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_add_shopping_cart_24)
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                    .addSwipeRightBackgroundColor(
                        ContextCompat.getColor(
                            this@HomeActivity,
                            R.color.green
                        )
                    )
                    .addSwipeRightLabel("Purchase")
                    .setSwipeRightLabelColor(Color.WHITE)
                    .addSwipeLeftLabel("Delete")
                    .setSwipeLeftLabelColor(Color.WHITE) //.addCornerRadius(TypedValue.COMPLEX_UNIT_DIP, 16)
                    //.addPadding(TypedValue.COMPLEX_UNIT_DIP, 8, 16, 8)
                    .create()
                    .decorate()
                super.onChildDraw(
                    c, recyclerView!!,
                    viewHolder!!, dX, dY, actionState, isCurrentlyActive
                )
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.checkListRecyclarview)


    }

    override fun onStart() {
        super.onStart()
        if (itemList.isNotEmpty())
        {
            itemList.clear()
        }
    }
}