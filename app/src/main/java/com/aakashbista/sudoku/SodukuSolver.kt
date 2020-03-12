package com.aakashbista.sudoku

class SodukuSolver {

    fun isValidNumber(grid: Array<IntArray>, num: Int, row: Int, col: Int): Boolean {
        //check for row
        for (i in grid.indices) {
            if (grid[row][i] == num  && i != col) return false
        }

        // check for column
        for (i in 0 until grid[0].size) {
            if (grid[i][col] == num && i != row) return false
        }

        // check in box
        val r = row - row % 3
        val c = col - col % 3
        for (i in r until r + 3) {
            for (j in c until c + 3) {
                if (grid[i][j] == num &&( i != row && j!=col)) return false
            }
        }
        return true
    }

    fun solve(grid: Array<IntArray>): Boolean {
        for (row in grid.indices) {
            for (col in grid.indices) {
                if (grid[row][col] == 0) {
                    for (num in 1..9) {
                        if (isValidNumber(grid, num, row, col)) {
                            grid[row][col] = num
                            if (solve(grid)) return true else grid[row][col] = 0
                        }
                    }
                    return false
                }
            }
        }
        return true
    }
}

