package edu.psu.sweng888.agecalculatorapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar
import kotlin.properties.Delegates

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
        val m_result_text_view = findViewById<TextView>(R.id.result_text)

        val today = Calendar.getInstance()
        m_dob_datepicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))
        {view, selected_year, selected_month, selected_day ->
            m_selected_year = selected_year
            m_selected_month = selected_month
            m_selected_day = selected_day
        }

        m_calc_age_button.setOnClickListener {
            val first_name = m_first_name_edit_text.getText().toString()
            val last_name = m_last_name_edit_text.getText().toString()

            val age = calculateAge()

            m_result_text_view.text = first_name + " " + last_name + " is " + age + " years old."
            m_result_text_view.visibility = View.VISIBLE

        }
    }

    fun calculateAge(): Int {
        val today = Calendar.getInstance()
        return today.get(Calendar.YEAR) - m_selected_year
    }
}