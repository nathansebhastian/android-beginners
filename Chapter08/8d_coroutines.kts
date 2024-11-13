import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

fun main () {
    // A suspend function to fetch user data asynchronously
    suspend fun fetchUser(): String {
        println("Fetching user data...")
        delay(3000)
        println("User fetched")
        return "Name is Nathan"
    }

    // A suspend function to fetch news headlines asynchronously
    suspend fun fetchNews(): String {
        println("Fetching the latest news...")
        delay(2000)
        println("News fetched")
        return "Android 15 is here!"
    }

    runBlocking {
        val totalTime = measureTimeMillis {
            // Launch async coroutines for parallel execution
            val userDeferred = async { fetchUser() }
            val newsDeferred = async { fetchNews() }

            // Await results
            val userResult = userDeferred.await()
            val newsResult = newsDeferred.await()

            // Print results
            println("User: $userResult")
            println("News: $newsResult")
        }
        println("Total runtime: ${totalTime / 1000.0} seconds")
    }
}

