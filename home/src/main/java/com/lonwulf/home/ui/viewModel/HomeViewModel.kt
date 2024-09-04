package com.lonwulf.home.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.lonwulf.core.data.mappers.mapCategoriesSectionToDomain
import com.lonwulf.core.data.mappers.mapPseudoCategoriesToSectionDomain
import com.lonwulf.core.dispatcher.CoroutineDispatcherProvider
import com.lonwulf.core.domain.models.CategoryItems
import com.lonwulf.core.domain.models.Product
import com.lonwulf.core.domain.models.Section
import com.lonwulf.core.domain.usecase.FetchCategoriesUseCase
import com.lonwulf.core.domain.usecase.FetchCustomerStatusUseCase
import com.lonwulf.core.domain.usecase.FetchHomeDeliveryUseCase
import com.lonwulf.core.domain.usecase.FetchHomePageApiUseCase
import com.lonwulf.core.domain.usecase.FetchPartnerDetailsUseCase
import com.lonwulf.core.domain.usecase.FetchProductsApiUseCase
import com.lonwulf.core.domain.usecase.FetchPromoApiUseCase
import com.lonwulf.core.domain.usecase.FetchStockQuantityUseCase
import com.lonwulf.core.state.GenericApiResultState
import com.lonwulf.core.utils.Constant
import com.lonwulf.core.utils.Constant.UserType.CUSTOMER
import com.lonwulf.core.utils.Prefs
import com.lonwulf.core.utils.SingleLiveData
import com.lonwulf.core.utils.Utils
import com.lonwulf.home.usecase.QueryCategoriesUseCase
import com.lonwulf.home.usecase.QueryHomePageSectionsUseCase
import com.lonwulf.home.usecase.UpdateCustomerLocationUseCase
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@Suppress("LongParameterList")
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchHomeDeliveryUseCase: FetchHomeDeliveryUseCase,
    private val queryCategoriesUseCase: QueryCategoriesUseCase,
    private val fetchCategoriesUseCase: FetchCategoriesUseCase,
    private val queryHomePageSectionsUseCase: QueryHomePageSectionsUseCase,
    private val fetchFetchHomePageApiUseCase: FetchHomePageApiUseCase,
    private val dispatcherProvider: CoroutineDispatcherProvider,
    private val cartItemUseCase: CartItemUseCase,
    private val fetchPromoApiUseCase: FetchPromoApiUseCase,
    private val fetchCustomerStatusUseCase: FetchCustomerStatusUseCase,
    private val fetchPartnerDetailsUseCase: FetchPartnerDetailsUseCase,
    private val fetchProductsApiUseCase: FetchProductsApiUseCase,
    private val fetchStockQuantityUseCase: FetchStockQuantityUseCase,
    private val updateCustomerLocationUseCase: UpdateCustomerLocationUseCase
) : ViewModel() {
    private val mutableSectionList = mutableListOf<Any>()
    val homePageSectionLiveData: SingleLiveData<List<Any>> = SingleLiveData()
    private var categoryItemsList: List<CategoryItems> = ArrayList()
    private var sectionsList = listOf<Section>()
    val homepageState: MutableLiveData<GenericApiResultState> = MutableLiveData()
    val showLoanIntro: SingleLiveData<Boolean> = SingleLiveData()
    val onProductDomainClick: SingleLiveData<Product> =
        SingleLiveData()
    val homePageSyncedLiveData: SingleLiveData<HomePageSynced> = SingleLiveData()

    companion object HomepageDataSections {
        const val CALL_CUSTOMER_CARE_INDEX = 0
        const val SET_LOCATION_BANNER_INDEX = 1
    }

    init {
        remoteConfigRepository.initialize {
            // ignore for now
        }
    }

    fun performDataRefresh() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val noConstraintsJobs = listOf(
                    async {
                        fetchProductsApiUseCase().collect { /* ignore result */ }
                    },
                    async {
                        fetchPromoApiUseCase().collect { /* ignore result */ }
                    },
                    async {
                        fetchCategoriesUseCase().collect { /* ignore result */ }
                    },
                    async {
                        fetchFetchHomePageApiUseCase().collect { /* ignore result */ }
                    },
                    async {
                        fetchHomeDeliveryUseCase().collect { /* ignore result */ }
                    },
                    async {
                        fetchStockQuantityUseCase().collect { /* ignore result */ }
                    }
                )

                val isPartnerAgent =
                    Prefs.getString(Constant.PrefKeys.PARTNER_TYPE) == Constant.UserType.AGENT
                val isUserLoggedIn = Prefs.getBoolean(Constant.PrefKeys.IS_LOGGED_IN)


                val partnerConstraintsJobs = if (isUserLoggedIn) {
                    listOfNotNull(
                        async { fetchPartnerDetailsUseCase().collect { /* ignore result */ } },
                    )
                } else {
                    emptyList()
                }

                noConstraintsJobs.awaitAll()
                partnerConstraintsJobs.awaitAll()
                prepareData()
                setResult(GenericApiResultState.Success(true))
            }
        } catch (e: Exception) {
            Timber.e(e)
            setResult(
                GenericApiResultState.Error(
                    e.message ?: "Something went wrong, please try again later."
                )
            )
        }
    }

    @Suppress("NestedBlockDepth")
    fun prepareData() {
        setInsiderUserObject()
        setInsiderUserAttributes()
        queryHomePageData()
        // Insider.Instance.enableLocationCollection(true)
        // start insider geofence tracking
        // Insider.Instance.startTrackingGeofence()
    }

    /**
     * Fetch dynamic homepage from db if empty do fetch from API
     *
     */
    private fun queryHomePageData() {
        viewModelScope.launch(IO) {
            Constant.HOMEPAGE_GRID_ITEM_MAX_COUNT = Utils.getScreenWidth() / Utils.maxColumnWidth()
            setResult(GenericApiResultState.Loading)
            val homepageSectionsJob = async {
                queryHomePageSectionsUseCase()
                    .flowOn(dispatcherProvider.io)
                    .collect { sections ->
                        sectionsList = sections
                    }
            }

            val categoriesJob = async {
                queryCategoriesUseCase().collect { categoryItems ->
                    categoryItemsList = categoryItems
                }
            }

            homepageSectionsJob.await()
            categoriesJob.await()
            setParentSections()
            setIsSyncedResult(HomePageSynced.HomepageResult(true))
            setResult(GenericApiResultState.Success(isSuccessful = true))
        }
    }

    private fun setResult(result: GenericApiResultState) = homepageState.postValue(result)

    private fun setParentSections() {
        mutableSectionList.clear()
        val editableSectionsList = mutableListOf<Section>()
        editableSectionsList.addAll(sectionsList)
        // Format positions
        var indexOfTopViewPager = -1
        var indexOfLocationBanner = 0
        if (isValidCustomerCareBannerCondition) {
            indexOfTopViewPager += 2
            indexOfLocationBanner += 1
        }
        if (isValidLocationBannerCondition) {
            indexOfTopViewPager += 1
        }
        // Remove the sliders first so we can add it at index 0
        val topViewPagerSection = sectionsList.find { it.viewType == Constant.VIEWPAGER }
        topViewPagerSection?.let {
            editableSectionsList.remove(it)
            if (indexOfTopViewPager == -1) indexOfTopViewPager = 0
            mutableSectionList.add(indexOfTopViewPager, it)
        }
        // Other homepage sections
        mutableSectionList.addAll(editableSectionsList)

        // Pseudo categories
        val indexOfPseudoCategories =
            if (indexOfTopViewPager == -1) 0 else indexOfTopViewPager + 1
        val pseudoCategoriesSection: Section =
            pseudoCategoriesList.mapPseudoCategoriesToSectionDomain()
        mutableSectionList.add(indexOfPseudoCategories, pseudoCategoriesSection)
        // Normal categories
        val categoriesSection: Section = categoryItemsList.mapCategoriesSectionToDomain()
        mutableSectionList.add(indexOfPseudoCategories + 1, categoriesSection)
        // Add customer care banner
        if (isValidCustomerCareBannerCondition) {
            mutableSectionList.add(
                CALL_CUSTOMER_CARE_INDEX,
                HomePageAdapterFactory.CUSTOMER_CARE_CONTACT_BANNER
            )
        }
        // Add location banner
        if (isValidLocationBannerCondition) {
            mutableSectionList.add(indexOfLocationBanner, HomePageAdapterFactory.LOCATION_BANNER)
        }
        homePageSectionLiveData.postValue(mutableSectionList)
    }

    private fun setIsSyncedResult(value: HomePageSynced) = homePageSyncedLiveData.postValue(value)

    fun updateCustomerLocation(coordinates: LatLng) {
        viewModelScope.launch(IO) {
            updateCustomerLocationUseCase.invoke(coordinates)
                .onStart { GenericApiResultState.Loading }
                .flowOn(dispatcherProvider.default)
                .collect { results ->
                    val coordinatesResults = results.results?.data
                    coordinatesResults?.latitude?.let {
                        Prefs.putDouble(
                            Constant.PrefKeys.LATITUDE,
                            it
                        )
                    }
                    coordinatesResults?.longitude?.let {
                        Prefs.putDouble(
                            Constant.PrefKeys.LONGITUDE,
                            it
                        )
                    }
                }
        }
    }


    private val isValidCustomerCareBannerCondition =
        (Prefs.getString(Constant.PrefKeys.PARTNER_TYPE) == CUSTOMER) && remoteConfigRepository.canAccessCallOption

    private val isValidLocationBannerCondition = (
        isLocationSet() || (
            Prefs.getString(Constant.PrefKeys.PREVIOUS_RECEIVING_AGENT)
                .isNullOrEmpty().not()
            ) || !isLoggedin()
        ).not()

    private fun isLocationSet() =
        Prefs.getDouble(Constant.PrefKeys.LATITUDE) != 0.0 &&
            Prefs.getDouble(Constant.PrefKeys.LONGITUDE) != 0.0

    private fun isLoggedin() = Prefs.getBoolean(Constant.PrefKeys.IS_LOGGED_IN)
}

sealed class HomePageSynced {
    data class HomepageResult(val value: Boolean) : HomePageSynced()
}
