package com.brickcommander.napp.model

import java.time.LocalDate

class Profile() {

    private val shopId: String = "S"+java.util.UUID.randomUUID().toString()
    private var shopName: String = ""
    private var owner: String = ""
    private var mobile: String = ""
    private var email: String = ""
    private var location: String = ""
    private var gstin: String = ""
    private val createdDate: LocalDate = LocalDate.now()

    fun getShopId(): String = shopId

    fun getShopName(): String = shopName
    fun setShopName(value: String) {
        shopName = value
    }

    fun getOwner(): String = owner
    fun setOwner(value: String) {
        owner = value
    }

    fun getMobile(): String = mobile
    fun setMobile(value: String) {
        mobile = value
    }

    fun getEmail(): String = email
    fun setEmail(value: String) {
        email = value
    }

    fun getGstin(): String = gstin
    fun setGstin(value: String) {
        gstin = value
    }

    fun getLocation(): String = location
    fun setLocation(value: String) {
        location = value
    }

    fun copy(): Profile {
        val newProfile = Profile()
        newProfile.shopName = shopName
        newProfile.owner = owner
        newProfile.mobile = mobile
        newProfile.email = email
        newProfile.gstin = gstin
        newProfile.location = location
        return newProfile
    }

    override fun toString(): String {
        return "Profile(shopName=$shopName, owner=$owner, mobile=$mobile, email=$email, gstin=$gstin, location=$location)"
    }
}
