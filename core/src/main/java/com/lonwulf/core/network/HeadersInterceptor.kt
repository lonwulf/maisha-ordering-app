package com.lonwulf.core.network

import android.content.Context
import com.lonwulf.core.utils.Constant
import com.lonwulf.core.utils.Prefs
import com.lonwulf.core.utils.Utils
import okhttp3.Interceptor

class HeadersInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = if (chain.request().url.toString().contains("proto/add")) {
            chain.request().newBuilder().build()
        } else {
            val removeAllAuthHeaders = awsUrlsWithNoAuth.contains(chain.request().url.toString())
            val removeSomeAuthHeaders = nearestAgentUrl.contains(chain.request().url.toString())
            val req = chain.request().newBuilder()
            if (removeAllAuthHeaders.not()) {
                val apiKey =
                    if (removeSomeAuthHeaders.not()) {
                        apiKey(
                            chain.request().url.toString()
                        )
                    } else {
                        Constant.getNearestAgentApiKey()
                    }
                val commonHeaders = req.addHeader("X-APP-ID", Constant.getAppID())
                    .addHeader("X-API-KEY", apiKey)
                    .addHeader(
                        "X-COMPANY-ID",
                        Prefs.getInt(Constant.MultiCompanyKeys.COUNTRY_ID).toString()
                    )
                    .addHeader("APP-VERSION", Utils.getBuildVersionName(context))
                if (removeSomeAuthHeaders.not()) {
                    commonHeaders.addHeader("X-FID", Constant.getFireBaseId())
                }
                commonHeaders.build()
            } else {
                req.build()
            }
        }
        return chain.proceed(request)
    }

    // APIS that need auth
    // all odoo endpoints

    // APIS that remove X-FID & use AWS api key (isPartAuthorized = true)
    // https://v1.prod.agents.svc.copiaglobal.io/nearbyV2
    private val nearestAgentUrl = listOf(
        "https://v1.prod.agents.svc.copiaglobal.io/nearbyV2"
    )

    // APIS that don't need auth at all (isNotAuthorized = true)
    private val awsUrlsWithNoAuth = listOf(
        "https://dev.nonprod.catalogue.svc.copiaglobal.io/getProductQty/",
        "https://uat.nonprod.catalogue.svc.copiaglobal.io/getProductQty/",
        "https://v2.prod.catalogue.svc.copiaglobal.io/products/",
        "https://sync.prod.catalogue.svc.copiaglobal.io/products.json",
        "https://sync.nonprod.catalogue.svc.copiaglobal.io/products.json",
        "https://promotion.qa.svc.copiaglobal.io/promotion/config",
        "https://promotion.prod.svc.copiaglobal.io/promotion/config"
    )

    private val awsBaseUrlsThatNeedAuth = listOf(
        "https://v2.prod.catalogue.svc.copiaglobal.io",
        "https://delivery-charge.dev.svc.copiaglobal.io/api/v2/deliverycharges/fetch_holidays",
        "https://delivery-charge.prod.svc.copiaglobal.io/api/v2/deliverycharges/fetch_holidays",
    )
    private fun apiKey(requestUrl: String): String {
        // Check if any base URL is a substring of the request URL
        val needsAWSAuth = awsBaseUrlsThatNeedAuth.any { baseUrl ->
            requestUrl.startsWith(baseUrl) || requestUrl.startsWith("https://$baseUrl")
        }
        return if (needsAWSAuth) {
            Constant.getProdDeltaProductSyncApiKey()
        } else {
            Constant.getApiKey()
        }
    }
}
