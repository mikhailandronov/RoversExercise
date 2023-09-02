package com.ma.rovers.tests

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

import com.ma.rovers.common.IntSize
import com.ma.rovers.exploration.*
import com.ma.rovers.common.Direction
import com.ma.rovers.common.IField
import com.ma.rovers.common.IFieldObject
class RoversTests {
    @Test
    fun `Describe field`() { // Описать плато
        // Given
        val length = 10
        val width = 20

        // When
        val field: IField = Field(
            IntSize(length),
            IntSize(width)
        )

        val cellsSet = mutableSetOf<Int>()
        val owningFieldsSet = mutableSetOf<IField>()
        for (x in 0..<length)
            for (y in 0..<width) {
                cellsSet.add(field.cell(x, y).hashCode())
                owningFieldsSet.add(field.cell(x, y).ownerField)
            }

        // Then
        assertEquals(
            length, field.length.size,
            "Length of field should be " + length + " but current value is " + field.length.size
        )

        assertEquals(
            width, field.width.size,
            "Length of field should be " + width + " but current value is " + field.width.size
        )

        assertEquals(
            length * width, cellsSet.size,
            "Expected field to have " + length * width + " cells, but current number of cells is " + cellsSet.size
        )

        assertTrue(owningFieldsSet.size == 1, "All cells should belong to one field")

        assertTrue(owningFieldsSet.elementAt(0) == field, "All cells should belong to the owning field")

    }

    @Test
    fun `Place rover on the field and remove it`() { // Разместить марсоход на плато и удалить его
        // Given
        val length = 10
        val width = 20
        val field: IField = Field(
            IntSize(length),
            IntSize(width)
        )

        val rover:IFieldObject = Rover()
        val xPoint = 2
        val yPoint = 2

        // When
        field.placeObject(rover, xPoint, yPoint)

        // Then
        assertEquals(
            rover, field.cell(xPoint, yPoint).locatedObject,
            "Located object is not the same that was placed"
        )

        assertTrue(
            field.cell(xPoint, yPoint).locatedObject is Rover,
            "Located object is not Rover"
        )

        assertEquals(
            field, rover.location?.ownerField,
            "Rover location should reference the same field it was placed on"
        )

        // When
        field.removeObject(xPoint, yPoint)

        // Then
        assertEquals(
            null, field.cell(xPoint, yPoint).locatedObject,
            "Located object should be null"
        )

    }

    @Test
    fun `Get rover location on the field`() { // Узнать расположение марсохода
        // Given
        val length = 10
        val width = 20
        val field: IField = Field(
            IntSize(length),
            IntSize(width)
        )

        val rover:IFieldObject = Rover()
        val xPoint = 2
        val yPoint = 2

        field.placeObject(rover, xPoint, yPoint)

        // When


        // Then
//        assertEquals(
//            rover, field.cell(xPoint, yPoint).locatedObject,
//            "Located object is not the same that was placed"
//        )

    }

    @Test
    fun `Set and get camera direction`() { // Задать и узнать направление камеры
        //Given
        //When
        val rover = Rover()
        //Then
        assertEquals(
            Direction.NORTH, rover.cameraDirection,
            "Rover default location should be NORTH"
        )
        //When
        rover.setCameraDirection(Direction.SOUTH)
        //Then
        assertEquals(
            Direction.SOUTH, rover.cameraDirection,
            "Rover location should be SOUTH but now it is " + rover.cameraDirection
        )
    }

    @Test
    fun `Turn rover right or left`() { // Повернуть марсоход
        TODO("Not yet implemented")
    }

    @Test
    fun `Make one step forward`() { // Пройти шаг вперед
        TODO("Not yet implemented")
    }

    @Test
    fun `Execute exploration program`() { // Выполнить программу перемещения (исследования)
        TODO("Not yet implemented")
    }
}