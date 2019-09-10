package edu.pes.laresistencia.activity


data class ActivityRequest (
    val title: String,
    val date: String,
    val description: String,
    val id: String,
    val from: String,
    val to: String,
    val location: String
)

data class CommentsRequest (
        val id: String,
        val activity_id: String,
        val userEmail: String,
        val comment: String,
        val creationDate: String,
        val userName: String,
        val userSurname: String
)