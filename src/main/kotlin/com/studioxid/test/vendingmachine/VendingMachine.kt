package com.studioxid.test.vendingmachine

import com.studioxid.test.vendingmachine.application.port.`in`.InsertCashInputPort
import com.studioxid.test.vendingmachine.application.port.`in`.InsertCardInputPort
import com.studioxid.test.vendingmachine.application.port.`in`.PurchaseProductInputPort
import com.studioxid.test.vendingmachine.application.port.`in`.ReturnBalanceInputPort
import com.studioxid.test.vendingmachine.application.service.VendingMachineService
import com.studioxid.test.vendingmachine.domain.*
import kotlin.system.exitProcess

class VendingMachine(
    val purchaseProductInputPort: PurchaseProductInputPort,
    val insertCashInputPort: InsertCashInputPort,
    val insertCardInputPort: InsertCardInputPort,
    val returnBalanceInputPort: ReturnBalanceInputPort,
    private val balance: Balance = Balance(),
    private val products: Array<Product> = arrayOf(
        Product("콜라", 1100),
        Product("물", 600),
        Product("커피", 700)
    ),
    private val caseArray: Array<Cash> = arrayOf(
        Cash(100, Currency.WON),
        Cash(500, Currency.WON),
        Cash(1000, Currency.WON),
        Cash(5000, Currency.WON),
        Cash(10000, Currency.WON),
    )
) {

    fun start() {
        val validCommands: List<String> = listOf("1", "2", "3", "4", "5", "6", "7")
        while (true) {
            when(val command = inputCommand(validCommands, ::printCommand)) {
                1,2,3 -> {
                    val purchaseResult = purchaseProductInputPort.purchaseProduct(products[command - 1], balance)
                    purchaseResult.fold(
                        ifLeft = { throwable -> println(throwable.message) },
                        ifRight = { product -> println("${product.name} 구매 성공, 현재 잔액 ${balance.calculateTotalBalance()}") }
                    )
                }
                4 -> {
                    insertCash()
                }
                5 -> {
                    val insertCardResult = insertCardInputPort.insertCard(
                        Card(
                            "1111",
                            CardType.CHECK_CARD,
                            "신한카드",
                            10000
                        ), balance)

                    insertCardResult.fold(
                        ifLeft = { throwable -> println(throwable.message) },
                        ifRight = { card -> println("${card.company} 투입 성공, 현재 잔액 ${balance.calculateTotalBalance()}") }
                    )
                }
                6 -> {
                    val returnBalanceResult = returnBalanceInputPort.returnBalance(balance)
                    returnBalanceResult.fold(
                        ifLeft = { throwable -> println(throwable.message) },
                        ifRight = { payMethodList ->
                            for(it in payMethodList) {
                                when(val payMethod = it.first) {
                                    is Card -> println("${payMethod.company} 반환 성공")
                                    is Cash -> println("${payMethod.amount} X ${it.second} 반환 성공")
                                }
                            }
                        }
                    )
                }
                7 -> end()
            }
        }
    }

    private fun insertCash() {
        val validCommands: List<String> = listOf("1", "2", "3", "4", "5", "6")
        when(val command = inputCommand(validCommands, ::printInsertCashCommand)) {
            1,2,3,4,5 -> {
                val insertedCashResult = insertCashInputPort.insertCash(caseArray[command -1 ], balance)
                insertedCashResult.fold(
                    ifLeft = { throwable -> println(throwable.message) },
                    ifRight = { product -> println("${product.amount} 투입 성공, 현재 잔액 ${balance.calculateTotalBalance()}") }
                )
            }
            else -> println("잘못된 화폐로 명령입력단계로 돌아갑니다.")
        }
    }

    private fun printInsertCashCommand() {
        println("투입할 화폐를 선택하세요.")
        println("1) 100원")
        println("2) 500원")
        println("3) 1000원")
        println("4) 5000원")
        println("5) 10000원")
        println("6) 그 외")
    }

    private fun end() {
        println("자판기가 종료됩니다.")
        exitProcess(0)
    }

    private fun inputCommand(validCommands: List<String>, printCommand: () -> Unit): Int {
        printCommand()
        var command = readln()
        while(!validCommands.contains(command)) {
            println("${validCommands.first()}~${validCommands.last()}중 유효한 숫자만 입력하세요.")
            printCommand()
            command = readln()
        }
        return command.toInt()
    }

    private fun printCommand() {
        println("자판기에서 진행할 행동을 입력해주세요.")
        println("1) 콜라(1,100원) 구입")
        println("2) 물(600원) 구입")
        println("3) 커피(700원) 구입")
        println("4) 현금 투입")
        println("5) 카드 투입")
        println("6) 잔돈 반환")
        println("7) 종료")
    }
}

fun main(args: Array<String>) {
    val vendingMachineService = VendingMachineService(
        maxCashStock = mapOf(
            Cash(100, Currency.WON) to 100,
            Cash(500, Currency.WON) to 100,
            Cash(1000, Currency.WON) to 100,
            Cash(5000, Currency.WON) to 100,
            Cash(10000, Currency.WON) to 100,
        ),
        cashStock = mutableMapOf(
            Cash(100, Currency.WON) to 0,
            Cash(500, Currency.WON) to 0,
            Cash(1000, Currency.WON) to 0,
            Cash(5000, Currency.WON) to 0,
            Cash(10000, Currency.WON) to 0,
        ),
        productStock = mutableMapOf(
            Product("콜라", 1100) to 100,
            Product("물", 600) to 100,
            Product("커피", 700) to 100,
        )
    )

    val application = VendingMachine(
        purchaseProductInputPort = vendingMachineService,
        insertCashInputPort = vendingMachineService,
        insertCardInputPort = vendingMachineService,
        returnBalanceInputPort = vendingMachineService,
    )

    application.start()
}