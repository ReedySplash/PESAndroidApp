package edu.pes.laresistencia.config

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.example.api.laresistencia.R
import kotlinx.android.synthetic.main.configuration_view.*
import kotlinx.android.synthetic.main.progressbar_layout.*
import java.util.*

class ConfigView : AppCompatActivity(), IConfigView {
    private lateinit var presenter: ConfigPresenter
    private var task_dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.configuration_view)
        setSupportActionBar(settings_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        init()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun init() {
        presenter = ConfigPresenter(this, this)
        initChangePassword()
        initChangeIdiom()
        initDeleteAccount()
        initIdiom()
    }

    private fun initIdiom() {
        idiom_selected.text = getString(R.string.language_selected)
    }

    private fun initDeleteAccount() {
        delete_account_layout.setOnClickListener {
            presenter.deleteButtonPressed()
        }
    }

    override fun showDeleteDialog() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(R.string.delete_dialog_title)
        dialog.setMessage(R.string.edit_delete_account_text)
        dialog.setPositiveButton(R.string.affirmative_delete_account, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, index: Int) {
                presenter.deleteAccount()
            }

        })
        dialog.setNegativeButton(R.string.negative_delete_account, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                dialog.dismiss()
            }
        })
        dialog.setIcon(android.R.drawable.ic_dialog_alert)
        dialog.show()
    }

    private fun initChangeIdiom() {
        change_idiom_layout.setOnClickListener {
            presenter.changeIdiomButtonPressed()
        }
    }

    private fun initChangePassword() {
        change_password_layout.setOnClickListener {
            presenter.changePasswordButtonPressed()
        }
    }

    override fun closeProgressDialog() {
        task_dialog?.dismiss()
    }

    override fun showProgressDialog(text: String?) {
        task_dialog = Dialog(this)
        task_dialog?.setContentView(R.layout.progressbar_layout)
        task_dialog?.dialog_text?.text = text
        task_dialog?.setCancelable(false)
        task_dialog?.setCanceledOnTouchOutside(false)
        task_dialog?.show()
    }

    override fun showError(error: String?) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun endView() {
        finish()
    }

    override fun onStop() {
        presenter.stopping()
        super.onStop()
    }
}
