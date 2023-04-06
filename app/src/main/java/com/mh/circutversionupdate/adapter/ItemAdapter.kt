package com.mh.circutversionupdate.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mh.circutversionupdate.databinding.CustomCheckListBinding
import com.mh.circutversionupdate.modal.Item

class ItemAdapter(
    private val list: ArrayList<Item>,
    val context: Context,
    val addItem: (Int) -> Unit,
    val shareItem: (Int) -> Unit,
    val location: (Int) -> Unit,
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CustomCheckListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CustomCheckListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder)
        {
            with(list[position])
            {
                binding.itemName.text = this.itemName
                binding.itemPrice.text ="$"+this.itemPrice
                binding.quantity.text =this.quantityPurchasedByuser+" item"

                binding.share.setOnClickListener {
                    addItem(position)
                }
                binding.edit.setOnClickListener {
                    shareItem(position)
                }
                binding.location.setOnClickListener {
                    location(position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}