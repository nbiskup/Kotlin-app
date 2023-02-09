package hr.algebra.nasa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.airbnb.lottie.LottieAnimationView
import hr.algebra.nasa.databinding.ActivitySplashScreenBinding
import hr.algebra.nasa.framework.*

private const val DELAY = 3000L
const val DATA_IMPORTED = "hr.algebra.nasa.data_imported"
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startAnimations()
        redirect()
    }

    private fun redirect() {
        if (getBooleanPreference(DATA_IMPORTED)){
            callDelayed(DELAY) { startActivity<HostActivity>() }

        } else {
            if (isOnline()){
                NasaService.enqueue(this)

            }else{
                binding.tvSplash.text = getString(R.string.no_internet)
                callDelayed(DELAY) { finish() }
            }
        }
    }

    private fun startAnimations() {
        binding.tvSplash.applyAnimation(R.anim.blink)
        binding.ivSplash.applyAnimation(R.anim.rotate)
    }
}