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

    fun cell(x: Int, y: Int): ICell
    fun placeObject(obj: IFieldObject, x: Int, y: Int)
    fun removeObject(x: Int, y: Int)
}
interface ICell{
    val ownerField: IField
    val locatedObject: IFieldObject?
//    fun placeObject(obj: IFieldObject)
//    fun removeObject()
}
interface IFieldObject{
    val location: ICell?
}

enum class Direction {
    NORTH, SOUTH, WEST, EAST
}
interface IRover: IFieldObject{
    val cameraDirection: Direction
}
