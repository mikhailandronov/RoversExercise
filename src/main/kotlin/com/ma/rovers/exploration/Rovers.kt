package com.ma.rovers.exploration

import com.ma.rovers.common.*

class Field (length: IntSize, width: IntSize): IField{
    private val cells: Array<Array<ICell>> = Array(length.size){Array(width.size){Cell(this)} }
    init {
        for (x in 0..<length.size)
            for (y in 0..<width.size) {
                val theCell = cells[x][y]
                (theCell as Cell).coords = Point(x, y)
            }
    }

    override val length: IntSize
        get() = IntSize(cells.size)
    override val width: IntSize
        get() =
            if (cells.isNotEmpty())
                IntSize(cells[0].size)
            else
                IntSize(0)

    override fun cell(coordinates: Point): ICell {
        if ((coordinates.x !in 0..< length.size) ||
            (coordinates.y !in 0..< width.size))
            throw IndexOutOfBoundsException("Cell index [${coordinates.x}, ${coordinates.y}] is out of bound")
        return cells[coordinates.x][coordinates.y]
    }

    override fun placeObject(obj: IFieldObject, coordinates: Point) {
        if ((coordinates.x !in 0..< length.size) ||
            (coordinates.y !in 0..< width.size))
            throw IndexOutOfBoundsException("Cell index [${coordinates.x}, ${coordinates.y}] is out of bound")
        (cells[coordinates.x][coordinates.y] as Cell).locatedObj = obj
        (obj as FieldObject).cell = cells[coordinates.x][coordinates.y]
    }

    override fun removeObject(coordinates: Point) {
        if ((coordinates.x !in 0..< length.size) ||
            (coordinates.y !in 0..< width.size))
            throw IndexOutOfBoundsException("Cell index [${coordinates.x}, ${coordinates.y}] is out of bound")
        ((cells[coordinates.x][coordinates.y] as Cell).locatedObj as FieldObject).cell = null
        (cells[coordinates.x][coordinates.y] as Cell).locatedObj = null
    }

}

class Cell(private val owner: IField): ICell{
    override val ownerField: IField
        get() = owner

    internal lateinit var coords: Point
    override val coordinates: Point
        get() = coords

    internal var locatedObj: IFieldObject? = null

    override val locatedObject: IFieldObject?
        get() = locatedObj

}

open class FieldObject() : IFieldObject{
    internal var cell: ICell? = null
    override val location: ICell?
        get() = cell
}

class Rover(direction: Direction = Direction.NORTH): FieldObject(), IRover{
    private lateinit var camDirection:Direction
    init {
        setCameraDirection (direction)
    }
    fun setCameraDirection(direction: Direction){
        camDirection = direction
    }
    override val cameraDirection: Direction
        get() = camDirection

    private fun turn(right: Boolean){
        val currentIndex = Direction.entries.indexOf(cameraDirection)
        val newIndex = currentIndex + (if (right) 1 else -1)
        val newDirection = when (newIndex){
            -1 -> Direction.entries.last()
            Direction.entries.size -> Direction.entries.first()
            else -> Direction.entries[newIndex]
        }
        setCameraDirection(newDirection)
    }
    override fun turnRight() {
        turn(true)
    }

    override fun turnLeft() {
        turn(false)
    }

    override fun moveForward() {
        if (location == null) return
        val currentLocation = location
        val currentCoordinates = currentLocation!!.coordinates
        val newX = when (cameraDirection){
            Direction.EAST -> currentCoordinates.x+1
            Direction.WEST -> currentCoordinates.x-1
            else -> currentCoordinates.x
        }
        val newY = when (cameraDirection){
            Direction.NORTH -> currentCoordinates.y+1
            Direction.SOUTH -> currentCoordinates.y-1
            else -> currentCoordinates.y
        }

        currentLocation.ownerField.removeObject(currentCoordinates)
        currentLocation.ownerField.placeObject(this, Point(newX, newY))
    }
}