import java.io.File
import java.util.concurrent.ConcurrentHashMap

fun main() {
    val primes = loadPrimes() // could have used a primality test
    println("The amount of primes loaded: ${primes.size}")

    val abPairs = abPairs()
    println("The amount of A, B pairs generated: ${abPairs.size}")

    eliminateAbPairs(abPairs, primes)

    println("The amount of A, B pairs left: ${abPairs.size}")
    val abPair = abPairs.keys.toList()[0]
    println("The A, B pair left: $abPair")
    println("The product: ${abPair.first * abPair.second}")
}

private fun eliminateAbPairs(
    abPairs: ConcurrentHashMap<Pair<Int, Int>, Unit>,
    primes: HashSet<Int>
) {
    var n = 0
    do {
        println("Testing with n = $n")
        abPairs.keys.forEach { pair ->
            run {
                if (!isProbablyPrime(primes, quadraticFormula(n, pair.first, pair.second))) {
                    abPairs.remove(pair)
                }
            }
        }
        n++
    } while (abPairs.keys.size > 1)
}

private fun isProbablyPrime(primes: HashSet<Int>, underTest: Int)
    = primes.contains(underTest)

@OptIn(ExperimentalUnsignedTypes::class)
private fun abPairs(): ConcurrentHashMap<Pair<Int, Int>, Unit> {
    val abPairs = ConcurrentHashMap<Pair<Int, Int>, Unit>()
    val aValues = (-999..999)
    val bValues = (-1_000..1_000)
    aValues.forEach { a -> bValues.forEach { b -> abPairs[Pair(a, b)] = Unit } }
    return abPairs
}

private fun loadPrimes(): HashSet<Int> {
    val primes = HashSet<Int>(50_000_000)
    (0..4).forEach { i, ->
        readFileAsLinesUsingBufferedReader("./50_million_primes_${i}.csv")
            .stream()
            .forEach { line ->
                line.split(",")
                    .stream()
                    .forEach { number -> primes.add(number.toInt()) }
            }
    }
    return primes
}

private fun quadraticFormula(n: Int, a: Int, b: Int)
    = n*n + a*n + b

private fun readFileAsLinesUsingBufferedReader(fileName: String): List<String>
    = File(fileName).bufferedReader().readLines()
