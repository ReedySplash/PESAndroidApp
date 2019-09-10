package edu.pes.laresistencia.register

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.graphics.drawable.RoundedBitmapDrawable
import com.example.api.laresistencia.R
import edu.pes.laresistencia.injection.component.DaggerRegisterInjector
import edu.pes.laresistencia.injection.modules.AppModule
import edu.pes.laresistencia.injection.modules.NetworkModule
import edu.pes.laresistencia.network.OAuthAPI
import edu.pes.laresistencia.network.UserAPI
import edu.pes.laresistencia.service.LoginService
import edu.pes.laresistencia.service.RegisterService
import edu.pes.laresistencia.storage.InternalStorage
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject
import android.graphics.Bitmap

import android.util.Base64
import edu.pes.laresistencia.home.HomeView


class RegisterPresenter(val context: Context, val view: IRegisterView) :
        RegisterService.OnRegisterCompleted, LoginService.OnLoginCompleted{

    @Inject
    lateinit var userAPI: UserAPI
    @Inject
    lateinit var oauthAPI: OAuthAPI

    private var registerService: RegisterService
    private var loggingService: LoginService
    private var internalStorage: InternalStorage

    //Variables
    private var countries = arrayListOf<String>()
    private var selectedImage: Boolean = false
    private val PICK_IMAGE: Int = 1


    private var email: String? = null
    private var password: String? = null

    init {
        val locales: Array<String> = Locale.getISOCountries()
        for (countryCode in locales)
            this.countries.add(
                    Locale("", countryCode).displayCountry)

        val injector = DaggerRegisterInjector
                .builder()
                .appModule(AppModule(context))
                .networkModule(NetworkModule)
                .build()
        injector.inject(this)
        registerService = RegisterService(this, userAPI)
        loggingService = LoginService(this, oauthAPI,userAPI)
        internalStorage = InternalStorage()
    }

    fun firstVisionButtonPressed(checked: Boolean) {
        if (checked) view.hideFirstPassword()
        else view.showFirstPassword()
    }

    fun secondVisionButtonPressed(checked: Boolean) {
        if (checked) view.hideSecondPassword()
        else view.showSecondPassword()
    }

    fun genderTypeSelected(position: Int) {
        //Default pictures
        if (!selectedImage) {
            when (position) {
                0 -> view.setProfilePictureDefault(R.drawable.ic_avatar_man)
                1-> view.setProfilePictureDefault(R.drawable.ic_avatar_woman)
                2-> view.setProfilePictureDefault(R.drawable.ic_avatar_other)
            }
        }
    }

    fun registerButtonPressed(register_name: String, register_surname: String, register_date: String,
                              register_gender: String, register_country: String, register_email: String,
                              register_password_1: String, register_password_2: String,
                              register_image: Bitmap?) {

        if (checkRequiredFields(register_name, register_surname, register_country,
                        register_email, register_password_1, register_password_2))
            view.showError(context.getString(R.string.register_missing_parameters))
        else if (!isCountryValid(register_country))
        {
            view.showCountryNotValid()
            view.showError(context.getString(R.string.register_country_not_valid))
        }
        else if (!isEmailValid(register_email))
        {
            view.showEmailNotValid()
            view.showError(context.getString(R.string.register_email_not_valid))
        }
        else if (register_password_1 != register_password_2)
            view.showError(context.getString(R.string.register_password_not_match))
        else if (!isDateValid(SimpleDateFormat("yyyy/MM/dd").parse(register_date))) {
            view.showDateNotValid()
            view.showError(context.getString(R.string.register_date_not_valid))
        }
        else
        {
            var bos = ByteArrayOutputStream()
            register_image?.compress(Bitmap.CompressFormat.WEBP, 100, bos)
            val encodedImage = Base64.encodeToString(bos.toByteArray(), Base64.NO_WRAP)
            val request = UserRequest(
                    name = register_name,
                    surname = register_surname,
                    email = register_email,
                    birthDate = register_date,
                    country = register_country,
                    gender = Gender.valueOf(register_gender.toUpperCase()),
                    password = register_password_1,
                    photo = encodedImage
            )
            email = register_email
            password = register_password_1
            view.showProgressDialog(context.getString(R.string.register_progress))
            registerService.register(request)
        }
    }

    fun pictureButtonPressed() {
        val galleryIntent = Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(
                context as Activity,
                galleryIntent,
                PICK_IMAGE,
                null
        )
    }

    fun loadProfilePicture(data: Intent?) {
        selectedImage = true
        view.setProfilePicture(data!!.data)
    }

    fun dateButtonClicked() {
        view.showDateDialog()
    }

    fun birthdayDateSelected(day: Int, month: Int, year: Int) {
        view.setBirthdayDate(day, month + 1, year)
    }

    fun getPossibleCountries(): ArrayList<String> {
        return countries
    }

    fun getPossibleGenders(): Array<String> {
        return context.resources.getStringArray(R.array.register_gender)
    }

    fun stoping()
    {
        loggingService.cancel()
        registerService.cancel()
        view.closeDialog()
    }

    private fun isEmailValid(email: String?): Boolean {
        val expression: String = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun isCountryValid(country: String?): Boolean {
        return countries.contains(country)
    }

    private fun isDateValid(date: Date): Boolean {
        //val sdf: SimpleDateFormat = SimpleDateFormat("yyyy/MM7dd")
        //val currentDate: Date = sdf.parse()
        return date.before(Date())

    }

    private fun checkRequiredFields(register_name: String, register_surname: String, register_country: String,
                                    register_email: String, register_password_1: String,
                                    register_password_2: String): Boolean {
        var miss = false
        if (register_name.isEmpty()) {
            miss = true
            view.showMissingNameError()
        }
        if (register_surname.isEmpty()) {
            miss = true
            view.showMissingSurnameError()
        }
        if (register_country.isEmpty()) {
            miss = true
            view.showMissingCountryError()
        }
        if (register_email.isEmpty()) {
            miss = true
            view.showMissingEmailError()
        }
        if (register_password_1.isEmpty()) {
            miss = true
            view.showMissingPassword1Error()
        }
        if (register_password_2.isEmpty()) {
            miss = true
            view.showMissingPassword2Error()
        }
        return miss
    }

    private fun treatCodeRequest(code: Int): Boolean {
        when (code) {
            in 200..299 -> {
                view.showError(context.getString(R.string.register_correct))
                return true
            }
            in 400..499 -> view.showError(context.getString(R.string.register_repeated_user))
            900 -> view.showError(context.getString(R.string.error_internal))
            else -> view.showError(context.getString(R.string.login_server_error))
        }
        view.closeDialog()
        return false
    }

    private fun handleResponse(code: Int): Boolean
    {
        when (code) {
            in 200..299 -> return true
            in 400..499 ->  view.showError(context.getString(R.string.login_incorrect_parameters))
            900 -> view.showError(context.getString(R.string.error_internal))
            else -> view.showError(context.getString(R.string.login_server_error))
        }
        view.closeDialog()
        return false
    }

    override fun registerCompleted(code: Int?)
    {
        if (code != null && treatCodeRequest(code))
        {
            view.closeDialog()
            view.showProgressDialog(context.getString(R.string.login_progress))
            loggingService.login(email!!, password!!)
        }
    }

    override fun loginCompleted(code: Int?)
    {
        if (code != null && handleResponse(code))
        {
            internalStorage
                    .storeToken(NetworkModule
                    .authenticatorInterceptor
                    .authToken!!, context)
            view.closeDialog()
            view.showProgressDialog(context.getString(R.string.infoUser_progress))
            loggingService.getInfoUser("/user/$email", NetworkModule
                    .authenticatorInterceptor
                    .authToken)
        }
        else view.endView()
    }

    override fun getUserInfoCompleted(response: Pair<Int?, UserRequest?>)
    {
        if (response.first != null && handleResponse(response.first!!))
        {
            if (response.second != null)
            {
                internalStorage.storeUser(context, response.second!!)
                view.closeDialog()
                val intent = Intent()
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.setClass(context, HomeView::class.java)
                context.startActivity(intent)
            }
            else
                view.closeDialog()
        }
        else view.closeDialog()
    }
}