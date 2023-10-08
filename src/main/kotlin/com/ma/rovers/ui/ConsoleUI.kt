package com.ma.rovers.ui

import com.ma.rovers.controllers.FieldController
import com.ma.rovers.domain.Direction
import com.ma.rovers.domain.Point
import com.ma.rovers.presenters.FieldViewModel

class ConsoleUI (val controller: FieldController): IUIRunner {
    override fun run() {
        var command = getMenuSelection()
        while (command != MenuOptions.Exit){
            val result: FieldViewModel? = when (command){
                MenuOptions.DefineField -> {
                    val fieldDimensions = requestFieldSize()
                    if (fieldDimensions != null)
                        controller.onNewFieldRequested(fieldDimensions[0], fieldDimensions[1])
                    else null
                }
                MenuOptions.PlaceRover -> {
                    val roverParams = requestRoverParams()
                    if (roverParams != null)
                        controller.onNewRoverPlacementRequested(
                            Point(
                                roverParams[0].toInt(),
                                roverParams[1].toInt()
                            ),
                            Direction.valueOf(roverParams[2]))
                    else null
                }
                MenuOptions.RunProgram -> {
                    val roverProgramParams = requestRoverProgramParams()
                    if (roverProgramParams != null)
                        controller.onRunRoverProgramRequested(
                            Point(
                                roverProgramParams[0].toInt(),
                                roverProgramParams[1].toInt()
                            ),
                            roverProgramParams[2])
                    else null
                }
                else -> null
            }

            displayField(result)
            command = getMenuSelection()
        }
    }

    private fun requestRoverParams(): Array<String>? {
        var input = ""
        while (!input.trim().matches(Regex("([0-9]+\\s+[0-9]+\\s+[NSEW])|-"))) {
            println("Введите через пробел координаты и направление камеры марсохода [N, S, E, W], либо '-' для возврата в меню:")
            input = readln()
        }
        if (input.trim() == "-") return null
        else {
            val values = input.split(regex = Regex("\\s")).toTypedArray()
            val dir = when (values[2]){
                "N"->Direction.NORTH.toString()
                "S"->Direction.SOUTH.toString()
                "W"->Direction.WEST.toString()
                "E"->Direction.EAST.toString()
                else -> ""
            }
            return arrayOf(values[0], values[1], dir)
        }
    }

    private fun requestRoverProgramParams(): Array<String>? {
        var input = ""
        while (!input.trim().matches(Regex("([0-9]+\\s+[0-9]+\\s+[MRL]+)|-"))) {
            println("Введите через пробел координаты марсохода и программу, либо '-' для возврата в меню:")
            input = readln()
        }
        if (input.trim() == "-")
            return null
        else {
            val values = input.split(regex = Regex("\\s")).toTypedArray()
            return arrayOf(values[0], values[1], values[2])
        }
    }

    private fun requestFieldSize(): Array<Int>? {
        var input = ""
        while (!input.trim().matches(Regex("([0-9]+\\s+[0-9]+)|-"))) {
            println("Введите через пробел длину и ширину участка плато, либо '-' для возврата в меню:")
            input = readln()
        }
        if (input.trim() == "-") return null
        else {
            val values = input.split(regex = Regex("\\s")).toTypedArray()
            return arrayOf(values[0].toInt(), values[1].toInt())
        }
    }

    private fun displayField(result: FieldViewModel?) {
        if (result == null) return
        if (result.errorMessage != ""){
            println(result.errorMessage)
            return
        }
        println(result.fieldSizeDescription)
        result.fieldObjects.forEach(){ println(it) }
    }

    enum class MenuOptions{
        DefineField, PlaceRover, RunProgram, Exit
    }
    private fun getMenuSelection(): MenuOptions{
        var selection = ""
        while (!selection.matches(Regex("[0123]"))) {
            println("[1] задать поле, [2] запустить марсоход, [3] передать программу на марсоход, [0] выход из программы")
            selection = readln()
        }
        return when (selection){
            "1" -> MenuOptions.DefineField
            "2" -> MenuOptions.PlaceRover
            "3" -> MenuOptions.RunProgram
            else -> MenuOptions.Exit
        }
    }
}