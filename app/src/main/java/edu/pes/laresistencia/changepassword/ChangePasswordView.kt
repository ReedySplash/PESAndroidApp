package edu.pes.laresistencia.changepassword
import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.example.api.laresistencia.R
import kotlinx.android.synthetic.main.change_password_view.*
import kotlinx.android.synthetic.main.progressbar_layout.*

class ChangePasswordView : AppCompatActivity(), IChangePasswordView {
    private lateinit var presenter: ChangePasswordPresenter
    private var dialog: Dialog? = null

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.change_password_view)
        presenter = ChangePasswordPresenter(this, this)
        init()
    }

    fun init() {
        initToolbar()
        initChangePasswordButton()
    }

    private fun initToolbar() {
        setSupportActionBar(app_bar_change_password)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun initChangePasswordButton() {
        change_password_button.setOnClickListener {
            presenter.changePassword(edit_original_password.text.toString(), new_password_text.text.toString(), repeat_new_password_text.text.toString())
        }
    }

    override fun showProgressDialog(text: String?) {
        dialog = Dialog(this)
        dialog?.setContentView(R.layout.progressbar_layout)
        dialog?.dialog_text?.text = text
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.show()
    }

    override fun closeProgressDialog() {
        dialog?.dismiss()
    }

    override fun showPasswordChanged(string: String?) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    override fun showError(error: String?) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun showAlert(i: Int) {
        if (i == 0) {
            edit_original_password.error = getString(R.string.original_password_requiered)
        }
        else if (i == 1) {
            new_password_text.error = getString(R.string.new_password_requiered)
        }
        else {
            repeat_new_password_text.error = getString(R.string.repeated_password_required)
        }
    }

    override fun endView() {
        finish()
    }

    override fun onStop() {
        presenter.stopping()
        super.onStop()
    }
}