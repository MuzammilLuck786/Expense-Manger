package com.pm.expensemanager.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.pm.expensemanager.*
import com.pm.expensemanager.databinding.ActivityLoginBinding
import com.pm.expensemanager.usermodel.DBHelperUser
import com.pm.expensemanager.usermodel.UserRepository
import com.pm.expensemanager.usermodel.UserViewModel
import com.pm.expensemanager.usermodel.UserViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var dbHelper: DBHelperUser
    lateinit var email:String
    lateinit var password:String
    lateinit var sharedPreferenceDatabase: SharedPreferenceDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        dbHelper= DBHelperUser.getDB(this@LoginActivity)
        sharedPreferenceDatabase= SharedPreferenceDatabase(this@LoginActivity)
        val userDao = dbHelper.userDao()
        val userRepository = UserRepository(userDao)
        val userViewModelFactory = UserViewModelFactory(userRepository)
        val userViewModel =
            ViewModelProvider(this, userViewModelFactory)[UserViewModel::class.java]

        binding.tvSignUpRegistration.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnLogin.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                email = binding.edEmailLogin.text.toString()
                password = binding.edPasswordLogin.text.toString()

                val userEmail = userViewModel.getUserByEmail(email)
                val userPassword = userViewModel.getUserByPassword(password)
                if (userEmail != null && userPassword !=null) {
                    withContext(Dispatchers.Main) {
                        val intent = Intent(this@LoginActivity, DashBoardActivity::class.java)
                        sharedPreferenceDatabase.saveData("email",email)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@LoginActivity, "Invalid Email or Password", Toast.LENGTH_LONG).show()
                    }
                }
            }


        }


    }
}