package com.ma.rovers.tests

import com.ma.rovers.domain.Direction
import com.ma.rovers.domain.IRover
import com.ma.rovers.domain.IntSize
import com.ma.rovers.domain.Point
import com.ma.rovers.repositories.InMemoryFieldsRepository
import com.ma.rovers.usecases.IRoverUseCase
import com.ma.rovers.usecases.RoverUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UseCasesTests {
    @Test
    fun `Define field`() { // Создать и сохранить плато
        // Given
        val repository = InMemoryFieldsRepository()
        val roverMgr: IRoverUseCase = RoverUseCase(repository)
        val length = 10
        val width = 20

        // When
        val result = roverMgr.defineField(IntSize(length), IntSize(width))

        // Then
        assertEquals(true, result.successful, "Result should be successful")

        // When
        val savedField = repository.restore()
        // Then
        assertAll("field properties",
            { assertEquals(IntSize(length), savedField.length, "Restored field is not what was requested") },
            { assertEquals(IntSize(width), savedField.width, "Restored field is not what was requested") }
        )
    }

    @Test
    fun `Put rover to field`() { // Поместить марсоход на плато
        // Given
        val repository = InMemoryFieldsRepository()
        val roverMgr: IRoverUseCase = RoverUseCase(repository)
        val length = 10
        val width = 20
        val position = Point(3, 3)
        val direction = Direction.SOUTH

        // When
        repository.delete()
        val result1 = roverMgr.putRoverToField(position, direction)
        // Then
        assertEquals(false, result1.successful, "No field to place rover: result should not be successful")

        // When
        val defineFieldResult = roverMgr.defineField(IntSize(length), IntSize(width))
        // Then
        assertEquals(true, defineFieldResult.successful, "Result should be successful")

        // When
        val result2 = roverMgr.putRoverToField(position, direction)
        // Then
        val theRover = repository.restore().cell(position).locatedObject as? IRover
        assertAll("rover properties",
            { assertEquals(true, result2.successful, "Result should be successful") },
            { assertNotEquals(null, theRover, "Rover not found where expected") },
            { assertEquals(position, theRover?.location?.coordinates, "Rover coordinates are wrong") },
            { assertEquals(direction, theRover?.cameraDirection, "Rover direction is wrong") }
        )
    }

    @Test
    fun `Run rover program`() { // Вызвать на выполнение программу марсохода на плато
        // Given
        val repository = InMemoryFieldsRepository()
        val roverMgr: IRoverUseCase = RoverUseCase(repository)
        val length = 10
        val width = 20
        val position = Point(3, 3)
        val direction = Direction.SOUTH
        val program = "MRMLML"

        // When
        repository.delete()
        val result1 = roverMgr.runRoverProgram(position, program)
        // Then
        assertEquals(false, result1.successful, "No field: result should not be successful")

        // When
        val defineFieldResult = roverMgr.defineField(IntSize(length), IntSize(width))
        // Then
        assertEquals(true, defineFieldResult.successful, "Result should be successful")

        // When
        val result2 = roverMgr.runRoverProgram(position, program)
        // Then
        assertEquals(false, result2.successful, "No rover: result should not be successful")

        // When
        val putRoverToFieldResult = roverMgr.putRoverToField(position, direction)
        // Then
        assertEquals(true, putRoverToFieldResult.successful, "Result should be successful")

        // When
        val result3 = roverMgr.runRoverProgram(position, program)
        // Then
        val theRover = repository.restore().cell(Point(2, 1)).locatedObject as? IRover
        assertAll("rover properties",
            { assertEquals(true, result3.successful, "Result should be successful") },
            { assertNotEquals(null, theRover, "Rover not found where expected") },
            { assertEquals(Point(2, 1), theRover?.location?.coordinates, "Rover coordinates are wrong") },
            { assertEquals(Direction.EAST, theRover?.cameraDirection, "Rover direction is wrong") }
        )
    }

    @Test
    fun `Get field state`() { // Получить статус марсохода
        // Given
        val repository = InMemoryFieldsRepository()
        val roverMgr: IRoverUseCase = RoverUseCase(repository)
        val length = 5
        val width = 5
        val position = Point(3, 3)
        val direction = Direction.SOUTH

        // When
        repository.delete()
        val result1 = roverMgr.getFieldState()
        // Then
        assertEquals(false, result1.successful, "No field: result should not be successful")

        // When
        val defineFieldResult = roverMgr.defineField(IntSize(length), IntSize(width))
        val result2 = roverMgr.getFieldState()
        // Then
        assertAll("field properties",
            { assertEquals(true, defineFieldResult.successful, "Result should be successful") },
            { assertEquals(true, result2.successful, "Result should be successful") },
            { assertEquals(length, result2.length, "Field length is wrong") },
            { assertEquals(width, result2.width, "Field width is wrong") },
            { assertEquals(true, result2.rovers.isEmpty(), "No array expected") }
        )
        // When
        val putRoverToFieldResult = roverMgr.putRoverToField(position, direction)
        val result3 = roverMgr.getFieldState()
        // Then
        assertAll("field and rover properties",
            { assertEquals(true, putRoverToFieldResult.successful, "Result should be successful") },
            { assertEquals(true, result3.successful, "Result should be successful") },
            { assertEquals(length, result3.length, "Field length is wrong") },
            { assertEquals(width, result3.width, "Field width is wrong") },
            { assertEquals(position, result3.rovers[0].coordinates, "Rover position is wrong") },
            { assertEquals(direction, result3.rovers[0].direction, "Rover direction is wrong") }
        )
    }
}