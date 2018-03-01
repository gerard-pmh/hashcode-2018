import java.io.File

fun main(args: Array<String>) {
}

fun parseFile(filePath: String): City {

    val lines = File(filePath).readLines()

    val header = lines[0].split(" ").map { it.toInt() }

    val rides = lines.drop(1)
            .map { it.split(" ").map { it.toInt() } }
            .map { Ride(it[0], it[1], it[2], it[3], it[4], it[5]) }

    return City(header[0],
            header[1],
            header[2],
            header[3],
            header[4],
            header[5],
            rides)
}

data class City(val rowCtn: Int,
                val colCtn: Int,
                val carCtn: Int,
                val rideCtn: Int,
                val bonusCtn: Int,
                val setCtn: Int,
                val rides: List<Ride>)

data class Ride(val startX: Int,
                val startY: Int,
                val endX: Int,
                val endY: Int,
                val earliestStart: Int,
                val latestFinish: Int)