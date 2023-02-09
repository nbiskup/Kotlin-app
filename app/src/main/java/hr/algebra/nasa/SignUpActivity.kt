package hr.algebra.nasa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import hr.algebra.nasa.databinding.ActivitySignUpBinding
import hr.algebra.nasa.framework.startActivity
import hr.algebra.nasa.utils.ToastUtils

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        setButtonListeners()
    }

    private fun setButtonListeners() {
        binding.signUpButton.setOnClickListener{
            val email = binding.emailEdit.text.toString()
            val password = binding.passwordEdit.text.toString()
            val passwordConfirm = binding.passwordConfirmEdit.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty() && passwordConfirm.isNotEmpty()){
                if (password == passwordConfirm){
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
                        if (it.isSuccessful) startActivity<SignInActivity>()
                        else ToastUtils.show(this, it.exception.toString())
                    }
                } else{
                    ToastUtils.show(this, getString(R.string.password_not_match))
                }
            }
            else ToastUtils.show(this, R.string.insert_data_in_empty_fields)
        }
    }


}