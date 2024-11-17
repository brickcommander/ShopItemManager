package com.brickcommander.napp.data

import com.brickcommander.napp.model.Customer
import com.brickcommander.napp.model.Item
import com.brickcommander.napp.model.Profile

object Data {
    var itemList: MutableList<Item> = mutableListOf()
    var customerList: MutableList<Customer> = mutableListOf()
    var profile: Profile = Profile()
}