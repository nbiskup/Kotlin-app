package hr.algebra.nasa

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import hr.algebra.nasa.api.NasaFetcher
import hr.algebra.nasa.framework.sendBroadcast

private const val JOB_ID = 1
@Suppress("DEPRECATION")
class NasaService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        NasaFetcher(this).fetchItems(10)
    }
    companion object{
        fun enqueue(context: Context){
            enqueueWork(context, NasaService::class.java, JOB_ID,
                Intent(context, NasaService::class.java))
        }
    }

}