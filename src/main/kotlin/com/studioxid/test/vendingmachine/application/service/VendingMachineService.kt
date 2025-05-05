package com.studioxid.test.vendingmachine.application.service

import arrow.core.Either
import com.studioxid.test.vendingmachine.application.port.`in`.*
import com.studioxid.test.vendingmachine.domain.*
import kotlin.math.min

class VendingMachineService(
    var cashStock: MutableMap<Cash, Int>,
    var cardSlot: Card? = null,
    var productStock: MutableMap<Product, Int>,
    val maxCashStock: Map<Cash, Int>,
)
    : PurchaseProductInputPort,
    InsertCashInputPort,
    InsertCardInputPort,
    ReturnBalanceInputPort {
    override fun insertCard(card: Card, balance: Balance): Either<Throwable, Card> {
        return cardSlot?.let {
            Either.Left(RuntimeException("${it.company}가 이미 투입되어있습니다."))
        } ?: run {
            cardSlot = card
            balance.insertCard(card);
            Either.Right(card)
        }
    }

    override fun insertCash(cash: Cash, balance: Balance): Either<Throwable, Cash> {
        if(maxCashStock[cash]!! == cashStock[cash]) {
            return Either.Left(RuntimeException("${cash.amount}가 가득 찼습니다. 관라자에게 문의 부탁드립니다."))
        }
        balance.insertCash(cash)
        cashStock[cash] = cashStock[cash]!! + 1
        return Either.Right(cash)
    }

    override fun purchaseProduct(product: Product, balance: Balance): Either<Throwable, Product> {
        if(this.productStock[product] == 0) {
            return Either.Left(RuntimeException("${product.name}의 재고가 부족합니다.", ))
        }
        if(balance.calculateTotalBalance() >= product.price) {
            balance.deductBalance(product.price)
            productStock[product] = productStock[product]!! - 1
            return Either.Right(product)
        } else {
            return Either.Left(RuntimeException("잔액이 부족합니다."))
        }
    }

    override fun returnBalance(balance: Balance): Either<Throwable, List<Pair<PayMethod, Int>>> {
        if(balance.calculateTotalBalance() == 0) {
            return Either.Left(RuntimeException("자판기에 투입된 금액이 없습니다."))
        }

        var leftAmount = balance.getCashBalance()
        val availableCashList = cashStock.filter { it.value > 0 }
            .map { it.key to it.value }
            .sortedBy { it.first.amount.dec() }

        val usedCashList = availableCashList.map {
            val canUseCashAmount = min(leftAmount / it.first.amount, it.second)
            leftAmount -= canUseCashAmount * it.first.amount
            it.first to canUseCashAmount
        }.filter { it.second > 0 }
        .toMutableList()

        if(leftAmount > 0) {
            return Either.Left(RuntimeException("잔액을 거슬러줄 수 있는 현금이 부족합니다. 관리자에게 문의바랍니다."))
        } else {

            balance.reset()
            val result = usedCashList.map {
                cashStock[it.first] = cashStock[it.first]!! - it.second
                (it.first as PayMethod) to it.second
            }.toMutableList()
            cardSlot?.let {
                result.add(it to 1)
            }
            cardSlot = null
            return Either.Right(result)
        }
    }
}