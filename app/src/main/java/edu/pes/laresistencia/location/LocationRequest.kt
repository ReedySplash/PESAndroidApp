package edu.pes.laresistencia.location


data class Location(
        val kind: String,
        val name: String,
        val latitude: Double,
        val longitude: Double,
        val description: String,
        val address: String
)