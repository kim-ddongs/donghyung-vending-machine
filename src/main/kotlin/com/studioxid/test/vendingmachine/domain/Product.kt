package com.studioxid.test.vendingmachine.domain

data class Product(
    val name: String,
    val price: Int
) {
    override fun equals(other: Any?): Boolean {
        return other is Product && other.name == this.name && other.price == this.price
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + price
        return result
    }
}