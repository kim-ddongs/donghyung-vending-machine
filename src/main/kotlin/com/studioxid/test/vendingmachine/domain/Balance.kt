package com.studioxid.test.vendingmachine.domain

class Balance {
    private var cashBalance: Int = 0
    private var cardBalance: Int = 0

    fun getCashBalance(): Int {
        return cashBalance;
    }

    fun calculateTotalBalance(): Int {
        return cashBalance + cardBalance
    }

    fun deductBalance(amount: Int): Int {
        if(cashBalance < amount) {
            cardBalance = calculateTotalBalance() - amount
            cashBalance = 0
        } else {
            cashBalance -= amount
        }
        return calculateTotalBalance()
    }

    fun insertCash(cash: Cash): Int {
        cashBalance += cash.amount

        return calculateTotalBalance()
    }

    fun insertCard(card: Card): Int {
        cardBalance += card.amount

        return calculateTotalBalance()
    }

    fun reset() {
        cashBalance = 0
        cardBalance = 0
    }

}