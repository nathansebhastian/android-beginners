// Classes

// Add open keyword to make class extendable
open class Vehicle (var vehicleType: String, var maker: String) {
    fun start(){
        println("Starting the engine..")
        println("$vehicleType is on")
    }

    fun drive(speed: Int){
        println("Driving $maker at $speed mph")
    }
}

// Class Inheritance
// The Car subclass extending the Vehicle superclass
class Car(
    vehicleType: String,
    maker: String,
    var transmission: String,
    var wheels: Int
) : Vehicle(vehicleType, maker) {

    fun getCarDetails() {
        println("Transmission: $transmission")
        println("Number of wheels: $wheels")
    }
}

var myCar = Car("Car", "Toyota", "MT", 4)

println(myCar.vehicleType)
myCar.start()
myCar.drive(40)
myCar.getCarDetails()
