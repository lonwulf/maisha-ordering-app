package com.lonwulf.core.utils

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

@Throws(JSONException::class)
fun toMap(obj: JSONObject): Map<String, Any> {
    val map = HashMap<String, Any>()
    val keys = obj.keys()
    while (keys.hasNext()) {
        val key = keys.next()
        var value = obj.get(key)
        if (value is JSONArray) {
            value = toList(value)
        } else if (value is JSONObject) {
            value = toMap(value)
        }
        map[key] = value
    }
    return map
}

@Throws(JSONException::class)
fun toList(array: JSONArray): List<Any> {
    val list = ArrayList<Any>()
    for (i in 0..(array.length() - 1)) {
        var value = array.get(i)
        if (value is JSONArray) {
            value = toList(value)
        } else if (value is JSONObject) {
            value = toMap(value)
        }
        list.add(value)
    }
    return list
}
