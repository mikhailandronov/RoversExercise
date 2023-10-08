package com.ma.rovers.presenters

import com.ma.rovers.usecases.IRoverUseCase

class SimpleFieldPresenter: IFieldPresenter {
    override fun formatFieldViewModel(fieldState: IRoverUseCase.GetFieldStateResult): FieldViewModel {
        val fieldObjectsDesc = mutableListOf<String>()
        fieldState.rovers.forEach {
            fieldObjectsDesc.add("На ячейке [${it.coordinates.x}, ${it.coordinates.y}] находится марсоход; " +
                    "направление камеры - ${it.direction})") }
        return FieldViewModel(fieldState.length, fieldState.width,
            "Перед нами поле размером ${fieldState.length} x ${fieldState.width} ячеек",
            fieldObjectsDesc.toList())
    }

    override fun formatErrorViewModel(errorMessage: String): FieldViewModel {
        return FieldViewModel(0, 0, "", listOf(), errorMessage)
    }
}