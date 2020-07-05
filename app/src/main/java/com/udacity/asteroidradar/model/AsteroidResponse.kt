package com.udacity.asteroidradar.model

import com.udacity.asteroidradar.db.AppDbEntity

data class AsteroidResponse(

    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean,
    val closeApproachDateMillis: Long
)

fun List<AsteroidResponse>.toListDatabaseModel() : List<AppDbEntity>  {

    return this.map {
        AppDbEntity(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous,
            closeApproachDateMillis = it.closeApproachDateMillis
        )
    }
}