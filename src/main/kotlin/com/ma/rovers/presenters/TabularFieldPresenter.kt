package com.ma.rovers.presenters

import com.ma.rovers.domain.Point
import com.ma.rovers.usecases.IRoverUseCase

class TabularFieldPresenter : IFieldPresenter {
    override fun formatFieldViewModel(fieldState: IRoverUseCase.GetFieldStateResult): FieldViewModel {
        val fieldObjectsDesc = mutableListOf<String>()
        for (y in 0..<fieldState.width) {
            var s = ""
            for (x in 0..<fieldState.length) {
                if (fieldState.rovers.isEmpty()) s += "[ ]"
                else {
                    var cellImage = ' '
                    fieldState.rovers.forEach {
                        if (it.coordinates == Point(x, y)) cellImage = it.direction.toString().first()
                    }
                    s += when(cellImage){
                        'N'->"[\u2191]" //стрелка вверх
                        'S'->"[\u2193]" //стрелка вниз
                        'W'->"[\u2190]" //стрелка влево
                        'E'->"[\u2192]" //стрелка вправо
                        else -> "[ ]"
                    }
                }
            }
            fieldObjectsDesc.add(0, s)
        }

        return FieldViewModel(
            fieldState.length, fieldState.width,
            "Перед нами поле размером ${fieldState.length} x ${fieldState.width} ячеек",
            fieldObjectsDesc.toList()
        )
    }

    override fun formatErrorViewModel(errorMessage: String): FieldViewModel {
        return FieldViewModel(
            0, 0, "", listOf(),
            "Произошла ошибка: $errorMessage"
        )
    }
}