@file:Suppress("TooGenericExceptionCaught", "SwallowedException", "MagicNumber")

package com.lonwulf.core.network

import android.content.Context
import android.os.Build
import android.text.format.DateFormat
import com.lonwulf.core.utils.Constant
import com.lonwulf.core.utils.Constant.GENERIC_DATE_TIME_FORMAT
import com.lonwulf.core.utils.Prefs
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import okio.buffer
import okio.source
import timber.log.Timber
import java.io.IOException
import java.util.Date

class RemoteLoggerInterceptor(val context: Context) : Interceptor {
    private val fireStore = Firebase.firestore
    private val requestEndPoints = listOf("https://rest.copiakenya.com/cms/v2/products/promos")
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (request.url.toString() in requestEndPoints) {
            val curlString = curlCommandForRequest(request)
            val requestDataToLog = hashMapOf<String, Any>(
                "partner_id" to Prefs.getLong(Constant.PrefKeys.PARTNER_ID, 0),
                if (request.method == "GET") {
                    "body" to "none"
                } else {
                    "body" to request.body.bodyToString()
                },
                "curl_request" to curlString,
                "created_at" to createdAt,
                "app_version" to versionCode()
            )
            val requestEndpoint = request.url.toString().extractEndpoint()
            log(requestDataToLog, "${request.method} -> $requestEndpoint ($createdAt)")
        }
        val response = chain.proceed(request)
        val responseEndpoint = response.request.url.toString().extractEndpoint()
        val bufferedSource = (response.body?.byteStream())?.let { it.source() }?.buffer()
        val responseString = bufferedSource?.readByteArray()?.let { String(it) }
        val responseDataToLog = hashMapOf<String, Any>(
            "partner_id" to Prefs.getLong(Constant.PrefKeys.PARTNER_ID, 0),
            "response_body" to (
                (
                    if ((
                            response.body?.contentLength()
                                ?: 0
                            ) < 1024 * 1024
                    ) {
                        responseString
                    } else {
                        "body too large"
                    }
                    ) ?: "no body"
                ),
            "created_at" to createdAt,
            "app_version" to versionCode()
        )
        if (response.request.url.toString() in requestEndPoints) {
            log(responseDataToLog, "<-- ${response.code} $responseEndpoint ($createdAt)")
        }
        // now we have extracted the response body but in the process
        // we have consumed the original response and can't read it again
        // so we need to build a new one to return from this method
        return response.newBuilder()
            .body(
                ResponseBody.create(
                    response.body?.contentType(),
                    responseString?.toByteArray() ?: byteArrayOf()
                )
            )
            .build()
    }

    private fun log(requestDataToLog: HashMap<String, Any>, documentTag: String) {
        fireStore.collection("logs")
            .document(Prefs.getLong(Constant.PrefKeys.PARTNER_ID, 0).toString())
            .collection(createdAt)
            .document(documentTag)
            .set(requestDataToLog)
            .addOnSuccessListener { documentReference ->
                Timber.i("DocumentSnapshot added with ID: $documentReference")
            }
            .addOnFailureListener { e ->
                Timber.e(e, "Error adding document")
            }
    }

    private val createdAt = DateFormat.format(GENERIC_DATE_TIME_FORMAT, Date().time).toString()
    private fun RequestBody?.bodyToString(): String {
        return try {
            val buffer = Buffer()
            this?.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            "Failed to parse request body"
        }
    }

    private fun curlCommandForRequest(request: Request): String {
        var command = ShellCommand("curl", listOf("-X" to request.method))
        request.headers.names().forEach { element ->
            command = command.argument("-H", "'$element: ${request.header(element)}'")
        }
        val body = request.body
        if (body != null) {
            if (body.contentType() != null) {
                command = command.argument("-H", "'Content-Type: ${body.contentType()}'")
            }
            if (body.contentLength() != (-1).toLong()) {
                command = command.argument("-H", "'Content-Length: ${body.contentLength()}'")
            }
            command = command.argument("-d" to "'${body.bodyToString()}'")
        }
        command = command.argument("-i" to "'${request.url}'")
        return command.toString()
    }

    private fun versionCode(): Long {
        val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            pInfo.longVersionCode
        } else {
            pInfo.versionCode.toLong()
        }
    }

    private fun String.extractEndpoint(): String {
        val indexOfDotCom = this.indexOf(".com")
        val indexOfDotIo = this.indexOf(".io")
        return if (indexOfDotCom != -1) {
            this.substring(indexOfDotCom + 5).replace("/", ".")
        } else if (indexOfDotIo != -1) {
            this.substring(indexOfDotIo + 4).replace("/", ".")
        } else {
            "invalid URL"
        }
    }
}

data class ShellCommand(
    val name: String,
    val arguments: List<Pair<String, String>>
) {
    fun argument(name: String, value: String): ShellCommand {
        return copy(arguments = arguments + Pair(name, value))
    }

    fun argument(arg: Pair<String, String>): ShellCommand {
        return copy(arguments = arguments + arg)
    }

    override fun toString(): String {
        return arguments.fold(name) { prev, el ->
            "$prev \\\n${el.first} ${el.second}"
        }
    }
}
