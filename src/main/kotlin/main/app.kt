package main

import java.io.File
import kotlin.math.abs

fun main(args: Array<String>) {

    val city = parseFile("./assets/b_should_be_easy.in")
    val cars = List(city.carCtn, { Car(0, 0, null) })
    val cityState = CityState(0, cars)

    val solution = hashMapOf<Int, MutableList<Int>>()

    while (cityState.step < city.stepCtn) {
        cityState.cars.forEachIndexed { carIndex, car ->
            city.rides.forEachIndexed { rideIndex, ride ->
                if (isStartable(car, ride, cityState.step, city.stepCtn)) {
                    car.ride = ride
                    ride.isTaken = true

                    if (solution.containsKey(carIndex)) {
                        solution.get(carIndex)!!.add(rideIndex)
                    } else {
                        solution.set(carIndex, mutableListOf(rideIndex))
                    }
                }
            }
            tick(car)
        }
        cityState.step += 1
    }
    printSolution(solution)
}

fun printSolution(solution: HashMap<Int, MutableList<Int>>) {
    solution.forEach { entry ->
        print(entry.value.size)
        entry.value.forEach { value ->
            print(" ")
            print(value)
        }
        print("\n")
    }
}

fun distance(a: Int, b: Int, x: Int, y: Int): Int {
    return abs(a - x) + abs(b - y)
}

fun distanceFromStart(car: Car, ride: Ride): Int {
    return distance(car.x, car.y, ride.startX, ride.startY)
}

fun rideDistance(ride: Ride): Int {
    return distance(ride.startX, ride.startY, ride.endX, ride.endY)
}

fun totalDistance(car: Car, ride: Ride): Int {
    return distanceFromStart(car, ride) + rideDistance(ride)
}

fun isStartable(car: Car, ride: Ride, step: Int, stepCtn: Int): Boolean {
    if (car.ride != null) {
        return false
    }
    if (ride.isTaken) {
        return false
    }
    val remainingSteps = stepCtn - step
    if (totalDistance(car, ride) > remainingSteps) {
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
    if (car.ride != null) {
        when {
            car.x < car.ride!!.endX -> car.x += 1
            car.x > car.ride!!.endX -> car.x -= 1
            car.y < car.ride!!.endY -> car.y += 1
            car.y > car.ride!!.endY -> car.y -= 1
            else -> car.ride = null
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
                val rides: List<Ride>)

data class Ride(val startX: Int,
                val startY: Int,
                val endX: Int,
                val endY: Int,
                val earliestStart: Int,
                val latestFinish: Int,
                var isTaken: Boolean = false)

data class CityState(var step: Int,
                     val cars: List<Car>)

data class Car(var x: Int,
               var y: Int,
               var ride: Ride?)
