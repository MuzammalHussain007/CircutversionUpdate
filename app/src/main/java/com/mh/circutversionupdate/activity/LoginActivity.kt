package com.mh.circutversionupdate.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.mh.circutversionupdate.AddItemActivity
import com.mh.circutversionupdate.R
import com.mh.circutversionupdate.utils.AppStorage
import com.mh.circutversionupdate.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var progressDialog : ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullscreen()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        innit()
        clickListener()

    }

    private fun innit() {
        AppStorage.init(this)
        progressDialog = ProgressDialog(this, R.style.AppCompatAlertDialogStyle)
        progressDialog.setMessage("Please Wait")
    }

    private fun clickListener() {
        binding.btnLogin.setOnClickListener {
            authentication()
        }

        binding.signupTextView.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }



    }

    private fun authentication() {

        if (binding.etEmailLogin.text!!.isEmpty()) {
            binding.etEmailLogin.error = getString(R.string.emailisnotempty)
            binding.etEmailLogin.requestFocus()
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etEmailLogin.text).matches()) {
            binding.etEmailLogin.error = getString(R.string.emailaddressnotvalid)
            binding.etEmailLogin.requestFocus()
            return
        }

        if (binding.etPasswordLogin.text!!.isEmpty()) {
            binding.etPasswordLogin.error = getString(R.string.passwordisnotempty)
            binding.etPasswordLogin.requestFocus()
            return
        }
        val email = binding.etEmailLogin.text.toString()
        val password = binding.etPasswordLogin.text.toString()
        if(email=="shop@shop.com" && password=="1234567890")
        {
            startActivity(Intent(this, AddItemActivity::class.java))
            finish()
        }else
            login(email,password)

    }

    private fun login(email: String, password: String) {
         progressDialog.show()
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    progressDialog.dismiss()
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("abce______", "signInWithEmail:success")
                    val user = FirebaseAuth.getInstance().currentUser
                    AppStorage.setUser(true)
                    Log.d("abce______", "signInWithEmail:success"+user)
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {

                    // If sign in fails, display a message to the user.
                    Log.w("abce______", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun fullscreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

    }

}