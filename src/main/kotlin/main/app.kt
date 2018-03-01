package main

import java.io.File
import kotlin.math.abs

fun main(args: Array<String>) {
    listOf("a_example",
            "b_should_be_easy",
            "c_no_hurry",
            "d_metropolis",
            "e_high_bonus")
            .forEach { processFile(it) }
}

fun processFile(fileName: String) {


    println("processing file: $fileName")

    val city = parseFile("./inputs/$fileName.in")
    val solution = hashMapOf<Int, MutableList<Int>>()

    println("number of steps: ${city.stepCtn}")

    with(city) {
        while (step < stepCtn) {
            println("current step: $step")
            cars.forEachIndexed { carIndex, car ->
                rides.forEachIndexed { rideIndex, ride ->
                    if (isStartable(car, ride, step, stepCtn)) {
                        car.currentRide = ride
                        ride.isTaken = true
                        if (solution.containsKey(carIndex)) {
                            solution[carIndex]!!.add(rideIndex)
                        } else {
                            solution[carIndex] = mutableListOf(rideIndex)
                        }
                    }
                }
                tick(car)
            }
            step += 1
        }
    }
    solutionToFile(solution, "./outputs/$fileName.out")
}

fun solutionToFile(solution: HashMap<Int, MutableList<Int>>, filePath: String) {
    File(filePath).printWriter().use { out ->
        solution.forEach { entry ->
            out.print(entry.value.size)
            entry.value.forEach { value ->
                out.print(" ")
                out.print(value)
            }
            out.print("\n")
        }
    }
}

fun distance(a: Int, b: Int, x: Int, y: Int): Int {
    return abs(a - x) + abs(b - y)
}

fun distanceFromStart(car: Car, ride: Ride): Int {
    return distance(car.x, car.y, ride.startX, ride.startY)
}

fun totalDistance(car: Car, ride: Ride): Int {
    return distanceFromStart(car, ride) + ride.distance
}

fun isStartable(car: Car, ride: Ride, step: Int, stepCtn: Int): Boolean {
    if (car.currentRide != null) {
        return false
    }
    if (ride.isTaken) {
        return false
    }
    if (totalDistance(car, ride) > stepCtn - step) {
        return false
    }
    if (ride.earliestStart > distanceFromStart(car, ride) + step) {
        return false
    }
    if (ride.latestFinish < totalDistance(car, ride) + step) {
        return false
    }
    return true
}

fun tick(car: Car) {
    with(car) {
        if (currentRide != null) {
            when {
                x < currentRide!!.endX -> x += 1
                x > currentRide!!.endX -> x -= 1
                y < currentRide!!.endY -> y += 1
                y > currentRide!!.endY -> y -= 1
                else -> currentRide = null
            }
        }
    }
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
                val stepCtn: Int,
                val rides: List<Ride>,
                var step: Int = 0,
                val cars: List<Car> = List(carCtn, { Car(0, 0, null) }))

data class Ride(val startX: Int,
                val startY: Int,
                val endX: Int,
                val endY: Int,
                val earliestStart: Int,
                val latestFinish: Int,
                val distance: Int = distance(startX, startY, endX, endY),
                var isTaken: Boolean = false)

data class Car(var x: Int,
               var y: Int,
               var currentRide: Ride?)
