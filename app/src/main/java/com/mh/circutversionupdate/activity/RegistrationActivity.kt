package com.mh.circutversionupdate.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.mh.circutversionupdate.R
import com.mh.circutversionupdate.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var progressDialog : ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullscreen()
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        innit()
        clickListener()
    }

    private fun clickListener() {
        binding.btnSignup.setOnClickListener {
            validateAndSignUp()
        }
        binding.signupTextView.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }

    }

    private fun innit() {
        progressDialog = ProgressDialog(this, R.style.AppCompatAlertDialogStyle)
        progressDialog.setMessage("Please Wait")
    }

    private fun fullscreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }


    private fun validateAndSignUp() {

        if (binding.etFullnameLogin.text!!.isEmpty()) {
            binding.etFullnameLogin.error = getString(R.string.TitleFieldmustnotempty)
            binding.etFullnameLogin.requestFocus()
            return
        }

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

        if (binding.etConfirmpasswordLogin.text!!.isEmpty()) {
            binding.etConfirmpasswordLogin.error = getString(R.string.cnfrmpasswordpasswordisnotempty)
            binding.etConfirmpasswordLogin.requestFocus()
            return
        }

        if (binding.etPasswordLogin.text.toString() != binding.etConfirmpasswordLogin.text.toString()) {
            binding.etPasswordLogin.error = getString(R.string.passwordnotmatch)
            binding.etConfirmpasswordLogin.error = getString(R.string.passwordnotmatch)
            binding.etPasswordLogin.requestFocus()
            return
        }

        val name = binding.etFullnameLogin.text.toString()
        val email = binding.etEmailLogin.text.toString()
        val password = binding.etPasswordLogin.text.toString()

        startAuthentication(name, email, password)


    }

    private fun startAuthentication(fullname: String, email: String, password: String) {
       progressDialog.show()
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    progressDialog.dismiss()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, "Some Thing went Wrong", Toast.LENGTH_SHORT)
                        .show()
                }
            }

    }
}