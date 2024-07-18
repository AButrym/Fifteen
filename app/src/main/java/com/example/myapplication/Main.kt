package com.example.myapplication

import kotlin.math.abs

// MVC - Model View Controller

// Controller - dialog with user
// View - print board
// Model - 1) state (data) 2) logic (service)


const val EMPTY: Byte = 16
const val DIM = 4
val INITIAL_STATE = ByteArray(16) { (it + 1).toByte() }
val TEST_STATE = byteArrayOf(1,2,3,4,5,6,7,8,9,10,11,12,13,14,16,15)

// MODEL: STATE
var state = INITIAL_STATE.clone()

// MODEL: ENGINE
fun transitionState(oldState: ByteArray, cell: Byte): ByteArray {
    val ixCell = oldState.indexOf(cell)
    val ixEmpty = oldState.indexOf(EMPTY)

    return if (areAdjacent(ixCell, ixEmpty))
        withSwapped(oldState, ixCell, ixEmpty)
    else oldState
}

fun withSwapped(arr: ByteArray, ix1: Int, ix2: Int): ByteArray {
    if (ix1 == ix2) return arr
    val res = arr.clone()
    res[ix1] = res[ix2].also { res[ix2] = res[ix1] }
    return res
}

fun areAdjacent(ix1: Int, ix2: Int): Boolean {
    val row1 = row(ix1)
    val col1 = col(ix1)
    val row2 = row(ix2)
    val col2 = col(ix2)
    return (row1 == row2 && abs(col1 - col2) == 1 ||
            col1 == col2 && abs(row1 - row2) == 1)
}

fun row(ix: Int) = ix / DIM
fun col(ix: Int) = ix % DIM
fun ix(row: Int, col: Int) = row * DIM + col

fun isWin(state: ByteArray): Boolean =
    state.contentEquals(INITIAL_STATE)

// CONTROLLER
fun main() {
    println("Welcome to Fifteen Game!")
//    state = TEST_STATE
    state.shuffle()
    while (!isWin(state)) {
        printBoard(state)
        val cell: Byte = readCell()
        state = transitionState(state, cell)
    }
    printBoard(state)
    println("You won!")
}

fun readCell(): Byte {
    while (true) {
        println("Enter cell to move (1..15):")
        val res = readln().toIntOrNull()
        if (res in 1..15) return res!!.toByte()
    }
}

// VIEW
fun printBoard(state: ByteArray, print: (String) -> Unit = ::print) {
    print("-".repeat(18))
    print("\n")
    for (iRow in 0..<DIM) {
        print("|")
        for (iCol in 0..<DIM) {
            print(formatCell(state[ix(iRow, iCol)]))
        }
        print("|\n")
    }
}

fun formatCell(cell: Byte) = "%3s ".format(if (cell == EMPTY) " " else cell)
