package com.ma.rovers.usecases

import com.ma.rovers.domain.Direction
import com.ma.rovers.domain.IntSize
import com.ma.rovers.domain.Point

interface IRoverUseCase {
    fun defineField(length: IntSize, width: IntSize): DefineFieldResult

    class DefineFieldResult(
        override val successful: Boolean = true,
        override val errorMessage: String = "") : IUseCaseResult {
    }

    fun putRoverToField(coordinates: Point, direction: Direction): PutRoverToFieldResult

    class PutRoverToFieldResult(
        override val successful: Boolean = true,
        override val errorMessage: String = "") : IUseCaseResult {
    }

    fun runRoverProgram(coordinates: Point, program: String): RunRoverProgramResult

    class RunRoverProgramResult(
        override val successful: Boolean = true,
        override val errorMessage: String = "") : IUseCaseResult {
    }

    fun getFieldState(): GetFieldStateResult

    class GetFieldStateResult(
        val length: Int,
        val width: Int,
        val rovers: Array<RoverInfo>,
        override val successful: Boolean = true,
        override val errorMessage: String = "") :IUseCaseResult {
    }

    data class RoverInfo(
        val coordinates: Point,
        val direction: Direction
    )

}