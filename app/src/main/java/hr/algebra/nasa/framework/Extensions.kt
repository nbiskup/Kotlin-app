package hr.algebra.nasa.framework

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.getSystemService
import androidx.preference.PreferenceManager
import hr.algebra.nasa.NASA_PROVIDER_CONTENT_URI
import hr.algebra.nasa.model.Item

fun View.applyAnimation(animationId: Int)
    = startAnimation(AnimationUtils.loadAnimation(context, animationId))

inline fun<reified T: Activity> Context.startActivity()
    = startActivity(
        Intent(this,T::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))

inline fun<reified T: Activity> Context.startActivity(key: String, value: Int)
        = startActivity(
    Intent(this,T::class.java).apply {
        putExtra(key,value)
    }.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))


inline fun<reified T: BroadcastReceiver> Context.sendBroadcast()
    = sendBroadcast(Intent(this, T::class.java))

fun Context.setBooleanPreference(key: String, value: Boolean = true){
    PreferenceManager.getDefaultSharedPreferences(this)
        .edit()
        .putBoolean(key, value)
        .apply()
}

fun Context.getBooleanPreference(key: String) =
    PreferenceManager.getDefaultSharedPreferences(this)
        .getBoolean(key, false)


fun Context.isOnline() : Boolean{
    val connectivityManager = getSystemService<ConnectivityManager>()
    connectivityManager?.activeNetwork?.let {network ->
        connectivityManager.getNetworkCapabilities(network)?.let {cap ->

            return cap.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || cap.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }
    }
    return false
}

fun callDelayed(delay: Long, runnable: Runnable) {
    Handler(Looper.getMainLooper()).postDelayed(
        runnable,
        delay
    )
}

@SuppressLint("Range")
fun Context.fetchItems() : MutableList<Item> {
    val items = mutableListOf<Item>()
    val cursor = contentResolver.query(NASA_PROVIDER_CONTENT_URI, null, null, null, null)

    while(cursor != null && cursor.moveToNext()){
        items.add(Item(
            cursor.getLong(cursor.getColumnIndex(Item::_id.name)),
            cursor.getString(cursor.getColumnIndex(Item::title.name)),
            cursor.getString(cursor.getColumnIndex(Item::explanation.name)),
            cursor.getString(cursor.getColumnIndex(Item::picturePath.name)),
            cursor.getString(cursor.getColumnIndex(Item::date.name)),
            cursor.getInt(cursor.getColumnIndex(Item::read.name)) == 1
        ))
    }

    return items
}