package com.ma.rovers.usecases

import com.ma.rovers.domain.*
import com.ma.rovers.repositories.IFieldsRepository
import java.lang.Exception
import java.rmi.UnexpectedException

class RoverManager(val repository: IFieldsRepository): IRoverManager {
    override fun defineField(length: IntSize, width: IntSize): IRoverManager.DefineFieldResult {
        try {
            val field = Field(length, width)
            repository.save(field)
        }
        catch (exception: Exception){
            return IRoverManager.DefineFieldResult(length,width, false, "Could not define field as requested")
        }
        return IRoverManager.DefineFieldResult(length,width)
    }

    override fun putRoverToField(coordinates: Point, direction: Direction): IRoverManager.PutRoverToFieldResult {
        try {
            val field = repository.restore()
            val rover = Rover(direction)
            field.placeObject(rover, coordinates)
            repository.save(field)
        }
        catch (exception: Exception){
            return IRoverManager.PutRoverToFieldResult(coordinates, direction, false,"Could not place rover as requested")
        }
        return IRoverManager.PutRoverToFieldResult(coordinates, direction)
    }

    override fun runRoverProgram(coordinates: Point, program: String): IRoverManager.RunRoverProgramResult {
        try {
            val field = repository.restore()
            val rover = field.cell(coordinates).locatedObject as IRover
            rover.executeProgram(program)
            repository.save(field)
            val newCoordinates = rover.location?.coordinates
            if (newCoordinates!=null)
                return IRoverManager.RunRoverProgramResult(newCoordinates, rover.cameraDirection)
            else
                throw UnexpectedException("Rover location coordinates are unexpectedly null")
        }
        catch (exception: Exception){
            return IRoverManager.RunRoverProgramResult(coordinates, Direction.NORTH,false,"Could not execute program as requested")
        }

    }
}