package com.ma.rovers.usecases

import com.ma.rovers.domain.*
import com.ma.rovers.repositories.IFieldsRepository
import java.lang.Exception

class RoverUseCase(val repository: IFieldsRepository): IRoverUseCase {
    override fun defineField(length: IntSize, width: IntSize): IRoverUseCase.DefineFieldResult {
        try {
            val field = Field(length, width)
            repository.save(field)
            return IRoverUseCase.DefineFieldResult()
        }
        catch (exception: Exception){
            return IRoverUseCase.DefineFieldResult(false, "Could not define field as requested")
        }
    }

    override fun putRoverToField(coordinates: Point, direction: Direction): IRoverUseCase.PutRoverToFieldResult {
        try {
            val field = repository.restore()
            val rover = Rover(direction)
            field.placeObject(rover, coordinates)
            repository.save(field)
            return IRoverUseCase.PutRoverToFieldResult()
        }
        catch (exception: Exception){
            return IRoverUseCase.PutRoverToFieldResult(false,"Could not place rover as requested")
        }
    }

    override fun runRoverProgram(coordinates: Point, program: String): IRoverUseCase.RunRoverProgramResult {
        try {
            val field = repository.restore()
            val rover = field.cell(coordinates).locatedObject as IRover
            rover.executeProgram(program)
            repository.save(field)
            return IRoverUseCase.RunRoverProgramResult()
        }
        catch (exception: Exception){
            return IRoverUseCase.RunRoverProgramResult(false,"Could not execute program as requested")
        }

    }

    override fun getFieldState(): IRoverUseCase.GetFieldStateResult {
        try {
            val field = repository.restore()
            val rovers = mutableListOf<IRoverUseCase.RoverInfo>()
            for (x in 0..< field.length.size)
                for (y in 0..< field.width.size){
                    val rover = field.cell(Point(x, y)).locatedObject
                    if (rover is IRover)
                        rovers.add(IRoverUseCase.RoverInfo(Point(x,y), rover.cameraDirection))
                }
            return IRoverUseCase.GetFieldStateResult(field.length.size, field.width.size, rovers.toTypedArray())
        }
        catch (exception: Exception){
            return IRoverUseCase.GetFieldStateResult(0, 0, arrayOf(), false,"Could not get field state as requested")
        }
    }
}