package com.ma.rovers.tests

import com.ma.rovers.domain.Direction
import com.ma.rovers.domain.IRover
import com.ma.rovers.domain.IntSize
import com.ma.rovers.domain.Point
import com.ma.rovers.repositories.InMemoryFieldsRepository
import com.ma.rovers.usecases.IRoverManager
import com.ma.rovers.usecases.RoverManager
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UseCasesTests {
    @Test
    fun defineField() { // Создать и сохранить плато
        // Given
        val repository = InMemoryFieldsRepository()
        val roverMgr: IRoverManager = RoverManager(repository)
        val length = 10
        val width = 20

        // When
        val result = roverMgr.defineField(IntSize(length), IntSize(width))

        // Then
        assertAll("result properties",
            { assertEquals(result.successful, true, "Result should be successful") },
            { assertEquals(result.length, IntSize(length), "Result contains wrong length") },
            { assertEquals(result.width, IntSize(width), "Result contains wrong width") }
        )

        // When
        val savedField = repository.restore()
        // Then
        assertAll("field properties",
            { assertEquals(savedField.length, IntSize(length), "Restored field is not what was requested") },
            { assertEquals(savedField.width, IntSize(width), "Restored field is not what was requested") }
        )
    }

    @Test
    fun putRoverToField() { // Поместить марсоход на плато
        // Given
        val repository = InMemoryFieldsRepository()
        val roverMgr: IRoverManager = RoverManager(repository)
        val length = 10
        val width = 20
        val position = Point(3, 3)
        val direction = Direction.SOUTH

        // When
        repository.delete()
        val result1 = roverMgr.putRoverToField(position, direction)
        // Then
        assertEquals(result1.successful, false, "No field to place rover: result should not be successful")

        // When
        val defineFieldResult = roverMgr.defineField(IntSize(length), IntSize(width))
        // Then
        assertEquals(defineFieldResult.successful, true, "Result should be successful")

        // When
        val result2 = roverMgr.putRoverToField(position, direction)
        // Then
        val theRover = repository.restore().cell(position).locatedObject as IRover
        assertAll("rover properties",
            { assertEquals(result2.successful, true, "Result should be successful") },
            { assertEquals(result2.coordinates, theRover.location?.coordinates, "Rover coordinates are wrong") },
            { assertEquals(result2.direction, theRover.cameraDirection, "Rover direction is wrong") }
        )
    }

    @Test
    fun runRoverProgram() { // Вызвать на выполнение программу марсохода на плато
        // Given
        val repository = InMemoryFieldsRepository()
        val roverMgr: IRoverManager = RoverManager(repository)
        val length = 10
        val width = 20
        val position = Point(3, 3)
        val direction = Direction.SOUTH
        val program = "MRMLML"

        // When
        repository.delete()
        val result1 = roverMgr.runRoverProgram(position, program)
        // Then
        assertEquals(result1.successful, false, "No field: result should not be successful")

        // When
        val defineFieldResult = roverMgr.defineField(IntSize(length), IntSize(width))
        // Then
        assertEquals(defineFieldResult.successful, true, "Result should be successful")

        // When
        val result2 = roverMgr.runRoverProgram(position, program)
        // Then
        assertEquals(result2.successful, false, "No rover: result should not be successful")

        // When
        val putRoverToFieldResult = roverMgr.putRoverToField(position, direction)
        // Then
        assertEquals(putRoverToFieldResult.successful, true, "Result should be successful")

        // When
        val result3 = roverMgr.runRoverProgram(position, program)
        // Then
        assertEquals(result3.successful, true, "Result should be successful")
        assertEquals(Point(2,1), result3.newCoordinates, "New coordinates are not what were expected")
        assertEquals(Direction.EAST, result3.newDirection, "New direction is not what was expected")
    }
}