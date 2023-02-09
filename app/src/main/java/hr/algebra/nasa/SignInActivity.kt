package hr.algebra.nasa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import hr.algebra.nasa.databinding.ActivitySignInBinding
import hr.algebra.nasa.framework.startActivity
import hr.algebra.nasa.utils.ToastUtils

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        setTextViewListeners()
        setButtonListeners()
    }

    private fun setButtonListeners() {
        binding.singInButton.setOnClickListener{
            val email = binding.emailEdit.text.toString()
            val password = binding.passwordEdit.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                    if (it.isSuccessful) startActivity<SplashScreenActivity>()
                    else ToastUtils.show(this, it.exception.toString())
                }
            }
            else ToastUtils.show(this, getString(R.string.insert_data_in_empty_fields))
        }
    }

    private fun setTextViewListeners() {
        binding.tvSignUp.setOnClickListener{
            startActivity<SignUpActivity>()
        }
    }
}