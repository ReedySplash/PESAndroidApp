package edu.pes.laresistencia.network

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface FriendshipAPI {

    @GET()
    fun status(@Url email: String): Call<FriendshipResponse>

    @POST()
    fun settle(@Url email: String): Call<Void>

    @POST()
    fun request(@Url email: String): Call<Void>

    @GET("/friendship")
    fun friendships(): Call<List<FriendshipResponse>>

    @DELETE()
    fun delete(@Url email: String): Call<Void>
}

enum class FriendshipStatus {
    NONE,
    PENDING,
    TO_ACCEPT,
    SETTLED
}

data class StatusResponse(val status: FriendshipStatus)

data class FriendshipResponse(
        var name: String,
        var email: String,
        var status: FriendshipStatus,
        @SerializedName("lastMessage") var lastMessage: String,
        @SerializedName("nonReadMessage") var nonReadMessage: Boolean) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            FriendshipStatus.valueOf(parcel.readString()),
            parcel.readString(),
            parcel.readByte().compareTo(0) != 0)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(status.toString())
        parcel.writeString(lastMessage)
        parcel.writeByte(if (nonReadMessage) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FriendshipResponse> {
        override fun createFromParcel(parcel: Parcel): FriendshipResponse {
            return FriendshipResponse(parcel)
        }

        override fun newArray(size: Int): Array<FriendshipResponse?> {
            return arrayOfNulls(size)
        }
    }
}