package com.brandsin.store.realtimeDatabase.dao

import com.brandsin.store.realtimeDatabase.RealtimeDatabase
import com.brandsin.user.ui.chat.model.MessageModel
import com.google.firebase.database.Query

class InboxDao {
    private var realtimeDatabase: RealtimeDatabase = RealtimeDatabase()

    /*fun checkChat(userid: String?, myId: String?): Query {
        val reference = realtimeDatabase.getChatListRef()
            .child(myId!!)
        return reference.orderByChild("member").equalTo(userid)
    }*/

    fun createNewMessage(myId: String?, chatId: String?, chatListModel: MessageModel) {
        realtimeDatabase.getMessageListRef().child(myId!!)
            .child(chatId!!)
            .setValue(chatListModel)
    }

    fun createMessage(
        myId: String?,
        chatId: String?,
        messageId: String?,
        messageModel: MessageModel
    ) {
        realtimeDatabase.getMessageListRef().child(myId!!)
            .child(chatId!!)
            .child(messageId!!)
            .setValue(messageModel)
    }

    fun getInboxListRef(myId: String): Query {
        return realtimeDatabase.getMessageListRef().child(myId).orderByKey()
    }

    fun readMessage(myId: String?, chatId: String?): Query {
        return realtimeDatabase.getMessageListRef()
            .child(myId!!) // 817
            .child(chatId!!)
    }
}