package com.ma.rovers.tests

import com.ma.rovers.controllers.FieldController
import com.ma.rovers.domain.Direction
import com.ma.rovers.domain.Point
import com.ma.rovers.presenters.SimpleFieldPresenter
import com.ma.rovers.repositories.InMemoryFieldsRepository
import com.ma.rovers.usecases.IRoverUseCase
import com.ma.rovers.usecases.RoverUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ControllerTests {
    @Test
    fun `onNewFieldRequested`(){
        // Given
        val repository = InMemoryFieldsRepository()
        val presenter = SimpleFieldPresenter()
        val controller = FieldController(RoverUseCase(repository), presenter)
        val length = 5
        val width = 15

        // When
        val result = controller.onNewFieldRequested(length, width)

        // Then
        val expectedFieldState = IRoverUseCase.GetFieldStateResult(
            length,
            width,
            arrayOf()
        )
        val expectedViewModel = presenter.formatFieldViewModel(expectedFieldState)

        assertEquals(expectedViewModel.fieldLength, result.fieldLength)
        assertEquals(expectedViewModel.fieldWidth, result.fieldWidth)
        assertEquals(expectedViewModel.fieldSizeDescription, result.fieldSizeDescription)
        assertEquals(expectedViewModel.fieldObjects.size, result.fieldObjects.size)
    }

    @Test
    fun `onNewRoverPlacementRequested`(){
        // Given
        val repository = InMemoryFieldsRepository()
        val presenter = SimpleFieldPresenter()
        val controller = FieldController(RoverUseCase(repository), presenter)
        val length = 5
        val width = 15
        val position = Point(3, 4)
        val direction = Direction.EAST

        // When
        repository.delete()
        controller.onNewFieldRequested(length, width)
        val result = controller.onNewRoverPlacementRequested(position, direction)

        // Then
        val expectedFieldState = IRoverUseCase.GetFieldStateResult(
            length,
            width,
            arrayOf(IRoverUseCase.RoverInfo(position, direction))
        )
        val expectedViewModel = presenter.formatFieldViewModel(expectedFieldState)

        assertEquals(expectedViewModel.fieldLength, result.fieldLength)
        assertEquals(expectedViewModel.fieldWidth, result.fieldWidth)
        assertEquals(expectedViewModel.fieldSizeDescription, result.fieldSizeDescription)
        assertEquals(expectedViewModel.fieldObjects.size, result.fieldObjects.size)
    }

    @Test
    fun `onRunRoverProgramRequested`(){
        // Given
        val repository = InMemoryFieldsRepository()
        val presenter = SimpleFieldPresenter()
        val controller = FieldController(RoverUseCase(repository), presenter)
        val length = 5
        val width = 15
        val position = Point(3, 3)
        val direction = Direction.NORTH
        val program = "MRMLM" //
        val newPosition = Point(5, 4)

        // When
        repository.delete()
        controller.onNewFieldRequested(length, width)
        controller.onNewRoverPlacementRequested(position, direction)
        val result = controller.onRunRoverProgramRequested(position, program)

        // Then
        val expectedFieldState = IRoverUseCase.GetFieldStateResult(
            length,
            width,
            arrayOf(IRoverUseCase.RoverInfo(newPosition, direction))
        )
        val expectedViewModel = presenter.formatFieldViewModel(expectedFieldState)

        assertEquals(expectedViewModel.fieldLength, result.fieldLength)
        assertEquals(expectedViewModel.fieldWidth, result.fieldWidth)
        assertEquals(expectedViewModel.fieldSizeDescription, result.fieldSizeDescription)
        assertEquals(expectedViewModel.fieldObjects.size, result.fieldObjects.size, "Wrong number of objects")
    }
}