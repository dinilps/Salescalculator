package com.dinil.salescalculator

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dinil.salescalculator.databinding.ActivityMainBinding
import com.dinil.salescalculator.model.Discounts
import com.dinil.salescalculator.model.Orders
import com.dinil.salescalculator.model.Products
import com.dinil.salescalculator.model.Result
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var orderJsonString = """[{ "orderId": 1, "discount": "SALE10", "items": [{ "sku": 1001, "quantity": 3 }, { "sku": 1004, "quantity": 1 }] }, { "orderId": 2, "items": [{ "sku": 1003, "quantity": 1 }] }, { "orderId": 3, "discount": "SALE30", "items": [{ "sku": 1003, "quantity": 1 }, { "sku": 1001, "quantity": 4 }, { "sku": 1002, "quantity": 2 }] }, { "orderId": 4, "discount": "SALE10", "items": [{ "sku": 1001, "quantity": 7 }] }, { "orderId": 5, "discount": "SALE20", "items": [{ "sku": 1003, "quantity": 1 }] }]""";
    var productJsonString = """[{ "sku": 1001, "price": 14.99 }, { "sku": 1002, "price": 156.99 }, { "sku": 1003, "price": 1099.99 }, { "sku": 1004, "price": 64.99 }]""";
    var discountsJsonString = """[{ "key": "SALE10", "value": 0.1 }, { "key": "SALE20", "value": 0.2 }, { "key":"SALE30", "value": 0.3 }]""";

    private val gson = Gson()
    var orderss: Orders = gson.fromJson(orderJsonString, Orders::class.java)
    var productss: Products = gson.fromJson(productJsonString, Products::class.java)
    var discountss: Discounts = gson.fromJson(discountsJsonString, Discounts::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val result = calculateSales(orderss,productss,discountss)

        binding.textView5.text=result.totalSalesBeforeDiscount.toString()
        binding.textView6.text=result.totalSalesAfterDiscount.toString()
        binding.textView7.text=result.totalAmountLost.toString()
        binding.textView8.text=result.averageDiscountPercentage.toString()
        Log.e("TotalSalesBeforeDiscount",  "$result.totalSalesBeforeDiscount.toString()")


    }
    private fun calculateSales(orders:Orders, products:Products, discounts: Discounts): Result{
        var totalSalesBeforeDiscount = 0.0
        var totalSalesAfterDiscount = 0.0
        var totalAmountLost = 0.0
        var numDiscountedSales = 0
        for (order in orders) {
            val code = order.discount
            var discountsValue = 0.0
            var afterDiscountsValue = 0.0
            when (code) {
                "SALE10" -> {
                    discountsValue = 0.1
                    afterDiscountsValue=0.9
                }

                "SALE20" -> {
                    discountsValue = 0.2
                    afterDiscountsValue=0.8
                }
                "SALE30" -> {
                    discountsValue = 0.3
                    afterDiscountsValue = 0.7
                }
                else -> {
                    discountsValue = 0.0
                    afterDiscountsValue = 0.0
                   }
                }

            for (item in order.items!!) {

                val prodetid = item?.sku
                val quantity = item?.quantity
               //Log.e("quantity",  "order: ${order.orderId} quantity: $quantity")
                for (pid in products) {
                    if (pid.sku == prodetid)
                        if (item != null) {
                            val price = pid.price

                            if (price != null) {
                                totalSalesBeforeDiscount += (price * quantity!!)
                                if (discountsValue!=0.0) {
                                    val discountPrice=price  * afterDiscountsValue
                                    totalSalesAfterDiscount += discountPrice * quantity!!

                                    val amountLost=price  * discountsValue
                                    totalAmountLost += amountLost * quantity!!

                                    numDiscountedSales++
                                } else {
                                    totalSalesAfterDiscount += (price * quantity!!)

                                }

                            }
                        }
                }
            }
        }
        var averageDiscountPercentage = 0.0
        if (numDiscountedSales > 0) {
            val totalDiscountPercentage = totalAmountLost / totalSalesBeforeDiscount * 100
            averageDiscountPercentage = totalDiscountPercentage / numDiscountedSales
        }
        Log.e("TotalSalesBeforeDiscount",  "$totalSalesBeforeDiscount")
        Log.e("TotalSalesAfterDiscount", "$totalSalesAfterDiscount")
        Log.e("TotalAmountLost", "$totalAmountLost")
        Log.e("AverageDiscountPercentage", "$averageDiscountPercentage")
        return Result(
            totalSalesBeforeDiscount,
            totalSalesAfterDiscount,
            totalAmountLost,
            averageDiscountPercentage
        )
    }

}