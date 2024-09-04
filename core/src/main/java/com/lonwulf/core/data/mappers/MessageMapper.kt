package com.lonwulf.core.data.mappers

import com.lonwulf.core.domain.models.MessageModel
import com.lonwulf.core.domain.models.MessageStructure
import com.lonwulf.core.objectboxdb.entity.MessageCacheEntityOB
import com.lonwulf.core.utils.EntityMapper
import com.lonwulf.core.utils.Utils
import java.util.Date
import javax.inject.Inject

class MessageMapper @Inject constructor() : EntityMapper<MessageCacheEntityOB, MessageStructure> {

    fun entityListToMessageList(entityList: List<MessageCacheEntityOB>): List<MessageStructure> {
        val list: ArrayList<MessageStructure> = ArrayList()
        for (entity in entityList) {
            list.add(mapFromEntity(entity))
        }
        return list
    }

    override fun mapFromEntity(entity: MessageCacheEntityOB): MessageStructure {
        val timeDiff: String = entity.createdAt?.let { Utils.getFormattedTimeDifference(it) } ?: ""
        return MessageStructure(
            title = entity.title ?: "",
            message = entity.message ?: "",
            body = entity.body ?: "",
            senderId = entity.senderId ?: "",
            productId = entity.productId ?: 0,
            promotionId = entity.promotionId ?: 0,
            url = entity.url ?: "",
            isRead = entity.isRead ?: false,
            id = entity.id,
            dateCreated = entity.createdAt,
            productCodes = entity.productCodes ?: listOf(),
            categoryNames = entity.categoryNames ?: listOf(),
            timeDifference = timeDiff,
            isLinked = entity.isLinked,
            sectionIdentifier = entity.sectionIdentifier,
            sectionId = entity.sectionId
        )
    }

    private fun mapModelToStructure(model: MessageModel): MessageStructure {
        return MessageStructure(
            title = model.title ?: "",
            message = model.message ?: "",
            body = model.body ?: "",
            senderId = model.sender_id ?: "",
            productId = model.productId,
            promotionId = model.promotionId,
            url = model.url ?: "",
            isRead = model.isRead ?: false,
            id = model.id,
            dateCreated = model.dateCreated,
            productCodes = model.product_codes ?: listOf(),
            categoryNames = model.category_names ?: listOf(),
            isLinked = model.isLinked,
            sectionIdentifier = model.sectionIdentifier,
            sectionId = model.sectionId
        )
    }

    fun messageItemToEntity(message: MessageModel): MessageCacheEntityOB {
        val item = mapModelToStructure(model = message)
        return mapToEntity(item)
    }

    override fun mapToEntity(model: MessageStructure): MessageCacheEntityOB = MessageCacheEntityOB(
        id = model.id, title = model.title,
        message = model.message,
        body = model.body,
        senderId = model.senderId,
        productId = model.productId,
        promotionId = model.promotionId,
        url = model.url,
        isRead = model.isRead,
        productCodes = model.productCodes,
        categoryNames = model.categoryNames,
        createdAt = model.dateCreated ?: Date(),
        isLinked = model.isLinked ?: false,
        sectionIdentifier = model.sectionIdentifier,
        sectionId = model.sectionId

    )
}
