package com.lonwulf.core.domain.repository


import com.lonwulf.core.data.response.partner.PartnerDetailsDto
import com.lonwulf.core.utils.Result

interface PartnerRepository {
    suspend fun fetchPartnerDetails(): Result<PartnerDetailsDto>

}
