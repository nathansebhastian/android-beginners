import kotlin.math.round

// if/else Conditional
var balance = 4000

if (balance > 5000) {
    println("You have the money for this trip. Let's go!")
} else if (balance > 3000) {
    println("You only have enough money for a staycation")
} else {
    println("Sorry, not enough money. Save more!")
}
println("The end!")

// when Conditional
var age = 15
when (age) {
    10 -> println("Age is 10")
    20 -> println("Age is 20")
    else -> println("Age is neither 10 nor 20")
}

val weekdayNumber = 1

when (weekdayNumber) {
    0 -> println("Sunday")
    1 -> println("Monday")
    2 -> println("Tuesday")
    3 -> println("Wednesday")
    4 -> println("Thursday")
    5 -> println("Friday")
    6 -> println("Saturday")
    else -> println("The weekday number is invalid")
}

// for Loop
for (number in 1..10) {
    println(number)
}

// while Loop
var flips = 0
var isHeads = false

while (isHeads == false) {
    flips += 1
    isHeads = round(Math.random()).toInt() == 1
}

println("It took $flips flips to land on heads.")