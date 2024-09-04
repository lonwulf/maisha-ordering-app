@file:Suppress("SwallowedException", "TooManyFunctions", "MagicNumber")

package com.lonwulf.core.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.content.res.Resources
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.TypedValue
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.lonwulf.core.data.response.delivery.DeliveryConfigDto
import com.lonwulf.core.domain.models.DeliveryCharge
import com.lonwulf.core.domain.models.FeeConfig
import com.lonwulf.core.domain.models.Product
import com.lonwulf.core.domain.models.StockInfo
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.useinsider.insider.Insider
import com.useinsider.insider.InsiderProduct
import org.json.JSONObject
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Utils {
    var lastRunTime: Long = 0
    private const val DOUBLE_RUN_TIME_DELTA: Long = 1000

    fun getTodayDate(format: String): String {
        val c = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(c)
    }

    fun getTodayDateMillis(): Long {
        return Calendar.getInstance().timeInMillis
    }

    /**
     * Returns a formatted date time string from current time,
     * returns the first non zero element.
     * "30 days ago", "1 hour ago", "40 minutes ago"
     *
     * @param dateTime Date object
     * @return string representation of time diff
     */
    fun getFormattedTimeDifference(dateTime: Date): String {
        val hashmapOfMonths = hashMapOf(
            1 to "Jan",
            2 to "Feb",
            3 to "March",
            4 to "April",
            5 to "May",
            6 to "June",
            7 to "July",
            8 to "Aug",
            9 to "Sept",
            10 to "Oct",
            11 to "Nov",
            12 to "Dec",

        )

        val diff = Date().time - dateTime.time
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            days != 0L -> {
                val time = getFormattedTime(dateTime, "MM/dd/yyyy").split("/")
                "${time[1]}, ${hashmapOfMonths[time[0].toInt()]} ${time[2]}"
            }

            hours != 0L -> {
                "$hours ${Constant.TimeVariation.HOURS} ago"
            }

            minutes != 0L -> {
                "$minutes ${Constant.TimeVariation.MINUTES} ago"
            }

            seconds != 0L -> {
                "$seconds ${Constant.TimeVariation.SECONDS} ago"
            }

            else -> ""
        }
    }

    /**
     * Takes a Date object and returns string representation based
     * on format passed
     *
     * @param date [Date]
     * @param format date format type
     * @return representation of the date as formatted string
     */
    fun getFormattedTime(date: Date, format: String): String {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(date)
    }

    fun isRunOnce(): Boolean {
        val runTime = System.currentTimeMillis()
        if (runTime - lastRunTime < DOUBLE_RUN_TIME_DELTA) {
            lastRunTime = runTime
            return false
        }
        lastRunTime = runTime
        return true
    }

    fun calculateNoOfColumns(
        context: Context,
        columnWidthDp: Float
    ): Int { // For example columnWidthdp=180
        val displayMetrics = context.resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / columnWidthDp + 0.5).toInt()
    }

    fun formatDecimal(amount: Double): String {
        val formatter = DecimalFormat("#,###,##0")
        return formatter.format(amount)
    }

    fun getHeaderKeyword(requestType: String): String {
        return if (Constant.IS_DEBUG) {
            // for 22272 add test KE and test UG if shared shortcode
            // for 6115
            // -> If sending via sms TEST <ENV> MESSAGE
            // -> via api -> <MESSAGE
            if (requestType == Constant.Variants.API_CALL) {
                ""
            } else { // sms call
                if (Prefs.getString(Constant.PrefKeys.COUNTRY_CODE).equals("KE")) {
                    "TEST " + Prefs.getString(Constant.PrefKeys.SELECTED_ENVIRONMENT) + " "
                } else {
                    "COPIA TEST " + Prefs.getString(Constant.PrefKeys.SELECTED_ENVIRONMENT) + " "
                }
            }
        } else {
            if (requestType == Constant.Variants.API_CALL) {
                ""
            } else {
                "Copia "
            }
        }
    }

    fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    fun maxColumnWidth(): Int {
        val dip = 110f
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dip,
            Resources.getSystem().displayMetrics
        )
        return px.toInt()
    }

    fun getBuildVersion(context: Context): Int {
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            return pInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return 0
    }

    fun getBuildVersionName(context: Context): String {
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            return pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }

    fun callToOrder(fragmentActivity: FragmentActivity, customerCarePhoneNumber: String?) {
        fragmentActivity.startActivity(
            Intent(
                Intent.ACTION_DIAL,
                Uri.fromParts("tel", customerCarePhoneNumber, null)
            )
        )
    }

    /**
     * Get a country's customer care number by country code and partner type
     *
     * @param countryCode
     */
    fun getCustomerCareNumber(countryCode: String?, partnerType: String?): String? {
        if (countryCode.isNullOrEmpty()) {
            return Prefs.getString(Constant.MultiCompanyKeys.PHONE)
        }

        return if (countryCode.equals("KE", true)) {
            when (partnerType) {
                Constant.UserType.CUSTOMER -> {
                    Prefs.getString(Constant.MultiCompanyKeys.PHONE)
                }

                else -> {
                    Constant.KE_CUSTOMER_CARE_TOLL_FREE_NO
                }
            }
        } else {
            Prefs.getString(Constant.MultiCompanyKeys.PHONE)
        }
    }

    /**
     * For context visit:
     * https://stackoverflow.com/questions/64788240/no-activity-found-to-handle-intentact-android-intent-action-view
     */
    fun showCustomTabs(url: String, context: Context, colorId: Int) {
        val customTabsIntent = CustomTabsIntent.Builder()
            .setToolbarColor(ContextCompat.getColor(context, colorId))
            .setShowTitle(true)
            .setStartAnimations(
                context,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            .setExitAnimations(context, android.R.anim.fade_in, android.R.anim.fade_out)
            .build()
        val browserIntent = Intent()
            .setAction(Intent.ACTION_VIEW)
            .addCategory(Intent.CATEGORY_BROWSABLE)
            .setDataAndType(Uri.fromParts("http", "", null), "text/plain")
        var resolvedBrowsers: List<ResolveInfo>
        resolvedBrowsers = context.packageManager
            .queryIntentActivities(browserIntent, PackageManager.MATCH_DEFAULT_ONLY)
        if (resolvedBrowsers.isEmpty()) {
            resolvedBrowsers = context.packageManager
                .queryIntentActivities(browserIntent, PackageManager.MATCH_ALL)
        }

        if (resolvedBrowsers.isNotEmpty()) {
            try {
                customTabsIntent.intent.`package` = resolvedBrowsers[0].activityInfo.packageName
                customTabsIntent.launchUrl(context, Uri.parse(url))
            } catch (e: Exception) {
                e.printStackTrace()
                openWebPage(url, context)
            }
        } else {
            val intentWeb = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            if (intentWeb.resolveActivity(context.packageManager) != null) {
                context.startActivity(intentWeb)
            } else {
                Toast.makeText(context, "No Browser Available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openWebPage(url: String, context: Context) {
        var webpage = Uri.parse(url)
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            webpage = Uri.parse("http://$url")
        }
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    fun openSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri: Uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

    fun thousandFormatter(
        total: String?,
        showCurrency: Boolean = false,
        alwaysShowDecimals: Boolean = true
    ): String {
        val formattedToBigDecimal = total?.let { BigDecimal(total) } ?: BigDecimal.ZERO
        val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault()) as DecimalFormat
        val symbols = DecimalFormatSymbols()
        symbols.currencySymbol =
            if (showCurrency) "${Prefs.getString(Constant.MultiCompanyKeys.COUNTRY_CURRENCY_NAME)} " else ""
        formatter.decimalFormatSymbols = symbols
        val fractionDigits = if (alwaysShowDecimals) 2 else 0
        formatter.minimumIntegerDigits = 1
        formatter.minimumFractionDigits = fractionDigits
        formatter.maximumFractionDigits = fractionDigits
        return formatter.format(formattedToBigDecimal).apply { this.plus("") }
    }

    fun isoDateTimeToDate(isoDateTime: String): Date? {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val convertedDate: Date?
        var formattedDate: String? = null
        try {
            convertedDate = sdf.parse(isoDateTime)
            formattedDate =
                convertedDate?.let {
                    SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(
                        it
                    )
                }
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return formattedDate?.let {
            SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
                .parse(it)
        }
    }

    fun isTelephonyEnabled(context: Context): Boolean {
        return if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)
        ) {
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
            telephonyManager != null && telephonyManager.simState == TelephonyManager.SIM_STATE_READY
        } else {
            false
        }
    }

    fun formatStringToDateString(
        selectedDate: String?,
        currentFormat: String?,
        outputFormat: String?
    ): String {
        val dateFormat = SimpleDateFormat(currentFormat, Locale.getDefault())
        val output = SimpleDateFormat(outputFormat, Locale.getDefault())
        var parsedDate: Date? = null
        if (selectedDate == null) {
            return ""
        }
        try {
            parsedDate = dateFormat.parse(selectedDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return output.format(parsedDate)
    }

    private fun saveDeliveryDaysToPrefs(days: List<Int>) {
        val mapOfDeliveryDays = mapOf(
            0 to "MONDAY",
            1 to "TUESDAY",
            2 to "WEDNESDAY",
            3 to "THURSDAY",
            4 to "FRIDAY",
            5 to "SATURDAY"
        )
        // Get corresponding list of day strings based on delivery days list
        val deliveryDaysString = days.mapNotNull { mapOfDeliveryDays[it] }
        val gson = Gson()
        val json = gson.toJson(deliveryDaysString)
        Prefs.putString(Constant.PrefKeys.AGENT_DELIVERY_DAYS, json)
    }


    fun jsonStringToProduct(productJson: String): Product {
        val type = object : TypeToken<Product>() {}.type
        return Gson().fromJson(productJson, type)
    }

    fun formatDateStringToDate(
        selectedDate: String?,
        currentFormat: String?
    ): Date? {
        val dateFormat = SimpleDateFormat(currentFormat, Locale.getDefault())
        var parsedDate: Date? = null
        if (selectedDate == null) {
            return Date()
        }
        try {
            parsedDate = dateFormat.parse(selectedDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return parsedDate
    }

    fun getOutStockProducts(
        products: List<Product>,
        stockInfoList: List<StockInfo>
    ): List<Product> {
        return products.filter { product ->
            // First find corresponding product in the stock info list
            val stockInfo = stockInfoList.find { it.productId == product.id?.toInt() }
            // use the result as the filter predicate
            stockInfo?.let {
                // Here we want a product whose quantity is being tracked
                it.qoh <= 0 && (it.trackStock && it.rejectOrders)
            } == true
        }
    }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    fun getValueFromJson(key: String, jsonData: JSONObject): String {
        return try {
            jsonData.getString(key)
        } catch (ex: Exception) {
            ""
        }
    }

    fun shareContent(context: Context, subject: String, shareBody: String) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"))
    }

    fun formatListToString(list: List<String>): String {
        return when {
            list.isEmpty() -> ""
            list.size == 1 -> list.first()
            list.size == 2 -> list.joinToString(" and ")
            else -> list.dropLast(1).joinToString(", ") + ", and " + list.last()
        }
    }
}

const val REQUEST_CHECK_SETTINGS = 123

fun isLocationEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}

fun requestLocationEnabled(activity: Activity) {
    val locationRequest =
        LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
    val settingsClient = LocationServices.getSettingsClient(activity)

    val locationSettingsRequest = LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest)
        .setAlwaysShow(true)
        .build()

    settingsClient.checkLocationSettings(locationSettingsRequest)
        .addOnSuccessListener {
            // Location settings are satisfied. You can initiate location requests here.
        }
        .addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
}
