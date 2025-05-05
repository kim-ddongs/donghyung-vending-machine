package com.studioxid.test.vendingmachine.domain

import arrow.core.Either

interface PayMethod {
    val amount: Int
}