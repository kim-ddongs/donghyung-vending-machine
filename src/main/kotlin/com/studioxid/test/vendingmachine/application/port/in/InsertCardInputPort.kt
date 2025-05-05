package com.studioxid.test.vendingmachine.application.port.`in`

import arrow.core.Either
import com.studioxid.test.vendingmachine.domain.Balance
import com.studioxid.test.vendingmachine.domain.Card

interface InsertCardInputPort {
    fun insertCard(card: Card, balance: Balance): Either<Throwable, Card>
}