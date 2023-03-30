package com.dinil.salescalculator.model

class Orders : ArrayList<Orders.OrdersItem>(){
    data class OrdersItem(
        var discount: String?,
        var items: List<Item?>?,
        var orderId: Int?
    )

    data class Item(
        var quantity: Int?,
        var sku: Int?
    )
}

