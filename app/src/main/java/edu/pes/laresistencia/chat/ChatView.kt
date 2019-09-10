package edu.pes.laresistencia.chat

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.signature.StringSignature
import com.example.api.laresistencia.R
import edu.pes.laresistencia.injection.modules.NetworkModule
import edu.pes.laresistencia.methods.ImageMethods
import edu.pes.laresistencia.network.Chat
import kotlinx.android.synthetic.main.activity_chat.*
import okhttp3.Response
import java.text.SimpleDateFormat
import java.util.*

class ChatView : AppCompatActivity(), IChatView {

    private val chatMessages: ArrayList<ChatMessage> = arrayListOf()
    private lateinit var mListAdapter: ChatAdapter
    private lateinit var emailContact: String
    private lateinit var nameContact: String
    private lateinit var presenter: ChatPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val bundle: Bundle = intent.extras
        emailContact = bundle.getString("email")
        nameContact = bundle.getString("name")
        presenter = ChatPresenter(this, this, emailContact)
        init()
    }

    private fun init()
    {
        initToolbar()
        initSendButton()
        initAdapter()
    }

    private fun initAdapter()
    {
        mListAdapter = ChatAdapter(this, chatMessages)
        list_chat.layoutManager = LinearLayoutManager(this)
        list_chat.adapter = mListAdapter
    }

    private fun initSendButton()
    {
       floating.setOnClickListener {
           runOnUiThread {
               presenter.sendMessage(xat_MessageBox.text.toString())
           }
           xat_MessageBox.text.clear()
       }
    }

    private fun initToolbar()
    {
        setSupportActionBar(chat_bar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        chat_name.text = nameContact
        Glide.with(this)
                .load("${NetworkModule.baseUrl}/user/$emailContact/photo")
                .asBitmap()
                .centerCrop()
                .signature(StringSignature(System.currentTimeMillis().toString()))
                .into(object: BitmapImageViewTarget(chat_profilePicture)
                {
                    override fun setResource(resource: Bitmap?) {
                        if (resource != null)
                        {
                            chat_profilePicture.setImageDrawable(ImageMethods
                                    .getRoundedBitmap(this@ChatView, resource))
                            chat_profilePicture.startAnimation(
                                    AnimationUtils.loadAnimation(this@ChatView, R.anim.fade_in))
                        }
                    }
                })
    }

    // region view

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun receivedMessage(message: ChatMessage) {
        val dateFormat = SimpleDateFormat("HH:mm")
        runOnUiThread {
            message.date = dateFormat.format(Date())
            chatMessages.add(message)
            mListAdapter.notifyDataSetChanged()
            list_chat.smoothScrollToPosition(chatMessages.size - 1)
        }
    }

    override fun chatOpened() {
        runOnUiThread {
            xat_MessageBox.isEnabled = true
        }
    }

    override fun chatClosed() {
        runOnUiThread {
            xat_MessageBox.isEnabled = false
        }
    }

    override fun failureHappened(throwable: Throwable, response: Response?) {
        runOnUiThread {
            Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun failureReceivingHistory() {
        runOnUiThread {
            Toast.makeText(this, "something happened receiving the history", Toast.LENGTH_SHORT).show()
        }
    }

    override fun history(body: List<Chat>) {
        runOnUiThread {
            chatMessages.addAll(body.map {
                val chat = ChatMessage()
                chat.receiver = it.receiver
                chat.type = ChatMessage.MessageType.CHAT
                chat.sender = it.sender
                chat.date = it.date
                chat.content = it.message
                chat
            })
            mListAdapter.notifyDataSetChanged()
            if (chatMessages.size > 0) {
                list_chat.smoothScrollToPosition(chatMessages.size - 1)
            }
        }

    }

    // endregion
}