package main

import java.io.File

fun main(args: Array<String>) {

    val city = parseFile("./assets/a_example.in")
    val cars = List(city.carCtn, { Car(0, 0, 0, 0) })
    val cityState = CityState(0, cars, city.rides.toMutableList())

    while (cityState.step < city.stepCtn) {
        cityState.step += 1
        cityState.cars.forEach { it.x = it.nextX; it.y = it.nextY }
    }

}


//fun findNearestRide(car: Car, rides: List<Ride>): Ride {
//    rides.minBy {  }
//}

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
                val stepCtn: Int,
                val rides: List<Ride>)

data class Ride(val startX: Int,
                val startY: Int,
                val endX: Int,
                val endY: Int,
                val earliestStart: Int,
                val latestFinish: Int)

data class CityState(var step: Int,
                     val cars: List<Car>,
                     val availableRides: MutableList<Ride>)

data class Car(var x: Int,
               var y: Int,
               var nextX: Int,
               var nextY: Int)