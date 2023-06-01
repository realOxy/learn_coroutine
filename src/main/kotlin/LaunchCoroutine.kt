import kotlinx.coroutines.delay
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine

fun main() {
    launchCoroutine(ProducerScope()) {
        while (peek() < 10) {
            println(peek())
            plus(peek().coerceAtLeast(1))
        }
    }
}

class ProducerScope(initial: Int = 0) {
    private var _value: Int = initial
    fun peek(): Int = _value
    // runBlocking cannot be removed.
    suspend fun plus(value: Int) {
        _value += value
        delay(1000L)
    }
}

fun <T, R> launchCoroutine(receiver: T, block: suspend T.() -> R) {
    block.startCoroutine(receiver, object : Continuation<R> {
        override val context: CoroutineContext
            get() = EmptyCoroutineContext

        override fun resumeWith(result: Result<R>) {
            println("End")
        }
    })
}

// The delay function is used to pause the current coroutine for a certain amount of time.
// When the coroutine reaches the delay function, it suspends its execution and releases the thread resources,
// until the specified time has passed, and then resumes the coroutine's execution.
// In this example, since the coroutine scope is suspended, the subsequent code the main function is executed and completed,
// and the delay function doesn't have a chance to resume its execution.
// Therefore, the program terminates before the delay function finishes suspending.
// To avoid premature termination of the program, we need to use a coroutine builder (such as runBlocking) to create a new coroutine context,
// and call the suspend function within it. By doing so, we ensure that the coroutine executes in a context that can handle suspensions and delays,
// thereby ensuring proper program execution.
//
// Alternatively, you can add the following code after the main function, and you will notice that the program seems to execute as expected:
//
// Thread.sleep(5000)
