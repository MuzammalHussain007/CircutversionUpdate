package com.mh.circutversionupdate.modal

import java.io.Serializable


data class Item(
    val itemID: String = "",
    val itemName: String = "",
    val itemPrice: String = "",
    val itemQuantity: String = "",
    val itemDescription: String = "",
    val itemLocation: String = "",
    val itemType: String = "",
    val itemLatitude: String = "",
    val itemLongitude: String = "",
    val itemImage: String = "",
    val isPurchased: Boolean = false,
    val quantityPurchasedByuser: String = "-1"
) : Serializable
