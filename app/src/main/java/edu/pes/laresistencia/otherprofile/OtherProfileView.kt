package edu.pes.laresistencia.otherprofile

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.signature.StringSignature
import com.example.api.laresistencia.R
import edu.pes.laresistencia.methods.ImageMethods
import edu.pes.laresistencia.register.UserRequest
import kotlinx.android.synthetic.main.other_profile_view.*
import kotlinx.android.synthetic.main.progressbar_layout.*


class OtherProfileView: AppCompatActivity(), IOtherProfileView {

    private lateinit var presenter: OtherProfilePresenter
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.other_profile_view)
        presenter = OtherProfilePresenter(this,this)
        init()
    }

    private fun init() {
        initToolbar()
        initProfile()
        initPictureProfile()
    }

    private fun initPictureProfile() {
        presenter.initImage()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initToolbar() {
        setSupportActionBar(app_bar_other_profile)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "$"
    }

    @SuppressLint("ResourceAsColor")
    override fun initButton() {
        if (profile_status_friendly.text == getString(R.string.friendship_none)) {
            friendly_button.setOnClickListener {
                presenter.requestFriendly()
            }
            cancel_button.visibility = View.INVISIBLE
        }

        else if (profile_status_friendly.text == getString(R.string.friendship_pending)) {
            friendly_button.visibility = View.INVISIBLE
            cancel_button.visibility = View.VISIBLE

            cancel_button.setOnClickListener {
                presenter.cancelRequest()
            }
        }
        else if (profile_status_friendly.text == getString(R.string.friendship_to_accept)) {
            text_button.text = getString(R.string.accept_friendship)
            text_button2.text = getString(R.string.listchats_cancel)
            friendly_button.setOnClickListener {
                presenter.acceptFriendly()
            }
            cancel_button.setOnClickListener {
                presenter.cancelRequest()
            }
        }
        else {
            friendly_button.visibility = View.INVISIBLE
            cancel_button.visibility = View.INVISIBLE
        }
    }



    @SuppressLint("ResourceAsColor")
    override fun changeFriendlyStatus() {
        if (profile_status_friendly.text.toString() == getString(R.string.friendship_none)) {
            profile_status_friendly.text = getString(R.string.friendship_pending)
            cancel_button.visibility = View.VISIBLE
            friendly_button.visibility = View.INVISIBLE
        }
        else {
            profile_status_friendly.text = getString(R.string.friendship_settled)
            cancel_button.visibility = View.INVISIBLE
            friendly_button.visibility = View.INVISIBLE
        }
    }

    override fun changeFriendlyRemoveStatus() {
        if (profile_status_friendly.text.toString() == getString(R.string.friendship_pending)) {
            profile_status_friendly.text = getString(R.string.friendship_none)
            cancel_button.visibility = View.INVISIBLE
            friendly_button.visibility = View.VISIBLE
        }
        else if (profile_status_friendly.text.toString() == getString(R.string.friendship_to_accept)) {
            profile_status_friendly.text = getString(R.string.friendship_none)
            cancel_button.visibility = View.INVISIBLE
            text_button.text = getString(R.string.request_friendship)
            friendly_button.visibility = View.VISIBLE
        }
        else {
            profile_status_friendly.text = getString(R.string.friendship_none)
            cancel_button.visibility = View.INVISIBLE
            friendly_button.visibility = View.VISIBLE
        }
    }

    override fun setProfilePictureFromUrl(path: String) {
        Glide.with(this)
                .load(path)
                .asBitmap()
                .centerCrop()
                .signature(StringSignature(System.currentTimeMillis().toString()))
                .into(object: BitmapImageViewTarget(other_profile_image)
                {
                    override fun setResource(resource: Bitmap?) {
                        if (resource != null)
                        {
                            other_profile_image.setImageDrawable(ImageMethods
                                    .getRoundedBitmap(this@OtherProfileView, resource))
                            other_profile_image.startAnimation(AnimationUtils.loadAnimation(this@OtherProfileView, R.anim.fade_in))
                        }
                    }
                })
    }

    private fun initProfile() {
        presenter.initProfile(intent.getStringExtra("email"))
        presenter.getFriendlyStatus()
    }

    override fun initFriendly(status: String) {
        profile_status_friendly.text = status

    }

    override fun initParameters(user: UserRequest?) {
        supportActionBar?.title = user!!.name + "'s profile"
        val name = user.name + " " + user.surname
        profile_name.text = name
        other_profile_country.text = user.country
        other_profile_gender.text = user.gender.toString()
        other_profile_birthday.text = user.birthDate
    }


    override fun showProgressDialog(text: String?) {
        dialog = Dialog(this)
        dialog.setContentView(R.layout.progressbar_layout)
        dialog.dialog_text.text = text
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    override fun closeDialog()
    {
        dialog.dismiss()
    }

    override fun showError(error: String?) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        presenter.stopping()
        super.onStop()
    }
}