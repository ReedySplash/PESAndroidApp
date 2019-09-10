package edu.pes.laresistencia.changepassword

import com.google.gson.annotations.SerializedName

data class ChangePasswordReq(
        @SerializedName("old_password") var old_password: String,
        @SerializedName("new_password") var new_password: String
)