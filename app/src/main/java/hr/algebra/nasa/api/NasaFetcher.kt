package hr.algebra.nasa.api

import android.content.ContentValues
import android.content.Context
import android.provider.Settings.Global
import android.util.Log
import hr.algebra.nasa.NASA_PROVIDER_CONTENT_URI
import hr.algebra.nasa.NasaReceiver
import hr.algebra.nasa.framework.sendBroadcast
import hr.algebra.nasa.handler.downloadImageAndStore
import hr.algebra.nasa.model.Item
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class NasaFetcher(private val context: Context) {

    private var nasaApi: NasaApi
    init {
        val retorfit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        nasaApi = retorfit.create(NasaApi::class.java)
    }

    fun fetchItems(count: Int){
        val request = nasaApi.fetchItems(count)
        request.enqueue(object: Callback<List<NasaItem>> {
            override fun onResponse(
                call: Call<List<NasaItem>>,
                response: Response<List<NasaItem>>
            ) {
                response?.body()?.let { populateItems(it) }
            }

            override fun onFailure(call: Call<List<NasaItem>>, t: Throwable) {
                Log.e(javaClass.name, t.toString(),t)
            }

        })

    }

    private fun populateItems(nasaItems: List<NasaItem>) {

        GlobalScope.launch {
            //val items = mutableListOf<Item>()
            nasaItems.forEach{
                var picturePath = downloadImageAndStore(context, it.url)

                val values = ContentValues().apply {
                    put(Item::title.name, it.title)
                    put(Item::explanation.name, it.explanation)
                    put(Item::picturePath.name, picturePath)
                    put(Item::date.name, it.date)
                    put(Item::read.name, false)
                }
                context.contentResolver.insert(NASA_PROVIDER_CONTENT_URI, values)
            }
            context.sendBroadcast<NasaReceiver>()
        }

    }

}