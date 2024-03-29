import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun String.extractLongNumbers(): List<Long> = split(' ')
    .filter { it.isNotBlank() }
    .map(String::toLong)
    .toList()

fun String.extractIntNumbers(sep: Char = ' '): List<Int> = split(sep)
    .filter { it.isNotBlank() }
    .map(String::toInt)
    .toList()
