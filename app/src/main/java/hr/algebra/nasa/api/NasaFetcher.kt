package hr.algebra.nasa.api

import android.content.Context
import android.provider.Settings.Global
import android.util.Log
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
                Log.e("API", t.toString(),t)
            }

        })

    }

    private fun populateItems(nasaItems: List<NasaItem>) {

        GlobalScope.launch {
            val items = mutableListOf<Item>()
            nasaItems.forEach{
                val picturePath = downloadImageAndStore(context, it.url)
                items.add(
                    Item(
                        null,
                        it.title,
                        it.explanation,
                        picturePath ?: "",
                        it.date,
                        false
                    )
                )
            }
            context.sendBroadcast<NasaReceiver>()
        }

    }

}