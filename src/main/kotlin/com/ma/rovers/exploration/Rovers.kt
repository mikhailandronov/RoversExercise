package com.ma.rovers.exploration

import com.ma.rovers.common.*

class Field (length: IntSize, width: IntSize): IField{
    private val cells: Array<Array<ICell>> = Array(length.size){Array(width.size){Cell(this)} }
    override val length: IntSize
        get() = IntSize(cells.size)
    override val width: IntSize
        get() =
            if (cells.isNotEmpty())
                IntSize(cells[0].size)
            else
                IntSize(0)

    override fun cell(x: Int, y: Int): ICell {
        if ((x !in 0..< length.size) || (y !in 0..< width.size))
            throw IndexOutOfBoundsException("Cell index [$x, $y] is out of bound")
        return cells[x][y]
    }

    override fun placeObject(obj: IFieldObject, x: Int, y: Int) {
        //cell(x, y).placeObject(obj)
        if ((x !in 0..< length.size) || (y !in 0..< width.size))
            throw IndexOutOfBoundsException("Cell index [$x, $y] is out of bound")
        (cells[x][y] as Cell).locatedObj = obj
        (obj as FieldObject).cell = cells[x][y]
    }

    override fun removeObject(x: Int, y: Int) {
        //cell(x, y).removeObject()
        if ((x !in 0..< length.size) || (y !in 0..< width.size))
            throw IndexOutOfBoundsException("Cell index [$x, $y] is out of bound")
        (cells[x][y] as Cell).locatedObj = null

    }

}

class Cell(private val owner: IField): ICell{
    override val ownerField: IField
        get() = owner

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
}