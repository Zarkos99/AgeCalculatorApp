package edu.psu.sweng888.agecalculatorapp

import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.properties.Delegates
import android.util.Log
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Objects


class MainActivity : AppCompatActivity() {

    private var m_selected_year by Delegates.notNull<Int>()
    private var m_selected_month by Delegates.notNull<Int>()
    private var m_selected_day by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val m_first_name_edit_text = findViewById<EditText>(R.id.first_name_field)
        val m_last_name_edit_text = findViewById<EditText>(R.id.last_name_field)
        val m_dob_datepicker = findViewById<DatePicker>(R.id.dob_datepicker)
        val m_calc_age_button = findViewById<Button>(R.id.calculate_button)

        // Initialize dates
        val now = LocalDateTime.now()
        m_selected_year = now.year
        m_selected_month = now.monthValue
        m_selected_day = now.dayOfMonth

        m_dob_datepicker.init(
            now.year,
            now.monthValue - 1, // datepicker indexes months as 0 to 11 while LocalDateTime indexes from 1 to 12
            now.dayOfMonth
        )
        { _, selected_year, selected_month, selected_day ->
            m_selected_year = selected_year
            m_selected_month = selected_month + 1
            m_selected_day = selected_day

        }

        m_calc_age_button.setOnClickListener {
            val first_name = m_first_name_edit_text.getText().toString()
            val last_name = m_last_name_edit_text.getText().toString()

            // Error handling for empty first name
            if (first_name.isEmpty()) {
                //Display error message
                Toast.makeText(
                    this@MainActivity,
                    "Please input a first name",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            // Error handling for empty last name
            if (last_name.isEmpty()) {
                //Display error message
                Toast.makeText(
                    this@MainActivity,
                    "Please input a last name",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            // Error handling for future date
            val is_future_year = m_selected_year > now.year
            val is_future_month =
                m_selected_year == now.year && (m_selected_month > now.monthValue)
            val is_future_day =
                m_selected_year == now.year && m_selected_month == now.monthValue && (m_selected_day > now.dayOfMonth)

            if (is_future_year || is_future_month || is_future_day) {
                //Display error message
                Toast.makeText(
                    this@MainActivity,
                    "Cannot choose a date of birth in the future",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            val result_text = calculateAge(first_name, last_name)
            //Display calculated age as a Toast
            Toast.makeText(this@MainActivity, result_text, Toast.LENGTH_LONG).show()
        }
    }

    private fun calculateAge(first_name: String, last_name: String): String {
        val now = LocalDateTime.now()
        val today_year = now.year
        val today_month = now.monthValue
        val today_day = now.dayOfMonth

        // We already ensured that the selected date is not in the future
        // See if the DOB is within the current year
        if (today_year == m_selected_year) {
            // DOB is days old if within the same month
            if (today_month == m_selected_month) {
                val age = today_day - m_selected_day
                return first_name + " " + last_name + " is " + age + " days old."
            }// DOB is months old if not within the same month
            else {
                val age = today_month - m_selected_month
                return first_name + " " + last_name + " is " + age + " months old."
            }
        }// Determine if the DOB is less than a year old
        else if ((today_year - m_selected_year) == 1) {
            if (today_month + (12 - m_selected_month) < 12) {
                val age = today_month + (12 - m_selected_month)
                return first_name + " " + last_name + " is " + age + " months old."
            }
            if (today_month + (12 - m_selected_month) == 12 && today_day < m_selected_day) {
                return first_name + " " + last_name + " is 11 months old. "
            }
        }

        var age = today_year - m_selected_year
        // If their birth day has not been reached yet then they are one year younger than the difference of the birth year and current year
        if (m_selected_month > today_month || (m_selected_month == today_month && m_selected_day > today_day)) {
            age -= 1
        }
        return first_name + " " + last_name + " is " + age + " years old."
    }
}