// String Types
// Char
var myChar = 'A'

// String
var myString = "ABC"

var myName = "Nathan"

var greeting = "Hello, $myName!"

println(greeting) // Hello, Nathan!

var myScore = 8

var message = "Your score is $myScore"

println(message) // Your score is 8

// Number Types
var myInt = 7

var myFloat = 1.05F

var myDouble = 1.000000000009

// Boolean Type
var isCompleted = true

isCompleted = false

// Any Type
var username: Any = "Nathan"

username = 5

// Nullable Type
var secretString: String? = null

println(secretString) // null

secretString = "SupErSecRet"

println(secretString) // SupErSecRet

// Operators
var myText = "Good " + "Morning!"
println(myText) // Good Morning!

var myNumber = 10 + 5
println(myNumber) // 15

// 1. Addition (+)
// Returns the sum between the two operands
println(5 + 2) // 7

// 2. Subtraction (-)
// Returns the difference between the two operands
println(5 - 2) // 3

// 3. Multiplication (*) operator
// Returns the multiplication between the two operands
println(5 * 2) // 10

// 4. Division operator
// Returns the sum between the two operands
println(5 / 2) // 2

// 5. Remainder operator
// Returns the remainder of the left operand after being divided by the right operand
println(5 % 2) // 1

var combineString = "Hello " + "human!"
println(combineString) // Hello human!

var stringAndNumber = "Hi " + 89
println(stringAndNumber)

println("ABC" == "ABC") // true

println("ABC" == "abc") // false

println("Z" == "A") // false