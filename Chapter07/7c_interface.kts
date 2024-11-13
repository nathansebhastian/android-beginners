// Interface example
interface Appliance {
    val brand: String
    fun turnOn()
}

class Microwave(override val brand: String): Appliance {
    override fun turnOn() {
        println("$brand oven is heating up.")
    }
}

// Implement multiple interfaces
interface Addition {
    fun addition(): Int
}

interface Subtraction {
    fun subtraction(): Int
}

class Calculate(
    var a: Int,
    var b: Int
): Addition, Subtraction {

    override fun addition(): Int {
        return a + b
    }

    override fun subtraction(): Int {
        return a - b
    }
}

var calc = Calculate(10, 3)

println(calc.addition())  // 13
println(calc.subtraction())  // 7