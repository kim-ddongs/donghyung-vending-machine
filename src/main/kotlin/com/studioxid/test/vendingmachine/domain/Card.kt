package com.studioxid.test.vendingmachine.domain

import arrow.core.Either

class Card(
    val cardNumber: String,
    val cardType: CardType,
    val company: String,
    override val amount: Int,
) : PayMethod

enum class CardType {
    CHECK_CARD,
    CREDIT_CARD
}