package com.ma.rovers.common

@JvmInline
value class IntSize(val size: Int){
    init {
        require(size > 0) {"Integer dimension size should be positive"}
    }
}

data class Point(val x: Int, val y: Int) {
    init {
        require(x >= 0) { "x must be non-negative" }
        require(y >= 0) { "y must be non-negative" }
    }
}
interface IField{
    val width: IntSize
    val length: IntSize

    fun cell(coordinates: Point): ICell
    fun placeObject(obj: IFieldObject, coordinates: Point)
    fun removeObject(coordinates: Point)
}
interface ICell{
    val ownerField: IField
    val coordinates: Point
    val locatedObject: IFieldObject?
}
interface IFieldObject{
    val location: ICell?
}

enum class Direction {
    NORTH, EAST, SOUTH, WEST
}
interface IRover: IFieldObject{
    val cameraDirection: Direction
    fun turnRight()
    fun turnLeft()
    fun moveForward()
}
