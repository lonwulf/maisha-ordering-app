package com.lonwulf.core.data.response.catalog

import com.squareup.moshi.Json

data class HomePageResponseDto(
    @Json(name = "data")
    val data: SectionDto? = null
)
