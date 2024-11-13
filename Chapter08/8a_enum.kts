enum class Direction {
    NORTH, SOUTH, EAST, WEST;

    fun description(): String {
        return when (this) {
            NORTH -> "You are heading North."
            SOUTH -> "You are heading South."
            EAST -> "You are heading East."
            WEST -> "You are heading West."
        }
    }
}

println(Direction.NORTH.description()) // You are heading North.
println(Direction.SOUTH.description()) // You are heading South.