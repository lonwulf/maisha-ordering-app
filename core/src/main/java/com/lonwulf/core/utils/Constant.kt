@file:Suppress("TooManyFunctions")

package com.lonwulf.core.utils

import com.lonwulf.core.BuildConfig
import com.lonwulf.core.utils.Constant.PrefKeys.IMAGE_ENVIRONMENT
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Constant {
    var HOMEPAGE_GRID_ITEM_MAX_COUNT = 0
    val IS_DEBUG: Boolean
        get() {
            return when (BuildConfig.BUILD_TYPE) {
                "debug" -> true
                "release" -> false
                else -> {
                    true
                }
            }
        }

    val categoryImages = mutableListOf<String>()
    val productImages = mutableListOf<String>()
    val homePageImages = mutableListOf<String>()
    val pseudoCategoryImages = mutableListOf<String>()
    const val PLAYSTORE_URL =
        "\n https://play.google.com/store/apps/details?id=com.lonwulf "

    const val TEST_SMS_KEYWORD = "test " // qa

    const val T_N_C_URL = "https://copia.co.ke/terms-conditions-ca/"
    const val PRIVACY_URL = "https://copia.co.ke/privacy-policy-ca/"
    const val FAQ_URL = "https://copia.co.ke/faqs-ca/"
    var LAST_SYNC_IMAGE_SIGNATURE: Long = 0
    const val DATABASE_NAME = "ecatalogue.db"
    const val PREF_NAME = "app_pref"
    const val UNKNOWN_ERROR = "Unknown error"
    const val CACHE_ERROR = "cache error"
    const val CACHE_TIMEOUT = 2000L
    const val NETWORK_TIMEOUT = 60L
    const val NETWORK_ERROR_TIMEOUT = "Network timeout"
    const val NETWORK_ERROR_UNKNOWN = "Unknown network error"
    const val HORIZONTAL_RECYCLERVIEW = "horizontal_recylerview"
    const val VIEWPAGER = "viewpager"
    const val CATEGORY = "category"
    const val SINGLE = "single"
    const val PSEUDO_CATEGORY = "pseudo_category"
    const val ALL_CATEGORIES: String = "All Categories"
    const val ALL_PRODUCTS_VIEW_TYPE = "ALL_PRODUCTS_VIEW_TYPE"
    const val POPULAR_PRODUCTS_VIEW_TYPE = "POPULAR_PRODUCTS_VIEW_TYPE"
    const val GENERIC_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
    const val GENERIC_DATE_REVERSED_FORMAT = "yyyy-MM-dd"
    const val GENERIC_DATE_FORMAT = "dd-MM-yyyy"
    const val FORMATTED_DATE_FORMAT = "EEE MMM dd yyyy"
    const val FORMATTED_DATE_FORMAT_ = "EEE, dd MMMM"
    const val DAY_MONTH_DATE_FORMAT_ = "dd MMM"
    const val DATE_MONTH_YEAR_FORMATTED = "dd MMM yyyy"
    const val DATE_FULL_MONTH_YEAR_FORMATTED = "dd MMMM yyyy"
    const val MONTH_DATE_YEAR_FORMAT = "MM/dd/yyyy"
    const val KE_CUSTOMER_CARE_TOLL_FREE_NO = "+254800721295"
    const val PARENT_CATEGORY_ID = 2 // Rep root parent ID
    const val MAX_CHUCKER_CONTENT_LENGTH = 250000L
    const val LAST_SYNC_DATE_FORMAT_PATTERN = "yyyy-MM-dd"
    const val ISO_8601_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val MAX_UNREAD_COUNT: Int = 99
    const val FIREBASE_SESSION_TIMEOUT_DURATION = 3600000L
    const val PROCEED_TO_MESSAGE_DETAILS = "_PROCEED_TO_MESSAGE_DETAILS"
    const val PURCHASE_DEAL_ITEM = "_PURCHASE_DEAL_ITEM"
    const val TO_PROMOS_LISTING = "_TO_PROMOS_LISTING"
    const val INIT_DOWNLOAD_SERVICE = "_INIT_DOWNLOAD_SERVICE"

    init {
        System.loadLibrary("Constants")
    }

    external fun getAppERPID(isDebug: Boolean): String

    external fun getXApiKey(): String

    external fun getXAppID(): String

    external fun getUrl(): String

    external fun getUATUrl(): String

    external fun getStagingUrl(): String

    external fun getQAUrl(): String
    external fun getDeliveryFeeQAUrl(): String
    external fun getDeliveryFeeStagingUrl(): String
    external fun getDeliveryFeeProductionUrl(): String
    external fun getDeltaSyncProdUrl(date: String): String
    external fun getDeltaSyncQAUrl(date: String): String
    external fun getProdSyncUrl(): String
    external fun getQASyncUrl(): String

    external fun getHolidaysQAUrl(): String

    external fun getHolidaysStagingUrl(): String

    external fun getHolidaysProductionUrl(): String

    external fun getProductQuantityQAUrl(dateStr: String): String

    external fun getProductQuantityUATUrl(dateStr: String): String

    external fun getProductQuantityProductionUrl(dateStr: String): String

    external fun validateProductQuantityQAUrl(): String

    external fun validateProductQuantityUATUrl(): String

    external fun validateProductQuantityProductionUrl(): String

    external fun getImagesUrl(): String

    external fun getUATImagesUrl(): String

    external fun getQAImagesUrl(): String

    external fun getDBEncyptKey(): String

    external fun getAppStringID(isDebug: Boolean): String

    external fun getPrefEncryptKey(): String

    external fun getProdDeltaProductSyncApiKey(): String

    external fun getQADeltaProductSyncApiKey(): String

    external fun getPromotionsQAUrl(): String

    external fun getPromotionsProductionUrl(): String
    external fun getNearestAgentProdUrl(): String
    external fun getNearestAgentQAUrl(): String
    external fun getNearestAgentApiKey(): String
    external fun getSTKRequestUrl(isDebug: Boolean): String

    external fun getPseudoCategoriesUrl(isDebug: Boolean): String

    val APP_ERP_ID: String = getAppERPID(IS_DEBUG)
    val APP_STRING_ID: String = getAppStringID(IS_DEBUG)
    val ECAT_NOTIFICATION_CHANNEL_NAME: CharSequence = "Ecat WorkManager Notifications"
    val ECAT_NOTIFICATION_CHANNEL_DESCRIPTION = "Shows notifications whenever work starts"
    val NOTIFICATION_TITLE: CharSequence = "WorkRequest Starting"
    val CHANNEL_ID = "VERBOSE_NOTIFICATION"
    val NOTIFICATION_ID = 1
    const val PROGRESS = "PROGRESS"
    const val TAG_PROGRESS = "TAG_PROGRESS"
    const val DELAY_TIME_MILLIS: Long = 5000L

    object ClickEvent {
        val ACTION_DISMISS_HOW_TO_PAY = "how_to_pay"
        val ACTION_DISMISS_JNC_CHARGE = "jnc_charge"
        const val ACTION_CLICK_PARENT_CATEGORY_ITEM = "click_parent_category_item"
        const val ACTION_ADD_TO_CART = "click_parent_category_item"
        const val ACTION_PROCEED_CHECKOUT = "click_parent_category_item"
        const val ACTION_VALIDATE_COUPON = "_ACTION_VALIDATE_COUPON"
    }

    object PUSH_NOTIFICATION_MESSAGE_TYPES {
        const val PROMOTION_MESSAGES = "promotion_messages"
        const val ORDER_MESSAGES = "promotion_messages"
    }

    object Environments {
        const val QA_ENVIRONMENT = "QA"
        const val UAT_ENVIRONMENT = "UAT"
        const val STAGING_ENVIRONMENT = "STAGING"
    }

    object PrefKeys {
        const val CUSTOMER_COD = "customer_cod"
        const val CUSTOMER_VERIFIED = "verified"
        const val CUSTOMER_AGENT_ID = "customer_agent_id"
        const val CUSTOMER_AMOUNT_LIMIT = "_CUSTOMER_AMOUNT_LIMIT"
        const val IS_LOAN_OPT_IN_COMPLETE = "IS_LOAN_OPT_IN_COMPLETE"
        const val SELECTED_ENVIRONMENT = "selected_environment"
        val FIREBASE_ID = "firebase_id"
        val HAS_HDL_PRODUCT = "hdl_product"
        const val WALK_THROUGH_KEY = "show_showcase"
        const val DOWN_WALK_THROUGH = "done_walk_through"
        const val HOW_TO_PAY_LIST = "how_to_pay"
        const val HOW_TO_PAY_LPP = "how_to_pay_lpp"
        const val HOME_DELIVERY_LIST = "_HOME_DELIVERY_LIST"
        const val show_showcase = "show_showcase"
        const val LAST_SYNC_TIME_STAMP = "last_sync_timestamp"
        const val DEACTIVATED_LAST_SYNC_TIME_STAMP = "_DEACTIVATED_LAST_SYNC_TIME_STAMP"
        const val LAST_SYNC_DATE = "last_sync_date"
        const val SOMEONE_ELSE_PHONE = "someone_else_phone"
        const val PHONE_TO_PAY = "phone_to_pay"
        const val AMOUNT_TO_PAY = "amount_to_pay"
        const val OC_PHONE = "oc_phone"
        const val RC_PHONE = "rc_phone"
        const val CUSTOMER_LPP = "customer_lpp"
        const val ORDER_TYPE = "order_type"
        const val SOMEONE_ELSE_FLAG = "order_someone_else"
        const val CHECKOUT_REGISTRATION_FLAG = "checkout registration"
        const val PARTNER_NAME = "name"
        const val PARTNER_FIRST_NAME = "first_name"
        const val PARTNER_LAST_NAME = "last_name"
        const val AGE_GROUP = "age_group"
        const val PARTNER_GENDER = "gender"
        const val PARTNER_DATA_ID = "partner_data_id"
        const val PARTNER_PHONE_NUMBER = "phoneNumber"
        const val PARTNER_TYPE = "partner_type"
        const val PARTNER_ID = "partner_id"
        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"
        const val EXPIRES = "expires"
        const val USER_TYPE = "user_type"
        const val UUID = "uuid"
        const val PARTNER_LOCATION = "uuid"
        const val IS_LOGGED_IN = "is_logged_in"
        const val LAST_SYNC_IMAGE_SIGNATURE = "last_sync_image_signature"
        const val ERP_PROD_APP_ID = "erp_prod_app_id"
        const val ERP_PROD_API_KEY = "erp_prod_api_key"
        const val ERP_UAT_APP_ID = "uat_app_id"
        const val ERP_UAT_API_KEY = "uat_api_key"
        const val API_KEY = "api_key"
        const val APP_ID = "app_id"
        const val ERP_QA_APP_ID = "qa_app_id"
        const val ERP_QA_API_KEY = "qa_api_id"
        const val ERP_ENVIRONMENT = "erp_environment"
        const val IMAGE_ENVIRONMENT = "image_environment"
        const val LOAN_PROGRAMS = "loan_programs"
        const val LOAN_PROGRAM_INVITED = "loan_program_invited"
        const val LOAN_OPTED_IN = "loan_opted_in"
        const val LOAN_OPTED_OUT = "loan_opted_out"
        const val LOAN_PROGRAM_ID = "loan_program_id"
        const val LOAN_CURRENT_LIMIT = "loan_current_limit"
        const val LOAN_PARTNER_ID = "loan_partner_id"
        const val LOAN_PARTNER_NAME = "loan_partner_name"
        const val LOAN_PRODUCTS = "loan_products"
        const val COPIA_PESA_AMOUNT = "copia_pesa_amount"
        const val RECEIVING_PARTNER_ID = "_RECEIVING_PARTNER_ID"
        const val RECEIVING_AGENT_PHONE = "_RECEIVING_AGENT_PHONE"

        const val BECOME_AGENT_FIRST_NAME = "b_first_name"
        const val BECOME_AGENT_LAST_NAME = "b_last_name"
        const val BECOME_AGENT_AGE_GROUP = "b_age_group"
        const val PRICE_LIST_SYNCED = "price_list_synced"
        const val RECEIVING_AGENT = "receiving_agent"
        const val PREVIOUS_RECEIVING_AGENT = "previous_receiving_agent"
        const val COPIA_ASSIGN_AGENT = "copia_assign_receiving_agent"

        const val COPIA_PESA_NAME = "copia_pesa_name"
        const val COPIA_PESA_URL = "copia_pesa_url"
        const val PRODUCTS_SYNCED = "products_synced"
        const val BOX_STORE_ID = "box_store_id"
        const val PARTNER_CREDIT_AVAILABLE = "partner_credit_available"
        const val AGENT_ASSIGNED_TO = "agent_assigned_to"
        const val PARTNER_PHOTO = "partner_photo"
        const val AGENT_CLASS = "agent_class"
        const val DEACTIVATION_REASON = "deactivation_reason"
        const val IS_PREPAY_AGENT = "is_prepay_agent"
        const val LATITUDE = "latitude"
        const val LOCATION = "location"
        const val LONGITUDE = "longitude"
        const val NO_COPIA_PESA_REDEMPTION = "no_copiapesa_redemption"
        const val PRICE_LIST = "price_list"
        const val PRICE_LIST_ID = "price_list_id"
        const val WAREHOUSE = "warehouse"
        const val FAQ_URL = "faq_url"
        const val PRIVACY_URL = "privacy_url"
        const val TNC_URL = "tnc_url"
        const val FRESHCHAT_APP_ID = "freshchat_app_id"
        const val FRESHCHAT_APP_KEY = "freshchat_app_key"
        const val CUSTOMER_LIMIT = "customer_limit"
        const val FRESHCHAT_UNREAD_COUNT = "freshchat_unread_count"
        const val UNREAD_MESSAGES_COUNT = "unread messages count"
        const val DELIVERY_FEE = "delivery_fee"
        const val DELIVERY_CHARGE = "delivery_charge"
        const val AGENT_DELIVERY_DAYS = "delivery_days"
        const val HOLIDAYS = "holidays"
        const val DELIVERY_CLASS = "delivery_class"
        const val PREVIOUS_RA_DELIVERY_CLASS = "delivery_class"
        const val EXPECTED_DELIVERY_DATE = "expected_delivery_date"
        const val BASKET_COUNT_LIMIT = "_BASKET_COUNT_LIMIT"
        const val REFERRAL_TERMS = "REFERRAL_TERMS"
        const val DELIVERY_CONFIG_SYNC_DATE = "DELIVERY_CONFIG_SYNC_DATE"
        const val HOLIDAYS_CONFIG_SYNC_DATE = "HOLIDAYS_CONFIG_SYNC_DATE"
        const val IS_FROM_ORDER_SUMMARY = "isFromOrderSummary"
        const val RA_SET_KEY = "RA_SET_KEY"
        const val IS_FROM_DELIVERYPOINT = "isFromDeliveryPoint"
        const val COUNTRY_CODE = "_COUNTRY_CODE"
    }

    object Variants {
        const val API_CALL = "api_call"
        const val SMS_CALL = "sms_call"
    }

    object UserType {
        const val CUSTOMER = "customer"
        const val AGENT = "agent"
        const val ASSOCIATE = "associate"
        const val TRADER = "trader"
    }

    enum class Features {
        CHECKOUT,
        HOME,
        PROFILE,
        SHOP
    }

    enum class ORDER_TYPES {
        CJO,
        JNC,
        LPP,
        NORMAL_ORDER,
        LOAN,
        CUSTOMER_LPP,
        RA_SELECTION
    }

    enum class CopiaPesaFields {
        NAME, URL
    }

    enum class OtherFields {
        FAQ, PRIVACY, TNC
    }

    enum class FreshChatFields {
        APP_ID, APP_KEY
    }

    enum class OrderTypes {
        CJO,
        JNC,
        LPP,
        NORMAL_ORDER,
        LOAN,
        CUSTOMER_LPP,
        RA_SELECTION
    }

    enum class AsanteRepaymentPeriod(val period: String) {
        ONE_DAY("1_day"),
        THREE_DAY("3_day"),
        SEVEN_DAY("7_day");

        override fun toString(): String {
            return period
        }
    }

    object SmsKeyWords {
        val COPIA_SMS_SHORTCODE = Prefs.getString(MultiCompanyKeys.PRIMARY_SMS_SHORTCODE) ?: "22272"
        const val AGENT_SMS_KEYWORD = "a_ecat"
        const val CUSTOMER_SMS_KEYWORD = "C_BUY"
        const val AGENT_LPP_KEYWORD = "a_lpp"
        const val CUSTOMER_LPP_KEYWORD = "lpp"
        const val SALES_ASSOCIATES_SMS_KEYWORD = "s_ecat"
        const val SALES_ASSOCIATES_LPP_KEYWORD = "s_lpp"
        const val REMOTE_SALES_ASSOCIATES_SMS_KEYWORD = "rs_ecat"
        const val REMOTE_AGENT_SMS_KEYWORD = "ra_ecat"
        const val COPIA_JAMII = "a_jamii"
        const val JNC_ORDER_KEYWORD = "STOCK_ECAT"
        const val LOAN_ORDER_KEYWORD = "A_LOAN ORDER"
        const val JOIN_KEYWORD = "Join "
        const val RESET_PASSWORD_KEYWORD = "reset "
        const val RA_SMS_KEYWORD = "NUNUA"
        const val C_COD_KEYWORD = "C_COD"
        const val CANCEL_ORDER_KEYWORD = "cancel"
        const val REFER_AGENT_KEYWORD = "ref"
        const val REFER_CUSTOMER_KEYWORD = "ref"
        const val ATTACH_PAYMENT_KEYWORD = "pay"
        const val C_POD_KEYWORD = "C_POD"
    }

    object MultiCompanyKeys {
        const val COUNTRY = "_COUNTRY"
        const val PHONE = "_PHONE"
        const val COUNTRY_ID = "_COUNTRYID"
        const val EMAIL = "_EMAIL"
        const val COUNTRY_NAME = "_NAME"
        const val COUNTRY_CODE = "_CODE"
        const val PHONE_CODE = "_PHONE_CODE"
        const val PRIMARY_SMS_SHORTCODE = "PRIMARY_SMS_CODE"
        const val COUNTRY_CURRENCY_NAME = "COUNTRY_CURRENCY"
        const val COUNTRY_CURRENCY_SYMBOL = "COUNTRY_SYMBOL"
        const val COUNTRY_LIST = "_COUNTRY_LIST"
    }

    object RemoteConfig {
        const val mpesa = "mpesa_payment_info"
        const val equitel = "equitel_payment_info"
    }

    object OrderStatus {
        const val CANCEL_KEY = "cancel"
        const val CANCELLED = "Order cancelled"
        const val PAYMENT_REQUIRE_KEY = "payment_required"
        const val PAYMENT_REQUIRED = "Payment is required"
        const val PARTNER_SHIPPING_REQUIRED = "Receiving Agent is required"
        const val REJECT_KEY = "rejected"
        const val PARTNER_SHIPPING_REQUIRED_KEY = "partner_shipping_required"
        const val REJECTED = "Order rejected"
        const val DRAFT_KEY = "draft"
        const val DRAFT = "Pending confirmation"
        const val LOAN_APPROVAL_PENDING_KEY = "loan_approval_pending"
        const val LOAN_APPROVAL_PENDING = "Pending"
        const val SALE_KEY = "sale"
        const val SALE = "Confirmed"
        const val LOCKED_KEY = "done"
        const val LOCKED = "Done"
        const val JNC_CONFIRMED_KEY = "jnc_confirmed"
        const val JNC_CONFIRMED = "Done"
        const val DEFAULT_STATUS = "-"
        const val PAID = "Paid"
        const val UNPAID = "Unpaid"
    }

    object FirebaseKeys {
        const val FIREBASE_ERP_PRODUCTION = "erp_prod"
        const val FIREBASE_ERP_QA = "erp_qa"
        const val FIREBASE_ERP_UAT = "erp_uat"
        const val FIREBASE_APP_ID = "app_id"
        const val FIREBASE_API_KEY = "api_key"
        const val DEACTIVATED_FEATURES = "disabled_features"
        const val CONFIGS = "configs"
        const val HDL_MESSAGE = "hdl_message"
        const val HDL_AMOUNT = "hdl_amount"
        const val COPIA_PESA = "copia_pesa"
        const val OTHER = "other"
        const val FRESHCHAT = "freshchat"
        const val BASKET_VALUE = "basket_value"
        const val REFERRAL_TERMS = "referral_terms"
    }

    object AnalyticsActions {
        const val NAV = "nav"
        const val BOTTOM_SHEET_BUY_NOW = "bottom_sheet_buy_now"
        const val FAVOURITE = "favourite"
        const val GRID_VIEW = "grid"
        const val LIST_VIEW = "list"
        const val QUANTITY = "quantity"
        const val ATTACH_PAYMENT = "attach_payment"
        const val PLACE_ORDER = "place_order"
        const val TO_PAYMENTS = "to_payments"
        const val TO_ORDER_SUMMARY = "to_order_summary"
        const val PROCEED_TO_REGISTRATION_FROM_CHECKOUT = "proceed_to_registration_from_checkout"
    }

    object AnalyticSearchTypes {
        const val CUSTOMERS_SEARCH = "agent_customer_search"
        const val PRODUCTS_SEARCH = "products_search"
    }

    object JNCKeys {
        const val IS_PARTNER_JNC_ENABLED = "JNC_STATUS_KEY"
        const val JNC_STATUS_CODE = "JNC_STATUS_CODE"
        const val JNC_STATUS_FROM_DATE = "JNC_STATUS_FROM_DATE"
        const val JNC_STATUS_TO_DATE = "JNC_STATUS_TO_DATE"
        const val JNC_STATUS_NAME = "JNC_STATUS_NAME"
        const val JNC_STATUS_PROGRAM_ID = "JNC_STATUS_PROGRAM_ID"
    }

    object MessagesSenderTypes {
        const val SENDER_COPIA = "copia"
        const val SENDER_COPIA_DEALS = "copia_deals"
        const val SENDER_INSIDER = "insider"
    }

    object TimeVariation {
        const val DAY = "days"
        const val HOURS = "hours"
        const val MINUTES = "minutes"
        const val SECONDS = "seconds"
    }

    object MessageUriExtras {
        const val MESSAGES_FRAGMENT_EXTRAS = "messages://extras"
    }

    enum class MessagesScreenFlag {
        ORDERS_MESSAGES_SCREEN, PROMOTION_MESSAGES_SCREEN, PRODUCTS_LISTING_SCREEN, CATEGORY_NAME
    }

    object AppModules {
        const val HOME = "home"
    }

    object CartUtilConstants {
        const val COUPON_DISCOUNT = "_COUPON_DISCOUNT"
        const val COUPON_ID = "_COUPON_ID"
    }

    object InternalDeepLink {
        const val DOMAIN = "com.lonwulf://"

        const val HOME = "${DOMAIN}home"
        const val CATEGORIES = "${DOMAIN}categories"
        const val CART = "${DOMAIN}cart"
        const val PROFILE = "${DOMAIN}profile"

        fun makeCustomDeepLink(id: String): String {
            return "${DOMAIN}customDeepLink?id=$id"
        }
    }

    object SortTypes {
        const val ASCENDING = "ascending"
        const val DESCENDING = "descinding"
        const val JISTOCK = "jistock"

        object Values {
            const val ASCENDING = 0
            const val DESCENDING = 1
            const val JISTOCK = 2
        }
    }

    object Pagination {
        const val offset = 0L
        const val limit = 20L
    }

    object FreshchatTags {
        const val MOBILE_SDK = "mobile_sdk"
        const val WEB_SDK = "web_sdk"
    }

    enum class PseudoCategories {
        PROMOTE, TOP, CLEARANCE, APP_ONLY
    }

    fun getDeactivatedViewsIds(featureName: String): List<String> {
        var deactiveFeatureList = ArrayList<String>()
        val gson = Gson()
        val json = Prefs.getString(featureName)
        if (!isListOfDeactivatedFeaturesEmpty(json)) {
            val type = object : TypeToken<ArrayList<String>>() {}.type
            deactiveFeatureList = gson.fromJson(json, type)
        }
        return deactiveFeatureList
    }

    private fun isListOfDeactivatedFeaturesEmpty(savedList: String?): Boolean {
        return savedList.isNullOrEmpty()
    }

    fun getCustomizeApiErrorMessage(message: String): String {
        return when (message) {
            "Invalid user" -> "Agent is Deactivated.Contact Support"
            "Something went wrong" -> "Problem with OTP. Contact Support"
            else -> message
        }
    }

    fun getOrderStatus(status: String?): String {
        return when (status) {
            OrderStatus.CANCEL_KEY -> {
                OrderStatus.CANCELLED
            }

            OrderStatus.PAYMENT_REQUIRE_KEY -> {
                OrderStatus.PAYMENT_REQUIRED
            }

            OrderStatus.REJECT_KEY -> {
                OrderStatus.REJECTED
            }

            OrderStatus.PARTNER_SHIPPING_REQUIRED_KEY -> {
                OrderStatus.PARTNER_SHIPPING_REQUIRED
            }

            OrderStatus.DRAFT_KEY -> {
                OrderStatus.DRAFT
            }

            OrderStatus.LOAN_APPROVAL_PENDING_KEY -> {
                OrderStatus.LOAN_APPROVAL_PENDING
            }

            OrderStatus.SALE_KEY -> {
                OrderStatus.SALE
            }

            OrderStatus.LOCKED_KEY -> {
                OrderStatus.LOCKED
            }

            OrderStatus.JNC_CONFIRMED_KEY -> {
                OrderStatus.JNC_CONFIRMED
            }

            null -> {
                OrderStatus.DEFAULT_STATUS
            }

            else -> {
                status
            }
        }
    }

    fun getBaseImageUrl(): String {
        val imageUrl: String?
        if (IS_DEBUG) {
            imageUrl = Prefs.getString(IMAGE_ENVIRONMENT)
            if (imageUrl.isNullOrEmpty()) {
                return getUATImagesUrl()
            }
        } else {
            imageUrl = getImagesUrl()
        }
        return imageUrl
    }

    fun getAppID(): String {
        return getXAppID()
    }

    fun getApiKey(): String {
        return getXApiKey()
    }

    fun getFireBaseId(): String {
        return Prefs.getString(PrefKeys.FIREBASE_ID) ?: ""
    }

    enum class JourneyConstants {
        LOGIN_CONSTANTS_KEY, ORDER_PLACEMENT, FROM_PROFILE, TO_ORDER_HISTORY
    }

    fun getDeltaProductSyncApiKey(): String {
        return if (IS_DEBUG) {
            getQADeltaProductSyncApiKey()
        } else {
            getProdDeltaProductSyncApiKey()
        }
    }

    internal enum class PromoChannels(private val channel: String) {
        ECATALOG("ecatalog"),
        A_ECAT("a ecat");

        fun getChannel() = channel
    }
}
