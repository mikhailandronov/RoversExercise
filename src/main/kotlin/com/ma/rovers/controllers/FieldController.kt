package com.ma.rovers.controllers

import com.ma.rovers.domain.Direction
import com.ma.rovers.domain.IntSize
import com.ma.rovers.domain.Point
import com.ma.rovers.presenters.FieldViewModel
import com.ma.rovers.presenters.IFieldPresenter
import com.ma.rovers.usecases.IRoverUseCase

class FieldController (val interactor: IRoverUseCase, val presenter: IFieldPresenter) {
    fun onNewFieldRequested(length: Int, width: Int): FieldViewModel{
        val useCaseResult = interactor.defineField(IntSize(length), IntSize(width))
        if (!useCaseResult.successful) return presenter.formatErrorViewModel(useCaseResult.errorMessage)

        val fieldState = interactor.getFieldState()
        if (!fieldState.successful) return presenter.formatErrorViewModel(fieldState.errorMessage)

        return presenter.formatFieldViewModel(fieldState)
    }

    fun onNewRoverPlacementRequested(position: Point, direction: Direction): FieldViewModel{
        val useCaseResult = interactor.putRoverToField(position, direction)
        if (!useCaseResult.successful) return presenter.formatErrorViewModel(useCaseResult.errorMessage)

        val fieldState = interactor.getFieldState()
        if (!fieldState.successful) return presenter.formatErrorViewModel(fieldState.errorMessage)

        return presenter.formatFieldViewModel(fieldState)
    }

    fun onRunRoverProgramRequested(position: Point, program: String): FieldViewModel{
        val useCaseResult = interactor.runRoverProgram(position, program)
        if (!useCaseResult.successful) return presenter.formatErrorViewModel(useCaseResult.errorMessage)

        val fieldState = interactor.getFieldState()
        if (!fieldState.successful) return presenter.formatErrorViewModel(fieldState.errorMessage)

        return presenter.formatFieldViewModel(fieldState)
    }
}