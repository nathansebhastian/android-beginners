fun main() {
    // A function to fetch user data
    fun fetchUser(): String {
        println("Fetching user data...")

        Thread.sleep(3000)

        println("User fetched")
        return "Name is Nathan"
    }

    // A function to fetch news headlines
    fun fetchNews(): String {
        println("Fetching the latest news...")

        Thread.sleep(2000)

        println("News fetched")
        return "Android 15 is here!"
    }

    val totalTime = kotlin.system.measureTimeMillis {
        val userResult = fetchUser()
        val newsResult = fetchNews()

        println("User: $userResult")
        println("News: $newsResult")
    }

    println("Total runtime: ${totalTime / 1000.0} seconds")
}

