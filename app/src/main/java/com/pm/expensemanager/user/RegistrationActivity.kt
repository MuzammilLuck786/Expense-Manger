package com.pm.expensemanager.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.pm.expensemanager.*
import com.pm.expensemanager.databinding.ActivityRegistrationBinding
import com.pm.expensemanager.others.Validation
import com.pm.expensemanager.usermodel.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistrationActivity : AppCompatActivity() {

    lateinit var binding:ActivityRegistrationBinding
    lateinit var name1: String
    lateinit var phone: String
    lateinit var email: String
    lateinit var password: String
    lateinit var dbHelper: DBHelperUser
    lateinit var sharedPreferenceDatabase: SharedPreferenceDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelperUser.getDB(this@RegistrationActivity)
        sharedPreferenceDatabase= SharedPreferenceDatabase(this@RegistrationActivity)

        val email1= sharedPreferenceDatabase.getData("email","")
        if (email1 != "") {
            val intent = Intent(this, DashBoardActivity::class.java)
            startActivity(intent)
            finish()
        }

        val userDao = dbHelper.userDao()
        val userRepository = UserRepository(userDao)
        val userViewModelFactory = UserViewModelFactory(userRepository)
        val userViewModel =
            ViewModelProvider(this, userViewModelFactory)[UserViewModel::class.java]

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnRegistration.setOnClickListener {
            name1 = binding.edName.text.toString()
            phone = binding.edPhone.text.toString()
            email = binding.edEmail.text.toString()
            password = binding.edPassword.text.toString()

            if (Validation.nameValidation(name1, binding.edName) &&
                Validation.phoneValidation(phone, binding.edPhone) &&
                Validation.emailValidation(email, binding.edEmail) &&
                Validation.passwordValidation(password, binding.edPassword)
            ) {
                CoroutineScope(Dispatchers.IO).launch {
                    val user = dbHelper.userDao().getUserByEmail(email)
                    if (user != null) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@RegistrationActivity,
                                "Email Already Exists",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        CoroutineScope(Dispatchers.IO).launch {
                            val userEntity = UserEntity(
                                name = name1,
                                phone = phone,
                                email = email,
                                password = password
                            )
                            userViewModel.insertUser(userEntity)
                            withContext(Dispatchers.Main) {
                                val intent =
                                    Intent(this@RegistrationActivity, LoginActivity::class.java)
                                Toast.makeText(
                                    this@RegistrationActivity,
                                    "Registered Successfully",
                                    Toast.LENGTH_LONG
                                ).show()
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                }

            }
        }

    }
}