package com.ma.rovers.tests

import com.ma.rovers.domain.Direction
import com.ma.rovers.domain.Point
import com.ma.rovers.presenters.IFieldPresenter
import com.ma.rovers.presenters.SimpleFieldPresenter
import com.ma.rovers.presenters.TabularFieldPresenter
import com.ma.rovers.usecases.IRoverUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
class PresenterTests {
    @Test
    fun `Format result with SimpleFieldPresenter`(){
        // Given
        val length = 10
        val width = 20
        val position = Point(3, 5)
        val direction = Direction.SOUTH
        val presenter: IFieldPresenter = SimpleFieldPresenter()
        val fieldState = IRoverUseCase.GetFieldStateResult(
            length,
            width,
            arrayOf(IRoverUseCase.RoverInfo(position, direction))
        )

        // When
        val viewModel = presenter.formatFieldViewModel(fieldState)

        // Then
        assertEquals(length, viewModel.fieldLength, "Incorrect length")
        assertEquals(width, viewModel.fieldWidth, "Incorrect width")
        assertEquals("Перед нами поле размером $length x $width ячеек", viewModel.fieldSizeDescription,
            "Incorrect field size description")
        assertEquals(1, viewModel.fieldObjects.size, "Incorrect number of field objects")
        assertEquals("На ячейке [${position.x}, ${position.y}] находится марсоход; направление камеры - $direction",
            viewModel.fieldObjects[0], "Incorrect description of field objects")
    }

    @Test
    fun `Format error with SimpleFieldPresenter`(){
        // Given
        val presenter: IFieldPresenter = SimpleFieldPresenter()
        val errorMessage = "Incorrect behaviour"

        // When
        val viewModel = presenter.formatErrorViewModel(errorMessage)

        // Then
        assertEquals("Произошла ошибка: $errorMessage", viewModel.errorMessage, "Incorrect error message")
        assertEquals(0, viewModel.fieldLength, "Incorrect length")
        assertEquals(0, viewModel.fieldWidth, "Incorrect width")
        assertEquals("", viewModel.fieldSizeDescription,
            "Incorrect field size description")
        assertEquals(0, viewModel.fieldObjects.size, "Incorrect number of field objects")

    }

    @Test
    fun `Format result with TabularFieldPresenter`(){
        // Given
        val length = 5
        val width = 1
        val position = Point(2, 0)
        val direction = Direction.EAST
        val presenter: IFieldPresenter = TabularFieldPresenter()
        val fieldState = IRoverUseCase.GetFieldStateResult(
            length,
            width,
            arrayOf(IRoverUseCase.RoverInfo(position, direction))
        )

        // When
        val viewModel = presenter.formatFieldViewModel(fieldState)

        // Then
        assertEquals(length, viewModel.fieldLength, "Incorrect length")
        assertEquals(width, viewModel.fieldWidth, "Incorrect width")
        assertEquals("Перед нами поле размером $length x $width ячеек", viewModel.fieldSizeDescription,
            "Incorrect field size description")
        assertEquals(1, viewModel.fieldObjects.size, "Incorrect number of field objects")
        assertEquals("[ ][ ][→][ ][ ]",
            viewModel.fieldObjects[0], "Incorrect description of field objects")
    }
}