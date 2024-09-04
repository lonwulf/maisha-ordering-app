package com.lonwulf.core.objectboxdb

import com.lonwulf.core.database.entity.Attributes
import com.lonwulf.core.database.entity.Image
import com.lonwulf.core.database.entity.Promos
import com.lonwulf.core.database.entity.Video
import com.lonwulf.core.domain.models.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.objectbox.converter.PropertyConverter

@Suppress("TooGenericExceptionCaught", "SwallowedException")
class ConvertersOB {
    class ImageTypeConverterOB : PropertyConverter<Image?, String?> {
        private val gson = Gson()
        override fun convertToEntityProperty(value: String?): Image? {
            val type = object : TypeToken<Image>() {}.type
            return gson.fromJson(value, type)
        }

        override fun convertToDatabaseValue(image: Image?): String? {
            return gson.toJson(image)
        }
    }

    class AttributesConverter : PropertyConverter<List<Attributes>?, String?> {
        private val gson = Gson()
        override fun convertToEntityProperty(value: String?): List<Attributes>? {
            value?.let {
                val type = object : TypeToken<List<Attributes>>() {}.type
                return gson.fromJson(value, type)
            } ?: return null
        }

        override fun convertToDatabaseValue(attributes: List<Attributes>?): String? {
            return gson.toJson(attributes)
        }
    }

    class VideoTypeConverter : PropertyConverter<Video?, String?> {
        private val gson = Gson()
        override fun convertToEntityProperty(value: String?): Video? {
            val type = object : TypeToken<Video>() {}.type
            return gson.fromJson(value, type)
        }

        override fun convertToDatabaseValue(image: Video?): String? {
            return gson.toJson(image)
        }
    }

    @Deprecated("Not needed")
    class PromosConverter : PropertyConverter<List<Promos>?, String?> {
        private val gson = Gson()
        override fun convertToEntityProperty(value: String?): List<Promos>? {
            val type = object : TypeToken<List<Promos>>() {}.type
            return try {
                gson.fromJson(value, type)
            } catch (e: Exception) {
                emptyList()
            }
        }

        override fun convertToDatabaseValue(promos: List<Promos>?): String? {
            return gson.toJson(promos)
        }
    }

    class ProductsConverter : PropertyConverter<List<Product>?, String?> {
        private val gson = Gson()
        override fun convertToEntityProperty(value: String?): List<Product>? {
            val type = object : TypeToken<List<Product>>() {}.type
            return try {
                gson.fromJson(value, type)
            } catch (e: Exception) {
                emptyList()
            }
        }

        override fun convertToDatabaseValue(products: List<Product>?): String? {
            return gson.toJson(products)
        }
    }

    class ProductIdsConverter : PropertyConverter<List<Int>?, String?> {
        private val gson = Gson()
        override fun convertToEntityProperty(value: String?): List<Int>? {
            val type = object : TypeToken<List<Int>>() {}.type
            return try {
                gson.fromJson(value, type)
            } catch (e: Exception) {
                emptyList()
            }
        }

        override fun convertToDatabaseValue(productIds: List<Int>?): String? {
            return gson.toJson(productIds)
        }
    }

    class ProductCodesConverter : PropertyConverter<List<String>?, String?> {
        private val gson = Gson()
        override fun convertToEntityProperty(value: String?): List<String>? {
            val type = object : TypeToken<List<Int>>() {}.type
            return try {
                gson.fromJson(value, type)
            } catch (e: Exception) {
                emptyList()
            }
        }

        override fun convertToDatabaseValue(productIds: List<String>?): String? {
            return gson.toJson(productIds)
        }
    }
}
