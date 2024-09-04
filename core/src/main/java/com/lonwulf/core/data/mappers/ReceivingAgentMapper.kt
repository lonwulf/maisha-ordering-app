@file:Suppress("PackageNaming")

package com.lonwulf.core.data.mappers

import com.lonwulf.core.data.response.AgentSearchDto
import com.lonwulf.core.data.response.partner.AgentDto
import com.lonwulf.core.domain.models.Agent
import com.lonwulf.core.utils.Utils.isoDateTimeToDate
import java.util.Date
import javax.inject.Inject

fun AgentDto.mapApiResponseToPreviousAgentEntity(): List<PreviousRAsEntityOB> {
    val previousAgents = mutableListOf<PreviousRAsEntityOB>()
    this.data.map { data ->
        previousAgents.add(
            PreviousRAsEntityOB(
                agentImage = data.agent_photo,
                latitude = data.latitude,
                longitude = data.longitude,
                lastOrderDate = isoDateTimeToDate(data.last_order_date)
                    ?: Date(System.currentTimeMillis()),
                name = data.name,
                partnerId = data.id,
                phone = data.phone,
                premiseImage = data.premise_image,
                location = data.shop_location,
                shopName = data.shop_name,
                warehouse = data.warehouse_name,
                deliveryClass = data.delivery_class
            )
        )
    }
    return previousAgents
}

fun AgentSearchDto.mapAgentSearchApiResponseToAgentList(): List<Agent> {
    val agents = mutableListOf<Agent>()
    this.data.map { data ->
        agents.add(
            Agent(
                agentImage = data.agent_image,
                latitude = data.latitude,
                location = data.location,
                longitude = data.longitude,
                name = data.name,
                partnerId = data.partner_id,
                phone = data.phone,
                premiseImage = data.premise_image,
                nearestLandmark = data.nearest_landmark,
                shopName = data.shop_name,
                warehouse = data.warehouse,
                deliveryClass = data.delivery_class
            )
        )
    }
    return agents
}

fun List<PreviousRAsEntityOB>.mapEntityToAgentList(): List<Agent> {
    val agents = mutableListOf<Agent>()
    this.map { previousRAEntityOB ->
        agents.add(
            Agent(
                agentImage = previousRAEntityOB.agentImage,
                latitude = previousRAEntityOB.latitude,
                location = previousRAEntityOB.location,
                longitude = previousRAEntityOB.longitude,
                name = previousRAEntityOB.name,
                partnerId = previousRAEntityOB.partnerId,
                phone = previousRAEntityOB.phone,
                premiseImage = previousRAEntityOB.premiseImage,
                nearestLandmark = "",
                shopName = previousRAEntityOB.shopName,
                warehouse = previousRAEntityOB.warehouse,
                deliveryClass = previousRAEntityOB.deliveryClass
            )
        )
    }
    return agents
}

fun Agent.mapAgentToEntity(): PreviousRAsEntityOB {
    return PreviousRAsEntityOB(
        agentImage = this.agentImage,
        latitude = this.latitude,
        longitude = this.longitude,
        lastOrderDate = Date(System.currentTimeMillis()),
        name = this.name,
        partnerId = this.partnerId,
        phone = this.phone,
        premiseImage = this.premiseImage,
        location = this.location,
        shopName = this.shopName,
        warehouse = this.warehouse,
        deliveryClass = this.deliveryClass
    )
}

fun PreviousRAsEntityOB.mapEntityToAgent(): Agent {
    return Agent(
        agentImage = this.agentImage,
        latitude = this.latitude,
        longitude = this.longitude,
        name = this.name,
        partnerId = this.partnerId,
        phone = this.phone,
        premiseImage = this.premiseImage,
        location = this.location,
        shopName = this.shopName,
        warehouse = this.warehouse,
        deliveryClass = this.deliveryClass,
        nearestLandmark = ""
    )
}

class ReceivingAgentMapper @Inject constructor()
