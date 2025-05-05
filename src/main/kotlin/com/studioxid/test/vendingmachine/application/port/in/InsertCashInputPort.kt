package com.studioxid.test.vendingmachine.application.port.`in`

import arrow.core.Either
import com.studioxid.test.vendingmachine.domain.Balance
import com.studioxid.test.vendingmachine.domain.Cash

interface InsertCashInputPort {
    fun insertCash(cash: Cash, balance: Balance): Either<Throwable, Cash>

}