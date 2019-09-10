package edu.pes.laresistencia.login

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.api.laresistencia.R
import kotlinx.android.synthetic.main.login_view.*
import kotlinx.android.synthetic.main.progressbar_layout.*

class LoginView : AppCompatActivity(), ILoginView {

    private lateinit var presenter: LoginPresenter
    private var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_view)
        presenter = LoginPresenter(this, this)
        init()
    }

    fun init() {
        initLoginButton()
        initCreateAccount()
    }

    private fun initLoginButton() {
        login_button.setOnClickListener {
            presenter.loginButtonPressed(
                    mail_text = mail_text.text.toString(),
                    password_text = password_text.text.toString()
            )
        }
    }

    private fun initCreateAccount() {
        create_account.setOnClickListener {
            presenter.createButtonPressed()
        }
    }

    override fun showMissingMailError(error: String) {
        mail_text.error = getString(R.string.register_email_required)
    }

    override fun showMissingPasswordError(error: String) {
        password_text.error = getString(R.string.register_password_required)
    }

    override fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun endView() {
        finish()
    }

    override fun showProgressDialog(text: String)
    {
        dialog = Dialog(this)
        dialog?.setContentView(R.layout.progressbar_layout)
        dialog?.dialog_text?.text = text
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.show()
    }

    override fun closeDialog()
    {
        dialog?.dismiss()
    }

    override fun onStop()
    {
        presenter.stoping()
        super.onStop()
    }

}