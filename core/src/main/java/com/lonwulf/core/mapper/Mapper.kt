package com.lonwulf.core.mapper

interface Mapper<SOURCE_DATA, DEST_DATA> {
    operator fun invoke(data: SOURCE_DATA): DEST_DATA
}
