package edu.pes.laresistencia.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import com.example.api.laresistencia.R
import edu.pes.laresistencia.storage.InternalStorage
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import edu.pes.laresistencia.injection.component.DaggerProfileInjector
import edu.pes.laresistencia.injection.modules.AppModule
import edu.pes.laresistencia.injection.modules.NetworkModule
import edu.pes.laresistencia.model.User
import edu.pes.laresistencia.network.AllANDDisRequest
import edu.pes.laresistencia.network.HealthProfileAPI
import edu.pes.laresistencia.network.UserAPI
import edu.pes.laresistencia.service.HealthProfileService

import edu.pes.laresistencia.service.ProfileService
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class ProfilePresenter (val context: Context, val view: IProfileView):
        ProfileService.OnChangeProfileCompleted {

    @Inject
    lateinit var userAPI: UserAPI
    private val PICK_IMAGE: Int = 1
    private var internalStorage: InternalStorage
    private val profileService: ProfileService

    @Inject
    lateinit var healthProfileAPI: HealthProfileAPI
    private val healthProfileService: HealthProfileService
    private val allANDDisRequest: AllANDDisRequest?

    init {
        val injector = DaggerProfileInjector
                .builder()
                .appModule(AppModule(context))
                .networkModule(NetworkModule)
                .build()
        injector.inject(this)
        internalStorage = InternalStorage()

        profileService = ProfileService(this, userAPI)
        healthProfileService = HealthProfileService(healthProfileAPI)
        allANDDisRequest = healthProfileService.getAllANDDis(internalStorage.getUser(context)!![4])

    }

    interface OnChangedProfilePicture
    {
        fun reloadPicture()
    }

    fun pictureButtonPressed() {
        val galleryIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        ActivityCompat.startActivityForResult(
                context as Activity,
                galleryIntent,
                PICK_IMAGE,
                null
        )
    }

    fun initProfile() {
        val profile: ArrayList<String>  = User.getData()

        view.initHeader(profile[0] + " " + profile[1], profile[4])
        view.setProfilePictureFromUrl("${NetworkModule.baseUrl}/user/${User.email}/photo")
    }

    fun uploadProfilePicture(image: Bitmap?) {
        val bos = ByteArrayOutputStream()
        image?.compress(Bitmap.CompressFormat.WEBP, 100, bos)
        profileService.changePhoto(NetworkModule.authenticatorInterceptor.authToken!!,
                Base64.encodeToString(bos.toByteArray(), Base64.NO_WRAP),
                User.email!!)
        view.showProgressDialog(context.resources.getString(R.string.upload_profile_picture))
    }


    fun getInfoUser(): Bundle {
        val data: Bundle = Bundle()
        data.putStringArrayList("allergies", getAllergies() as java.util.ArrayList<String>?)
        data.putStringArrayList("diseases", getDiseases() as java.util.ArrayList<String>?)
        data.putString("weight", getWeight())
        data.putString("height", getHeight())
        data.putString("bloodtype", getBloodType())
        return data
    }

    override fun changePhotoCompleted(code: Int?)
    {
        if (code != null && handleResponse(code))
        {
            (context as OnChangedProfilePicture).reloadPicture()
            view.closeDialog()
        }
        else
            view.setProfilePictureFromUrl("${NetworkModule.baseUrl}/user/${User.email}/photo")
    }

    private fun handleResponse(response: Int): Boolean
    {
        when (response) {
            in 200..299 -> return true
            900 -> view.showToast(context.getString(R.string.error_internal))
            else -> view.showToast(context.getString(R.string.profile_errorSaving))
        }
        view.closeDialog()
        return false
    }

    fun stoping()
    {
        profileService.cancel()
        view.closeDialog()
    }

    private fun getWeight(): String? {
        if (allANDDisRequest != null) {
            return allANDDisRequest.weight
        }
        return null
    }

    private fun getHeight(): String? {
        if (allANDDisRequest != null) {
            return allANDDisRequest.height
        }
        return null
    }

    private fun getBloodType(): String? {
        if (allANDDisRequest != null) {
            return allANDDisRequest.bloodtype
        }
        return null
    }

    private fun getAllergies(): MutableList<String>? {
        if (allANDDisRequest != null) {
            var allergies_v: MutableList<String> = arrayListOf()
            if (allANDDisRequest.allergies != null) {
                for (aller : Any in allANDDisRequest.allergies) {
                    allergies_v.add(aller.toString().subSequence(6, aller.toString().lastIndex).toString())
                }
            }
            return allergies_v
        }
        return null
    }

    private fun getDiseases(): MutableList<String>? {
        if (allANDDisRequest != null) {
            var diseases_v: MutableList<String> = arrayListOf()
            if (allANDDisRequest.diseases != null) {
                for (aller : Any in allANDDisRequest.diseases) {
                    diseases_v.add(aller.toString().subSequence(6, aller.toString().lastIndex).toString())
                }
            }
            return diseases_v
        }
        return null
    }
}