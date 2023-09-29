package com.ma.rovers.usecases

import com.ma.rovers.domain.Direction
import com.ma.rovers.domain.IntSize
import com.ma.rovers.domain.Point

interface IRoverManager {
    fun defineField(length: IntSize, width: IntSize): DefineFieldResult

    class DefineFieldResult(
        val length: IntSize,
        val width: IntSize,
        override val successful: Boolean = true,
        override val errorMessage: String = "") : IUseCaseResult {
    }

    fun putRoverToField(coordinates: Point, direction: Direction): PutRoverToFieldResult

    class PutRoverToFieldResult(
        val coordinates: Point, val direction: Direction,
        override val successful: Boolean = true,
        override val errorMessage: String = "") : IUseCaseResult {
    }

    fun runRoverProgram(coordinates: Point, program: String): RunRoverProgramResult

    class RunRoverProgramResult(
        val newCoordinates: Point,
        val newDirection: Direction,
        override val successful: Boolean = true,
        override val errorMessage: String = "") : IUseCaseResult {
    }
}