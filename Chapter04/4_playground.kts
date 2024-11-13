// List Collection
var numbers = mutableListOf(11, 22, "String")

// Set Collection
var mySet = mutableSetOf("John", "Doe")

for (value in mySet) {
    println(value)
}

mySet.forEach { value ->
    println(value)
}

// Map Collection
var myMap = mutableMapOf("firstName" to "John", "lastName" to "Doe")

for ((key, value) in myMap) {
    println("$key = $value")
}

myMap.forEach { (key, value) ->
    println("$key = $value")
}
