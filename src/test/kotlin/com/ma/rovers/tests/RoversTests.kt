package com.ma.rovers.tests

import com.ma.rovers.common.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

import com.ma.rovers.exploration.*

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
                val theCell = field.cell(Point(x, y))
                cellsSet.add(theCell.hashCode())
                owningFieldsSet.add(theCell.ownerField)
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

        val bottomLeft = Point(0, 0)
        val topRight = Point(length-1, width-1)
        assertEquals(
            bottomLeft, field.cell(bottomLeft).coordinates,
            "Field bottom-left cell coordinates are not those that are expected"
        )
        assertEquals(
            topRight, field.cell(topRight).coordinates,
            "Field top-right cell coordinates are not those that are expected"
        )
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
        val coordinates = Point(2, 2)

        // When
        field.placeObject(rover, coordinates)

        // Then
        assertEquals(
            rover, field.cell(coordinates).locatedObject,
            "Located object is not the same that was placed"
        )

        assertTrue(
            field.cell(coordinates).locatedObject is Rover,
            "Located object is not Rover"
        )

        assertEquals(
            field.cell(coordinates), rover.location,
            "Rover location should reference the cell it was placed on"
        )

        assertEquals(
            field, rover.location?.ownerField,
            "Rover location should reference the same field it was placed on"
        )

        // When
        field.removeObject(coordinates)

        // Then
        assertEquals(
            null, field.cell(coordinates).locatedObject,
            "Located object should be null"
        )

        assertEquals(
            null, rover.location,
            "Rover location should be null after deletion"
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

        // When
        val coordinates = Point(2, 2)
        field.placeObject(rover, coordinates)

        // Then
        assertEquals(
            rover.location?.coordinates, coordinates,
            "Location coordinates are not those where rover was placed"
        )

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
        //Given
        val rover = Rover()

        //When
        rover.turnRight()

        //Then
        assertEquals(
            Direction.EAST, rover.cameraDirection,
            "Rover location should be EAST but now it is " + rover.cameraDirection
        )
        //When
        rover.turnLeft()
        rover.turnLeft()
        //Then
        assertEquals(
            Direction.WEST, rover.cameraDirection,
            "Rover location should be WEST but now it is " + rover.cameraDirection
        )
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