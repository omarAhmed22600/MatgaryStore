package com.brandsin.store.ui.chat.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.realtimeDatabase.dao.InboxDao
import com.brandsin.store.utils.PrefMethods
import com.brandsin.user.ui.chat.model.MessageModel
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class InboxViewModel : BaseViewModel() {

    private val inboxDao: InboxDao = InboxDao()

    var isImageUploaded: Boolean = false

    private val _usersInboxMutable: MutableLiveData<MutableList<MessageModel>> = MutableLiveData()
    val usersInboxLive: LiveData<MutableList<MessageModel>> = _usersInboxMutable

    private val _messagesMutableData: MutableLiveData<ArrayList<MessageModel>> = MutableLiveData()
    val messagesLiveData: LiveData<ArrayList<MessageModel>> = _messagesMutableData

    // List to store the last messages
    private var inboxesList = mutableListOf<MessageModel>()

    fun readChat() {
        // get all Users who start conversation before
        // enqueueSignal(Load)
        inboxDao.getInboxListRef(PrefMethods.getStoreData()?.id.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    inboxesList = ArrayList()
                    for (it in snapshot.children) {
                        val lastMessage81766 =
                            it.children.lastOrNull()?.getValue(MessageModel::class.java)
                        lastMessage81766?.let { inboxesList.add(it) }
                    }

                    _usersInboxMutable.value = inboxesList
                    // enqueueSignal(StopLoading)
                }

                override fun onCancelled(error: DatabaseError) {
                    println("readChat ERROR ${error.message}")
                    // var errorMessage = error.message
                    // enqueueSignal(StopLoading, SomethingWentWrong.ErrorMessage)
                }
            })
    }

    fun readMessage(senderId: String?) {
        inboxDao.readMessage(
            PrefMethods.getStoreData()?.id.toString(), // 817
            senderId.toString().trim() + PrefMethods.getStoreData()?.id.toString().trim(), // 817130
        ).get().addOnCompleteListener {
            val messageList = ArrayList<MessageModel>()
            if (it.result.exists()) {
                for (ds in it.result.children) {
                    val messages = ds.getValue(MessageModel::class.java)
                    messageList.add(messages!!)
                }
            }
            _messagesMutableData.postValue(messageList)
        }
    }

    fun sendMessage(
        avatarUser: String?, senderName: String?,
        senderId: String?,
        message: String, typeMessage: String
    ) {
        // Add new Message in the Conversation to Chat
        val messageId = UUID.randomUUID().toString()
        val messageModel: MessageModel
        if (typeMessage == "image") {
            messageModel = MessageModel(
                avaterstore = PrefMethods.getStoreData()?.thumbnail.toString()
                    .trim(), // messageItem?.avaterstore.toString(),
                avateruser = avatarUser.toString(),
                messageType = "private",
                senderName = senderName.toString(),
                storename = PrefMethods.getStoreData()?.name.toString(), // messageItem?.storename.toString(),
                senderId = senderId.toString(),
                storeId = PrefMethods.getStoreData()?.id.toString(), // messageItem?.storeId.toString(),
                image = message,
                message = message,
                messageId = messageId,
                date = System.currentTimeMillis().toString(),
                type = typeMessage,
                typeBay = "store"
            )
        } else {
            messageModel = MessageModel(
                avaterstore = PrefMethods.getStoreData()?.thumbnail.toString()
                    .trim(), // messageItem?.avaterstore.toString(),
                avateruser = avatarUser.toString(),
                messageType = "private",
                senderName = senderName.toString(),
                storename = PrefMethods.getStoreData()?.name.toString(), // messageItem?.storename.toString(),
                senderId = senderId.toString(),
                storeId = PrefMethods.getStoreData()?.id.toString(), // messageItem?.storeId.toString(),
                image = message,
                message = message,
                messageId = messageId,
                date = System.currentTimeMillis().toString(),
                type = typeMessage,
                typeBay = "store"
            )
        }

        inboxDao.createMessage(
            PrefMethods.getStoreData()?.id.toString().trim(), // 66
            senderId.toString().trim() +
                    PrefMethods.getStoreData()?.id.toString().trim(), // 817136
            messageId,
            messageModel
        )

        inboxDao.createMessage(
            senderId.toString().trim(), // 66
            senderId.toString().trim() + PrefMethods.getStoreData()?.id.toString().trim(), // 81766
            messageId,
            messageModel
        )

        isImageUploaded = true

        readMessage(senderId)
    }

    fun uploadImageToSend(
        fileName: Uri,
        storeId: String?,
        senderId: String?,
        avatarUser: String?,
        senderName: String?,
    ) {
        // enqueueSignal(Load)
        val storageReference = FirebaseStorage.getInstance()
            .getReference("PictureImage/")
            .child("${PrefMethods.getUserData()?.id.toString().trim()}/")
            .child(
                "${
                    PrefMethods.getUserData()?.id.toString().trim() + storeId.toString().trim()
                }/"
            )

        val storageReference2 = FirebaseStorage.getInstance()
            .getReference("PictureImage/")
            .child("${senderId.toString().trim()}/")
            .child(
                "${
                    senderId.toString().trim() + PrefMethods.getUserData()?.id.toString().trim()
                }/"
            )

        storageReference.putFile(fileName).addOnSuccessListener { taskSnapshot ->
            val task = taskSnapshot.storage.downloadUrl
            task.addOnCompleteListener { uri: Task<Uri> ->
                if (uri.isSuccessful) {
                    val path = uri.result.toString()
                    sendMessage(avatarUser, senderName, senderId, path, "image")
                }
            }
        }

        storageReference2.putFile(fileName).addOnSuccessListener { taskSnapshot ->
            val task = taskSnapshot.storage.downloadUrl
            task.addOnCompleteListener { uri: Task<Uri> ->
                if (uri.isSuccessful) {
                    val path = uri.result.toString()
                    // sendMessage(path, "image")
                }
            }
        }
    }
}

