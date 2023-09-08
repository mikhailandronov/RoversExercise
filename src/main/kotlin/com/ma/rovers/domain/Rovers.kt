package com.ma.rovers.domain

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

        var newX = when (cameraDirection){
            Direction.EAST -> currentCoordinates.x+1
            Direction.WEST -> currentCoordinates.x-1
            else -> currentCoordinates.x
        }

        var newY = when (cameraDirection){
            Direction.NORTH -> currentCoordinates.y+1
            Direction.SOUTH -> currentCoordinates.y-1
            else -> currentCoordinates.y
        }

        if (newX < 0 || newX >= currentLocation.ownerField.length.size) newX = currentCoordinates.x
        if (newY < 0 || newY >= currentLocation.ownerField.width.size) newY = currentCoordinates.y

        currentLocation.ownerField.removeObject(currentCoordinates)
        currentLocation.ownerField.placeObject(this, Point(newX, newY))
    }

    override fun executeProgram(program: String) {
        val regex = Regex(pattern = "^[LRM]+$")
        if (!regex.matches(program))
            throw IllegalArgumentException("Only L, R, M are allowed as program steps symbols")

        for (step in program)
            when (step){
                'L' -> turnLeft()
                'R' -> turnRight()
                'M' -> moveForward()
            }
    }
}