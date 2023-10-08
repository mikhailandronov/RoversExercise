package com.ma.rovers.config

import com.ma.rovers.controllers.FieldController
import com.ma.rovers.presenters.IFieldPresenter
import com.ma.rovers.presenters.SimpleFieldPresenter
import com.ma.rovers.repositories.IFieldsRepository
import com.ma.rovers.repositories.InMemoryFieldsRepository
import com.ma.rovers.ui.ConsoleUI
import com.ma.rovers.ui.IUIRunner
import com.ma.rovers.usecases.IRoverUseCase
import com.ma.rovers.usecases.RoverUseCase

fun main(args: Array<String>) {
    val repository: IFieldsRepository = InMemoryFieldsRepository()
    val interactor: IRoverUseCase = RoverUseCase(repository)
    val presenter: IFieldPresenter = SimpleFieldPresenter()
    val controller = FieldController(interactor, presenter)
    val uiRunner: IUIRunner = ConsoleUI(controller)
    uiRunner.run()
}