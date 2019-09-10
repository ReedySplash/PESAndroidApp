package edu.pes.laresistencia.register

import com.google.gson.annotations.SerializedName


enum class Gender {
    @SerializedName("MALE")
    MALE,
    @SerializedName("FEMALE")
    FEMALE,
    @SerializedName("OTHER")
    OTHER
}

data class UserRequest (
        val name: String,
        val surname: String,
        val gender: Gender,
        @SerializedName("birthDate") var birthDate: String,
        val email: String,
        val country: String,
        var password: String,
        @SerializedName("photo") var photo: String
)