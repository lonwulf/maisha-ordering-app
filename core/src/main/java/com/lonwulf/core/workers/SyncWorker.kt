@file:Suppress("LongMethod", "ComplexMethod", "NestedBlockDepth")

package com.lonwulf.core.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lonwulf.core.EcatApp
import com.lonwulf.core.domain.usecase.FetchCategoriesUseCase
import com.lonwulf.core.domain.usecase.FetchCustomerStatusUseCase
import com.lonwulf.core.domain.usecase.FetchHomeDeliveryUseCase
import com.lonwulf.core.domain.usecase.FetchHomePageApiUseCase
import com.lonwulf.core.domain.usecase.FetchPartnerDetailsUseCase
import com.lonwulf.core.domain.usecase.FetchProductsApiUseCase
import com.lonwulf.core.domain.usecase.FetchPromoApiUseCase
import com.lonwulf.core.domain.usecase.FetchStockQuantityUseCase
import com.lonwulf.core.utils.Constant
import com.lonwulf.core.utils.Prefs
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class SyncWorker(
    val context: Context, workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    @Inject
    lateinit var fetchPromoApiUseCase: FetchPromoApiUseCase

    @Inject
    lateinit var fetchProductsApiUseCase: FetchProductsApiUseCase

    @Inject
    lateinit var fetchHomeDeliveryUseCase: FetchHomeDeliveryUseCase

    @Inject
    lateinit var fetchStockQuantityUseCase: FetchStockQuantityUseCase

    @Inject
    lateinit var fetchFetchHomePageApiUseCase: FetchHomePageApiUseCase

    @Inject
    lateinit var fetchCategoriesUseCase: FetchCategoriesUseCase

    @Inject
    lateinit var fetchCustomerStatusUseCase: FetchCustomerStatusUseCase

    @Inject
    lateinit var fetchPartnerDetailsUseCase: FetchPartnerDetailsUseCase


    init {
        EcatApp.coreComponent(context)?.inject(this)
    }

    override suspend fun doWork(): Result = coroutineScope {
        try {
            val noConstraintsJobs = listOf(async {
                fetchProductsApiUseCase().collect { /* ignore result */ }
            }, async {
                fetchPromoApiUseCase().collect { /* ignore result */ }
            }, async {
                fetchCategoriesUseCase().collect { /* ignore result */ }
            }, async {
                fetchFetchHomePageApiUseCase().collect { /* ignore result */ }
            }, async {
                fetchStockQuantityUseCase().collect { /* ignore result */ }
            })

            val isPartnerAgent =
                Prefs.getString(Constant.PrefKeys.PARTNER_TYPE) == Constant.UserType.AGENT
            val isUserLoggedIn = Prefs.getBoolean(Constant.PrefKeys.IS_LOGGED_IN)

            val customerConstraintsJobs = if (!isPartnerAgent && isUserLoggedIn) {
                listOfNotNull(
                    async { fetchCustomerStatusUseCase().collect { /* ignore result */ } },
                )
            } else {
                emptyList()
            }

            val partnerConstraintsJobs = if (isUserLoggedIn) {
                listOfNotNull(async { fetchPartnerDetailsUseCase().collect { /* ignore result */ } })
            } else {
                emptyList()
            }

            noConstraintsJobs.awaitAll()
            customerConstraintsJobs.awaitAll()
            partnerConstraintsJobs.awaitAll()

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}
