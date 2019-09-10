package edu.pes.laresistencia.profile

import com.google.gson.annotations.SerializedName

data class ChangePhotoReq(
        @SerializedName("photo") val photo: String
)
