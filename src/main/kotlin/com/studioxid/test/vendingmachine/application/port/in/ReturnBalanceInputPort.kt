package com.studioxid.test.vendingmachine.application.port.`in`

import arrow.core.Either
import com.studioxid.test.vendingmachine.domain.Balance
import com.studioxid.test.vendingmachine.domain.Cash
import com.studioxid.test.vendingmachine.domain.PayMethod

interface ReturnBalanceInputPort {
    fun returnBalance(balance: Balance): Either<Throwable, List<Pair<PayMethod, Int>>>
}