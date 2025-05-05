package com.studioxid.test.vendingmachine.domain

import arrow.core.Either

class Cash(
    override val amount: Int,
    val currency: Currency = Currency.WON,
) : PayMethod {
    override fun equals(other: Any?): Boolean {
        return other is Cash && other.amount == this.amount && other.currency == this.currency
    }

    override fun hashCode(): Int {
        var result = amount
        result = 31 * result + currency.hashCode()
        return result
    }
}

enum class Currency {
    WON,
}