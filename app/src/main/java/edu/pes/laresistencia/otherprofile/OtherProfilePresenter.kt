package edu.pes.laresistencia.otherprofile
import android.content.Context
import com.example.api.laresistencia.R
import edu.pes.laresistencia.injection.component.DaggerOtherProfileInjector
import edu.pes.laresistencia.injection.modules.AppModule
import edu.pes.laresistencia.injection.modules.NetworkModule
import edu.pes.laresistencia.network.*
import edu.pes.laresistencia.register.UserRequest
import edu.pes.laresistencia.service.FriendshipService
import edu.pes.laresistencia.service.LoginService
import edu.pes.laresistencia.service.ProfileService
import edu.pes.laresistencia.storage.InternalStorage
import javax.inject.Inject

class OtherProfilePresenter(val context: Context, val view: IOtherProfileView):ProfileService.OnChangeProfileCompleted, LoginService.OnLoginCompleted, FriendshipService.OnFriendshipRequest
{
    @Inject
    lateinit var oauthAPI: OAuthAPI
    @Inject
    lateinit var userAPI: UserAPI
    @Inject
    lateinit var friendshipAPI: FriendshipAPI
    private val loggingService: LoginService
    private val profileService: ProfileService
    private val friendshipService: FriendshipService
    private var internalStorage: InternalStorage
    private var email: String = ""


    init {
        val injector = DaggerOtherProfileInjector
                .builder()
                .appModule(AppModule(context))
                .networkModule(NetworkModule)
                .build()
        injector.inject(this)
        loggingService = LoginService(this,oauthAPI,userAPI)
        profileService = ProfileService(this, userAPI)
        friendshipService = FriendshipService(this,friendshipAPI)
        internalStorage = InternalStorage()
    }
    fun initProfile(email: String) {
        this.email = email
        getInfoUser()
    }

    fun getFriendlyStatus() {
        friendshipService.getFriendly(email)
    }

    fun requestFriendly() {
        sendFriendly("request")
    }

    fun acceptFriendly() {
        sendFriendly("settle")
    }


    private fun getInfoUser() {
        view.showProgressDialog(context.getString(R.string.get_other_info_usuari))
        loggingService.getInfoUser( "/user/" + email, internalStorage.getToken(context)!!)
    }

    private fun handleOtherUserResponse(code: Int?): Boolean {
        when (code) {
            in 200..299 ->
                return true
            900 ->
                view.showError(context.getString(R.string.error_internal))
            else ->
                view.showError(context.getString(R.string.login_server_error))
        }
        view.closeDialog()
        return false
    }

    override fun getUserInfoCompleted(response: Pair<Int?, UserRequest?>) {
        if (response.first != null && handleOtherUserResponse(response.first)) {
            view.initParameters(response.second)
        }
    }

    private fun sendFriendly(type: String) {
        if (type == "request") {
            view.showProgressDialog(context.getString(R.string.sending_friendship_request))
            friendshipService.requestFriendship(friendshipAPI, this.email)
        }
        else {
            view.showProgressDialog(context.getString(R.string.sending_friendship_settle))
            friendshipService.acceptFriendship(friendshipAPI, this.email)
        }
    }

    private fun handleOtherFriendlyStatus(code: Int?): Boolean {
        when (code) {
            in 200..299 ->
                    return true
            900 ->
                view.showError(context.getString(R.string.error_internal))
            else ->
                view.showError(context.getString(R.string.login_server_error))
        }
        view.closeDialog()
        return false
    }

    override fun getFriendlyStatusCompleted(code: Int?, body: FriendshipResponse?) {
        if (body != null && handleOtherFriendlyStatus(code)) {
            view.initFriendly(body.status.toString())
        }
        view.closeDialog()
        view.initButton()
    }


    private fun handleSendFriendlyResponse(code: Int?): Boolean {
        when (code) {
            in 200..299 -> {
                return true
            }
            900 ->
                view.showError(context.getString(R.string.error_internal))
            else ->
                view.showError(context.getString(R.string.login_server_error))
        }
        view.closeDialog()
        return false
    }


    override fun changePhotoCompleted(code: Int?) {
    }

    override fun loginCompleted(code: Int?) {
    }


    fun initImage() {
        view.setProfilePictureFromUrl("${NetworkModule.baseUrl}/user/${this.email}/photo")
    }

    fun stopping() {
        profileService.cancel()
        view.closeDialog()
    }

    fun cancelRequest() {
        view.showProgressDialog(context.getString(R.string.canceling_friendship))
        friendshipService.removeRequestFriendship(friendshipAPI,this.email)
    }

    override fun requestFriendshipCompleted(code: Int?) {
        if (code != null && handleSendFriendlyResponse(code)) {
            view.changeFriendlyStatus()
            view.initButton()
        }
        view.closeDialog()
    }

    override fun acceptFriendshipCompleted(code: Int?) {
        if (code != null && handleSendFriendlyResponse(code)) {
            view.changeFriendlyStatus()
            view.initButton()
        }
        view.closeDialog()
    }

    override fun chatsReceived(code: Int?, list: List<FriendshipResponse>?) {
    }

    override fun removeFriendshipCompleted(code: Int?) {
        if (code != null && handleRemoveFriendlyCode(code)) {
            view.changeFriendlyRemoveStatus()
            view.initButton()
            view.closeDialog()
        }
    }

    private fun handleRemoveFriendlyCode(code: Int?): Boolean {
        when (code) {
            in 200..299 ->
                return true
            900 ->
                view.showError(context.getString(R.string.error_internal))
            else ->
                view.showError(context.getString(R.string.login_server_error))
        }
        view.closeDialog()
        return false
    }

}