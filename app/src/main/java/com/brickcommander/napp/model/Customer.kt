package com.brickcommander.napp.model

import java.time.LocalDate

class Customer() {

    private val customerId: String = "C"+java.util.UUID.randomUUID().toString()
    private var name: String = ""
    private var mobile: String = ""
    private var email: String = ""
    private var address: String = ""
    private var customerNameQ: Int = 0
    private val createdDate: LocalDate = LocalDate.now()

    fun getCustomerId(): String = customerId

    fun getName(): String = name
    fun setName(value: String) {
        name = value
    }

    fun getMobile(): String = mobile
    fun setMobile(value: String) {
        mobile = value
    }

    fun getEmail(): String = email
    fun setEmail(value: String) {
        email = value
    }

    fun getAddress(): String = address
    fun setAddress(value: String) {
        address = value
    }

    fun getCustomerNameQ(): Int = customerNameQ
    fun setCustomerNameQ(value: Int) {
        customerNameQ = value
    }

    fun copy(): Customer {
        val newItem = Customer()
        newItem.name = name
        newItem.mobile = mobile
        newItem.email = email
        newItem.address = address
        newItem.customerNameQ = customerNameQ
        return newItem
    }

    override fun toString(): String {
        return "Customer(customerId=$customerId, name=$name, mobile=$mobile, email=$email, address=$address, customerNameQ=$customerNameQ)"
    }
}
