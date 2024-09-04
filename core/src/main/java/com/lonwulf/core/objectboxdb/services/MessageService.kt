package com.lonwulf.core.objectboxdb.services

import android.content.Context
import com.lonwulf.core.EcatApp
import com.lonwulf.core.objectboxdb.entity.MessageCacheEntityOB
import com.lonwulf.core.objectboxdb.entity.MessageCacheEntityOB_
import com.lonwulf.core.objectboxdb.mapMessageEntityToMessageEntityOB
import com.lonwulf.core.objectboxdb.store
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.objectbox.kotlin.and
import io.objectbox.kotlin.equal
import io.objectbox.query.QueryBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MessageService(private val boxService: BoxStoreService) {
    private val msgBox = boxService.getBoxStoreBoxModel(MessageCacheEntityOB::class.java)
    fun insertMessage(messageCacheEntity: MessageCacheEntityOB): Long =
        boxService.store(messageCacheEntity)

    fun deleteMessage(id: Int): Int {
        val msgEntity = msgBox.query(MessageCacheEntityOB_.id equal id)
            .build().use { it.find() }
        msgBox.remove(msgEntity)
        return msgEntity.size
    }

    fun getMessages(): List<MessageCacheEntityOB> {
        return msgBox.query()
            .order(MessageCacheEntityOB_.createdAt, QueryBuilder.DESCENDING)
            .build().find()
    }

    fun getMessagesBySenderId(senderId: String): List<MessageCacheEntityOB> =
        msgBox.query(MessageCacheEntityOB_.senderId equal senderId)
            .order(MessageCacheEntityOB_.createdAt, QueryBuilder.DESCENDING)
            .build().use { it.find() }

    fun findMessageById(messageId: Int): MessageCacheEntityOB =
        msgBox.query(MessageCacheEntityOB_.id equal messageId)
            .order(MessageCacheEntityOB_.createdAt, QueryBuilder.DESCENDING)
            .build().use { it.findUnique()!! }

    fun findCategoryNames(messageId: Int): List<String> =
        msgBox.query(MessageCacheEntityOB_.id equal messageId)
            .order(MessageCacheEntityOB_.createdAt, QueryBuilder.DESCENDING)
            .build().property(MessageCacheEntityOB_.categoryNames).findStrings().toList()

    fun getMessageProductCodes(senderId: String, messageId: Int): Array<String> =
        msgBox.query(MessageCacheEntityOB_.senderId equal senderId and (MessageCacheEntityOB_.id equal messageId))
            .order(MessageCacheEntityOB_.createdAt, QueryBuilder.DESCENDING)
            .build().property(MessageCacheEntityOB_.productCodes).findStrings()

    private fun insertAllMessages(messages: List<MessageCacheEntityOB>) = boxService.store(messages)

    /**
     * todo >> delete this together with entity and dao in a future apk release
     */
    @Suppress("TooGenericExceptionCaught")
    fun dumpFromRoom(context: Context) {
        try {
            val messageDao = EcatApp.coreComponent(context)?.ecatDatabase()?.messageDao()
            CoroutineScope(Dispatchers.IO).launch {
                messageDao?.getAllMessages()?.let {
                    if (it.isNotEmpty()) {
                        insertAllMessages(it.mapMessageEntityToMessageEntityOB())
                        messageDao.deleteAllMessages()
                    }
                }
            }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }
}
