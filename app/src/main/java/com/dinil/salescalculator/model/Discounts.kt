package com.dinil.salescalculator.model

class Discounts : ArrayList<Discounts.DiscountsItem>(){
    data class DiscountsItem(
        var key: String?,
        var value: Double?
    )
}