package edu.pes.laresistencia.home

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.signature.StringSignature
import com.example.api.laresistencia.R
import edu.pes.laresistencia.config.ConfigView
import edu.pes.laresistencia.listchats.ListChatFragments
import edu.pes.laresistencia.location.LocationView
import edu.pes.laresistencia.methods.ImageMethods
import edu.pes.laresistencia.profile.ProfilePresenter
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

class HomeView : AppCompatActivity(), IHomeView,  ProfilePresenter.OnChangedProfilePicture
{
    override fun removeChecked() {
        val size: Int = home_navDrawable.menu.size()
        for (i in 0 until size)
        {
            home_navDrawable.menu.getItem(i).isChecked = false
        }
    }

    private lateinit var presenter: HomePresenter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(home_toolbar)
        presenter = HomePresenter(this, this)
        initializeModel()
        initProfileImage()
        initFragment()
        initMenu()
    }

    private fun initProfileImage() {
        home_navDrawable.getHeaderView(0).nav_image.setOnClickListener {
            presenter.profileClicked(home_navDrawable.menu.getItem(4))
        }
    }

    override fun initHeader(name: String, email: String, url: String) {
        /*val glideUrl: GlideUrl = GlideUrl(url, LazyHeaders.Builder()
                .addHeader("Authorization", "bearer " + NetworkModule.authenticatorInterceptor.authToken).build())*/

        setProfilePicture(url)
        home_navDrawable.getHeaderView(0).nav_name.text = name
        home_navDrawable.getHeaderView(0).nav_email.text = email
        home_navDrawable.menu.getItem(5).isCheckable = false
        home_navDrawable.menu.getItem(6).isCheckable = false
    }

    private fun initializeModel()
    {
        presenter.getInfoUser()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId)
        {
            R.id.action_closeSession -> {
                presenter.logout()
                return true
            }
            R.id.action_settings ->
            {
                presenter.settingsClicked()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed()
    {
        if (home_drawerLayout.isDrawerOpen(GravityCompat.START))
            home_drawerLayout.closeDrawer(GravityCompat.START)
        else super.onBackPressed()
    }

    override fun closeDrawable()
    {
        home_drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK)
            supportFragmentManager.findFragmentById(R.id.home_frameFragment).
                    onActivityResult(requestCode, resultCode, data)
    }

    private fun initFragment()
    {
        //initial fragment
        supportFragmentManager
                .beginTransaction()
                .add(R.id.home_frameFragment, LocationView())
                .commit()
        home_navDrawable.menu.getItem(0).isChecked = true
    }

    private fun initMenu()
    {
        val toggle = ActionBarDrawerToggle(
                this, home_drawerLayout, home_toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        home_drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        home_navDrawable.setNavigationItemSelectedListener(presenter)
    }

    override fun setProfilePicture(url: String)
    {
        Glide.with(this)
                .load(url)
                .asBitmap()
                .centerCrop()
                .signature(StringSignature(System.currentTimeMillis().toString()))
                .into(object : BitmapImageViewTarget(home_navDrawable.getHeaderView(0).nav_image)
                {
                    override fun setResource(resource: Bitmap?) {
                        if (resource != null)
                        {
                            home_navDrawable.getHeaderView(0)
                                    .nav_image.setImageDrawable(ImageMethods.
                                    getRoundedBitmap(this@HomeView, resource))
                        }
                    }
                })
    }

    override fun setHeightToolbar()
    {
        val dp: Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, this.resources.displayMetrics)
        supportActionBar?.elevation = dp
    }

    override fun reloadPicture()
    {
        presenter.reloadPicture()
    }
}
