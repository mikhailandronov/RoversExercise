package com.ma.rovers.presenters

import com.ma.rovers.usecases.IRoverUseCase

interface IFieldPresenter {
    fun formatFieldViewModel(fieldState: IRoverUseCase.GetFieldStateResult): FieldViewModel
}

data class FieldViewModel(
    val fieldLength: Int,
    val fieldWidth: Int,
    val fieldSizeDescription: String,
    val fieldObjects: List<String>
)
