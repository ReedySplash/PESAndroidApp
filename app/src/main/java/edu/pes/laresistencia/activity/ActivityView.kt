package edu.pes.laresistencia.activity

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.signature.StringSignature
import com.example.api.laresistencia.R
import edu.pes.laresistencia.injection.modules.NetworkModule
import edu.pes.laresistencia.methods.ImageMethods
import kotlinx.android.synthetic.main.activity_comments.*
import kotlinx.android.synthetic.main.activity_view.*
import kotlinx.android.synthetic.main.comment_item.*


class ActivityView: AppCompatActivity(), IActivityView, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var presenter: ActivityPresenter
    private lateinit var commentsAdapter: ListCommentsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)
        presenter = ActivityPresenter(this, this)
        init()
    }

    private fun init() {
        initToolbar()
        initActivity()
        initTabs()
        initCalendarButton()
    }

    private fun initToolbar() {
        setSupportActionBar(activity_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun initActivity() {
        val bundle = intent.extras
        var id: String? = null
        if (bundle != null)
            id = bundle.getString("id")
        presenter.initActivity(id)
    }

    private fun initTabs() {
        val fragmentAdapter = MyPagerAdapter(supportFragmentManager)
        viewpager_main.adapter = fragmentAdapter
        activity_tab.setupWithViewPager(viewpager_main)
    }

    private fun initCalendarButton() {
        activity_add_calendar.setOnClickListener {
            presenter.addCalendarButtonPressed()
        }
    }

    fun initComments() {
        val listComments = presenter.initComments()
        if (listComments != null)
            initRecyclerView(listComments)
    }

    fun initCommentButton() {
        floating_comment.setOnClickListener {
            presenter.commentButtonPressed(comments_MessageBox.text.toString())
        }
    }

    fun initOptionsButton() {
        comments_delete_button.setOnClickListener {
            presenter.optionsButtonPressed()
        }
    }

    override fun showActivity(activity: ActivityRequest) {
        activity_title.text = activity.title
        activity_date.text = activity.date
        activity_fromto.text = activity.from + " - " + activity.to
        activity_location.text = "Meet at: " + activity.location
        setActivityImage(activity.id)
    }

    private fun setActivityImage(id: String) {
        val url = "${NetworkModule.baseUrl}/activities/" + id + "/image"
        val glideUrl: GlideUrl = GlideUrl(url, LazyHeaders.Builder()
                .addHeader("Authorization", "bearer " + NetworkModule.authenticatorInterceptor.authToken).build())

        Glide.with(this)
                .load(glideUrl)
                .centerCrop()
                .into(activity_image)

    }

    fun getDescription(): String? {
        return presenter.getDescription()
    }

    override fun showServerError() {
        Toast.makeText(this, getString(R.string.activity_not_load), Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRecyclerView(listComments: List<CommentsRequest>?) {
        if (listComments != null) {
            comments_recyclerView.layoutManager = LinearLayoutManager(this)
            commentsAdapter = ListCommentsAdapter(this, listComments.reversed(), presenter.getActivityService(), this)
            comments_recyclerView.adapter = commentsAdapter
        }
    }

    override fun reloadRecyclerView(listComments: List<CommentsRequest>) {
        commentsAdapter.updateData(listComments.reversed())
        commentsAdapter.notifyDataSetChanged()
    }

    override fun resetCommentEditText() {
        comments_MessageBox.text = null
    }

    override fun showPostCommentError() {
        Toast.makeText(this, getString(R.string.activity_comment_error), Toast.LENGTH_SHORT).show()
    }

    override fun showGetCommentsError() {
        Toast.makeText(this, getString(R.string.activity_get_comment_error), Toast.LENGTH_SHORT).show()
    }

    override fun onRefresh() {
        val listComments = presenter.initComments()
        if (listComments != null)
            initRecyclerView(listComments)
        commentslist_reloadlayout.isRefreshing = false
    }

    fun initReloadComments() {
        commentslist_reloadlayout.setOnRefreshListener(this)
        commentslist_reloadlayout.setColorSchemeResources(R.color.colorAccent)
    }

    fun updateComments() {
        presenter.updateComments()
    }

}


