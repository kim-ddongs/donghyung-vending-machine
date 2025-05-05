package com.studioxid.test.vendingmachine.application.port.`in`

import arrow.core.Either
import com.studioxid.test.vendingmachine.domain.Balance
import com.studioxid.test.vendingmachine.domain.Product

interface PurchaseProductInputPort {
    fun purchaseProduct(product: Product, balance: Balance): Either<Throwable, Product>
}