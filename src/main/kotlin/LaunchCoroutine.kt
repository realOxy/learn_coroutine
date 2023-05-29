import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
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
    suspend fun plus(value: Int) = runBlocking {
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

