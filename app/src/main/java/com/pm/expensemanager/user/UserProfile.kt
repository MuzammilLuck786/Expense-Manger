package com.pm.expensemanager.user

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.pm.expensemanager.*
import com.pm.expensemanager.databinding.ActivityUserProfileBinding
import com.pm.expensemanager.usermodel.DBHelperUser
import com.pm.expensemanager.usermodel.UserRepository
import com.pm.expensemanager.usermodel.UserViewModel
import com.pm.expensemanager.usermodel.UserViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class UserProfile : AppCompatActivity() {
    lateinit var binding: ActivityUserProfileBinding
    lateinit var dbHelper: DBHelperUser
    lateinit var sharedPreferenceDatabase: SharedPreferenceDatabase
    lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        binding=ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper= DBHelperUser.getDB(this@UserProfile)
        sharedPreferenceDatabase= SharedPreferenceDatabase(this@UserProfile)

        val userDao = dbHelper.userDao()
        val userRepository = UserRepository(userDao)
        val userViewModelFactory = UserViewModelFactory(userRepository)
        userViewModel =
            ViewModelProvider(this, userViewModelFactory)[UserViewModel::class.java]

        binding.btnLogout.setOnClickListener {

            sharedPreferenceDatabase.clearData()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()

        }

        binding.showDatePickerButton.setOnClickListener {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            fun calculateAge(selectedDate: Calendar) {
                val currentDate = Calendar.getInstance()

                val diffInMillis = currentDate.timeInMillis - selectedDate.timeInMillis
                val ageYears = diffInMillis / (1000L * 60 * 60 * 24 * 365)
                val ageMonths = diffInMillis / (1000L * 60 * 60 * 24 * 30)
                val ageDays = diffInMillis / (1000L * 60 * 60 * 24)

                binding.tvShowAge.text = "Age: $ageYears years, $ageMonths months, $ageDays days"
                binding.tvShowAge.visibility = TextView.VISIBLE
            }


            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(selectedYear, selectedMonth, selectedDay)
                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    binding.tvSetAge.text = sdf.format(selectedDate.time)
                    calculateAge(selectedDate)

                },
                year, month, day
            )
            datePickerDialog.show()
        }


        binding.buttonCalculate.setOnClickListener {
            val weight = binding.editTextWeight.text.toString().toFloatOrNull()
            val feet = binding.editTextFeet.text.toString().toIntOrNull()
            val inches = binding.editTextInches.text.toString().toIntOrNull()

            if (weight != null && feet != null && inches != null && feet > 0 && inches >= 0 && inches < 12) {
                val bmi = calculateBMI(weight, feet, inches)
                val resultText = "Your BMI is $bmi"
                binding.textViewResult.text = resultText
            } else {
                binding.textViewResult.text = "Invalid input"
            }
        }
    }
    private fun calculateBMI(weight: Float, feet: Int, inches: Int): Double {
        val heightInMeters = ((feet * 12) + inches) * 0.0254 // Convert feet and inches to meters
        return weight / (heightInMeters * heightInMeters)
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.IO).launch {
            val email= sharedPreferenceDatabase.getData("email","")
            val user= userViewModel.getUserByEmail(email)
            if (user!=null){
                binding.tvName.text = "Name: ${user.name}"
                binding.tvNumber.text = "Phone: ${user.phone}"
                binding.tvEmail.text = "Email: ${user.email}"
            }
        }


    }
}