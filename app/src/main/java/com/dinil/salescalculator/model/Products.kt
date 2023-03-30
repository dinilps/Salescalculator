package com.dinil.salescalculator.model

class Products : ArrayList<Products.ProductsItem>(){
    data class ProductsItem(
        var price: Double?,
        var sku: Int?
    )
}