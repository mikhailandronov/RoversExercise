package com.ma.rovers.tests

import com.ma.rovers.common.*
import com.ma.rovers.exploration.Field
import com.ma.rovers.exploration.Rover
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


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
        val rover2= Rover()
        // Then
        assertThrows<IllegalArgumentException>("Cell is busy: exception should be thrown") {field.placeObject(rover2, coordinates)}


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
            coordinates, rover.location?.coordinates,
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
        // Given
        val length = 10
        val width = 20
        val field: IField = Field(
            IntSize(length),
            IntSize(width)
        )

        val rover = Rover()
        val coordinates = Point(2, 2)
        field.placeObject(rover, coordinates)

        // When
        rover.moveForward()

        // Then
        assertEquals(
            Point(2, 3), rover.location?.coordinates,
            "Location coordinates are not those expected"
        )

        // When
        repeat(width) {rover.moveForward()}

        // Then
        assertEquals(
            Point(2, width-1), rover.location?.coordinates,
            "Location coordinates are not those expected"
        )

        // When
        rover.turnLeft()
        repeat(length) {rover.moveForward()}

        // Then
        assertEquals(
            Point(0, width-1), rover.location?.coordinates,
            "Location coordinates are not those expected"
        )
    }

    @Test
    fun `Execute exploration program`() { // Выполнить программу перемещения (исследования)
        // Given
        val length = 6
        val width = 6
        val field: IField = Field(
            IntSize(length),
            IntSize(width)
        )

        val rover1 = Rover() // Direction = NORTH
        field.placeObject(rover1, Point(1, 2))

        // When
        val program0 = "LMLMLZMLMM"
        // Then
        assertThrows<IllegalArgumentException>("Incorrect program: exception should be thrown") {rover1.executeProgram(program0)}

        // When
        val program1 = "LMLMLMLMM"
        rover1.executeProgram(program1)
        // Then
        assertEquals(
            Point(1,3), rover1.location?.coordinates,
            "Final location is not what expected"
        )
        assertEquals(
            Direction.NORTH, rover1.cameraDirection,
            "Final camera direction is not what expected"
        )

        // When
        val rover2 = Rover()
        rover2.setCameraDirection(Direction.EAST)
        field.placeObject(rover2, Point(3, 3))
        val program2 = "MMRMMRMRRM"
        rover2.executeProgram(program2)
        // Then
        assertEquals(
            Point(5,1), rover2.location?.coordinates,
            "Final location is not what expected"
        )
        assertEquals(
            Direction.EAST, rover2.cameraDirection,
            "Final camera direction is not what expected"
        )
    }
}