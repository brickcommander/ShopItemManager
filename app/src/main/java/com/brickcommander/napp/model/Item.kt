package com.brickcommander.napp.model

import java.time.LocalDate

class Item() {

    private val id: String = java.util.UUID.randomUUID().toString()
    private var name: String = ""
    private var buyingPrice: Int = 0
    private var sellingPrice: Int = 0
    private var totalCount: Int = 0
    private var remainingCount: Int = 0
    private val createdDate: LocalDate = LocalDate.now()

    fun getId(): String = id

    fun getName(): String = name
    fun setName(value: String) {
        name = value
    }

    fun getBuyingPrice(): Int = buyingPrice
    fun setBuyingPrice(value: Int) {
        buyingPrice = value
    }

    fun getSellingPrice(): Int = sellingPrice
    fun setSellingPrice(value: Int) {
        sellingPrice = value
    }

    fun getTotalCount(): Int = totalCount
    fun setTotalCount(value: Int) {
        totalCount = value
    }

    fun getRemainingCount(): Int = remainingCount
    fun setRemainingCount(value: Int) {
        remainingCount = value
    }

    fun copy(): Item {
        val newItem = Item()
        newItem.name = name
        newItem.buyingPrice = buyingPrice
        newItem.sellingPrice = sellingPrice
        newItem.totalCount = totalCount
        newItem.remainingCount = remainingCount
        return newItem
    }

    override fun toString(): String {
        return "Item(id='$id', name='$name', buyingPrice=$buyingPrice, sellingPrice=$sellingPrice, totalCount=$totalCount, remainingCount=$remainingCount)"
    }
}
