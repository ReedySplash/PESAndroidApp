package edu.pes.laresistencia.register

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v4.graphics.drawable.RoundedBitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.example.api.laresistencia.R
import edu.pes.laresistencia.methods.ImageMethods
import kotlinx.android.synthetic.main.datepicker_layout.*
import kotlinx.android.synthetic.main.progressbar_layout.*
import kotlinx.android.synthetic.main.register_view.*
import java.text.SimpleDateFormat
import java.util.*

class RegisterView : AppCompatActivity(), IRegisterView {

    private lateinit var presenter: RegisterPresenter
    private val PICK_IMAGE: Int = 1
    private var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_view)

        presenter = RegisterPresenter(this, this)
        init()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        initToolbar()
        initVisionButton()
        initSpinnerGender()
        initRegisterButton()
        initPossibleCountries()
        initProfilePicture()
        initPasswordWatcher()
        initBirthdayDate()
    }

    private fun initToolbar() {
        setSupportActionBar(app_bar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun initVisionButton() {
        register_vision_btn1.setOnCheckedChangeListener { buttonView, isChecked ->
            presenter.firstVisionButtonPressed(isChecked)
        }
        register_vision_btn2.setOnCheckedChangeListener { buttonView, isChecked ->
            presenter.secondVisionButtonPressed(isChecked)
        }
    }

    private fun initRegisterButton() {
        register_button.setOnClickListener {
            presenter.registerButtonPressed(
                    register_name = register_name.text.toString(),
                    register_surname = register_surname.text.toString(),
                    register_image = (register_image.drawable as RoundedBitmapDrawable).bitmap,
                    register_date = register_date_button.text.toString(),
                    register_gender = register_gender.selectedItem.toString(),
                    register_country = register_country.text.toString(),
                    register_email = register_email.text.toString(),
                    register_password_1 = register_password_1.text.toString(),
                    register_password_2 = register_password_2.text.toString()
            )
        }
    }

    private fun initSpinnerGender() {
        var adapter = ArrayAdapter<String>(
                this,
                R.layout.spinner_dropdown,
                presenter.getPossibleGenders())
        adapter.setDropDownViewResource(R.layout.spinner_dropdown)
        register_gender.adapter = adapter

        register_gender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                presenter.genderTypeSelected(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //Nothing
            }
        }
    }

    private fun initPossibleCountries() {
        register_country.setAdapter(ArrayAdapter<String>(
                this,
                R.layout.spinner_dropdown,
                presenter.getPossibleCountries()
        ))
        register_country.threshold = 1
    }

    private fun initProfilePicture() {
        register_image.setOnClickListener {
            presenter.pictureButtonPressed()
        }
    }

    private fun initPasswordWatcher() {
        register_password_2.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(editable: Editable?) {
                val firstPassword: String = register_password_1.text.toString()
                val secondPassword = editable as Editable
                if (secondPassword.isNotEmpty() && firstPassword.isNotEmpty()) {
                    if (firstPassword != secondPassword.toString()) {
                        register_password_2.error = getString(R.string.register_password_not_match)
                    } else register_password_2.error = null
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
    }

    private fun initBirthdayDate() {
        register_date_button.text = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(Date())
        register_date_button.setOnClickListener {
            presenter.dateButtonClicked()
        }
    }

    override fun hideFirstPassword() {
        register_password_1.transformationMethod = HideReturnsTransformationMethod.getInstance()
    }

    override fun showFirstPassword() {
        register_password_1.transformationMethod = PasswordTransformationMethod.getInstance()
    }

    override fun hideSecondPassword() {
        register_password_2.transformationMethod = HideReturnsTransformationMethod.getInstance()
    }

    override fun showSecondPassword() {
        register_password_2.transformationMethod = PasswordTransformationMethod.getInstance()
    }

    override fun setProfilePicture(path: Uri) {
        Glide.with(this)
                .load(path)
                .asBitmap()
                .centerCrop()
                .into(object: BitmapImageViewTarget(register_image)
                {
                    override fun setResource(resource: Bitmap?) {
                        if (resource != null)
                        {
                            val picture = ImageMethods.makeImageDraw(resource, this@RegisterView)
                            register_image.setImageDrawable(picture)
                            register_image
                                    .startAnimation(AnimationUtils
                                    .loadAnimation(this@RegisterView, R.anim.fade_in))
                        }
                    }
                })
    }

    override fun setProfilePictureDefault(id: Int)
    {
        Glide.with(this)
                .load(id)
                .asBitmap()
                .centerCrop()
                .into(object: BitmapImageViewTarget(register_image)
        {
            override fun setResource(resource: Bitmap?) {
                if (resource != null)
                {
                    val picture = ImageMethods.makeImageDraw(resource, this@RegisterView)
                    register_image.setImageDrawable(picture)
                    register_image
                            .startAnimation(AnimationUtils
                                    .loadAnimation(this@RegisterView, R.anim.fade_in))
                }
            }
        })
    }

    //This function probably should not be here
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK)
            presenter.loadProfilePicture(data)
    }

    override fun showMissingNameError() {
        register_name.error = getString(R.string.register_name_required)
    }

    override fun showMissingSurnameError() {
        register_surname.error = getString(R.string.register_surname_required)
    }

    override fun showMissingCountryError() {
        register_country.error = getString(R.string.register_country_required)
    }

    override fun showMissingEmailError() {
        register_email.error = getString(R.string.register_email_required)
    }

    override fun showMissingPassword1Error() {
        register_password_1.error = getString(R.string.register_password_required)
    }

    override fun showMissingPassword2Error() {
        register_password_2.error = getString(R.string.register_password_required)
    }

    override fun showEmailNotValid() {
        register_email.error = getString(R.string.register_email_not_valid)
    }

    override fun showCountryNotValid() {
        register_country.error = getString(R.string.register_country_not_valid)
    }

    override fun showDateNotValid() {
        register_date_button.error = getString(R.string.register_date_not_valid)
    }

    override fun showError(error: String ) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun showDateDialog() {
        var dialog = Dialog(this)
         dialog.setContentView(R.layout.datepicker_layout)
         dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
         dialog.register_button_dialog.setOnClickListener {
             presenter.birthdayDateSelected(
                     dialog.register_datepicker.dayOfMonth,
                     dialog.register_datepicker.month,
                     dialog.register_datepicker.year
             )
             dialog.dismiss()
         }
        dialog.show()
        dialog.show()
    }

    override fun setBirthdayDate(day: Int, month: Int, year: Int) {
        val date: String = month.toString() + "/" + day.toString() + "/" + year.toString()
        register_date_button.text = date
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
