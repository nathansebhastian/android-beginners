// Creating an abstract class
abstract class Appliance(val brand: String) {

    // Abstract function - each appliance type has its own way to turn on
    abstract fun turnOn()

    // Concrete function - a shared way to turn off any appliance
    open fun turnOff() {
        println("Turning off the $brand appliance.")
    }
}

// Extending an abstract class
class Microwave(brand: String) : Appliance(brand) {

    override fun turnOn() {
        println("Turning on the $brand microwave.")
    }
}

val microwave = Microwave("LG")

microwave.turnOn() // Turning on the LG microwave.
microwave.turnOff() // Turning off the LG appliance.
