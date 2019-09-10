package edu.pes.laresistencia.profile

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.signature.StringSignature
import com.example.api.laresistencia.R
import edu.pes.laresistencia.methods.ImageMethods
import kotlinx.android.synthetic.main.profile_view.*
import kotlinx.android.synthetic.main.progressbar_layout.*

class ProfileView: Fragment(), IProfileView {

    private lateinit var presenter: ProfilePresenter
    private lateinit var activity: Activity
    private val PICK_IMAGE: Int = 1
    private var dialog: Dialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profile_view, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.title = activity.getString(R.string.profile_title)
        presenter = ProfilePresenter(activity, this)
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun init() {
        initProfile()
        initProfilePicture()
        initProfileTabsAndPager()
    }

    private fun initProfileTabsAndPager() {
        val data = presenter.getInfoUser()
        profile_pager.adapter = ViewPagerAdapter(((context as FragmentActivity).supportFragmentManager), data, context)
        profile_tabs.setupWithViewPager(profile_pager)
    }

    private fun initProfile() {
        presenter.initProfile()
    }

    private fun initProfilePicture() {
        edit_image.setOnClickListener {
            presenter.pictureButtonPressed()
        }
    }

    override fun initHeader(name: String, email: String)
    {
        profile_name.text = name
        profile_email.text = email
    }

    override fun setProfilePictureFromUrl(path: String)
    {
        Glide.with(this)
                .load(path)
                .asBitmap()
                .centerCrop()
                .signature(StringSignature(System.currentTimeMillis().toString()))
                .into(object: BitmapImageViewTarget(edit_image)
                {
                    override fun setResource(resource: Bitmap?) {
                        if (resource != null)
                        {
                            edit_image.setImageDrawable(ImageMethods
                                    .getRoundedBitmap(activity, resource))
                            edit_image.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.fade_in))
                        }
                    }
                })
    }

    //This function probably should not be here
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK)
            Glide.with(this)
                    .load(data?.data)
                    .asBitmap()
                    .centerCrop()
                    .into(object: BitmapImageViewTarget(edit_image)
                    {
                        override fun setResource(resource: Bitmap?) {
                            if (resource != null)
                            {
                                val picture = ImageMethods.makeImageDraw(resource, activity)
                                edit_image.setImageDrawable(picture)
                                edit_image.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.fade_in))
                                presenter.uploadProfilePicture(picture.bitmap)
1                            }
                        }
                    })
    }

    override fun showToast(message: String)
    {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun closeDialog()
    {
        dialog?.dismiss()
    }

    override fun showProgressDialog(text: String)
    {
        dialog = Dialog(activity)
        dialog?.setContentView(R.layout.progressbar_layout)
        dialog?.dialog_text?.text = text
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.show()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activity = context as (Activity)
    }

    override fun onStop()
    {
        presenter.stoping()
        super.onStop()
    }
}