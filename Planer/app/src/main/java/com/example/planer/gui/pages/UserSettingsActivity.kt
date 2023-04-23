package com.example.planer.gui.pages

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.planer.R
import com.example.planer.ViewModel.SettingsViewModel
import com.example.planer.databinding.ActivityUserSettingsBinding
import com.example.planer.entities.Settings
import com.example.planer.entities.Types
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_user_settings.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import java.util.*


class UserSettingsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityUserSettingsBinding

    private val settingsViewModel: SettingsViewModel by viewModels()
    private var localSettings: Settings = Settings()
    private var localTypes: List<Types> = emptyList()

    var week_hours = 0
    var weekend_hours = 0
    var type_1 = ""
    var type_2 = ""
    var type_3 = ""
    var type_4 = ""

    var type_1_color = 1
    var type_2_color = 1
    var type_3_color = 1
    var type_4_color = 1

    //var chosenItems: MutableSet<String> = mutableSetOf()
    var chosenItems: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.type1Col1.setOnClickListener(this)
        binding.type1Col2.setOnClickListener(this)
        binding.type1Col3.setOnClickListener(this)
        binding.type1Col4.setOnClickListener(this)

        binding.type2Col1.setOnClickListener(this)
        binding.type2Col2.setOnClickListener(this)
        binding.type2Col3.setOnClickListener(this)
        binding.type2Col4.setOnClickListener(this)

        binding.type3Col1.setOnClickListener(this)
        binding.type3Col2.setOnClickListener(this)
        binding.type3Col3.setOnClickListener(this)
        binding.type3Col4.setOnClickListener(this)

        binding.type4Col1.setOnClickListener(this)
        binding.type4Col2.setOnClickListener(this)
        binding.type4Col3.setOnClickListener(this)
        binding.type4Col4.setOnClickListener(this)

        //binding.dateSpinner.setOnClickListener(this)
        binding.calendarIcon.setOnClickListener(this)

        binding.btnSave.setOnClickListener(this)

       // binding.weekHours.setOnClickListener(this)
        //binding.weekendHours.setOnClickListener(this)

        settingsViewModel.readSettingsFromDb().observe(this) { settings ->
            if (settings == null) {
                CoroutineScope(Dispatchers.IO).launch {
                    settingsViewModel.createSettingsIfDontExist(Settings())
                }
            } else {
                localSettings = settings
                binding.slider.value = localSettings.dailyAvailableHours.toFloat()
            }
        }

        binding.slider.value = localSettings.dailyAvailableHours.toFloat()

        chosenItems.add("kliknij aby usunąć")
        binding.dateSpinner.setSelection(0)

    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.btn_save -> {
                val buttns = findViewById<LinearLayout>(R.id.button_layout)
                val savedNotif = Snackbar.make(buttns, "Zapisane!", BaseTransientBottomBar.LENGTH_SHORT)
                savedNotif.anchorView = buttns
                localSettings.dailyAvailableHours = binding.slider.value.toInt()
                CoroutineScope(Dispatchers.IO).launch {
                    settingsViewModel.saveSettings(localSettings)
                    savedNotif.show()
                }
            }

            //typ 1
            R.id.type1_col1 -> {
                uncheckType(1, type_1_color)
                binding.type1Col1.setImageResource(R.drawable.radio_checked)
                //binding.type1Col1.setColorFilter(ContextCompat.getColor(this, R.color.brown_important_urgent_off), PorterDuff.Mode.SRC_ATOP)
                type_1_color = 1
            }

            R.id.type1_col2 -> {
                uncheckType(1, type_1_color)
                binding.type1Col2.setImageResource(R.drawable.radio_checked)
                type_1_color = 2

            }

            R.id.type1_col3 -> {
                uncheckType(1, type_1_color)
                binding.type1Col3.setImageResource(R.drawable.radio_checked)
                type_1_color = 3

            }

            R.id.type1_col4 -> {
                uncheckType(1, type_1_color)
                binding.type1Col4.setImageResource(R.drawable.radio_checked)
                type_1_color = 4

            }

            //typ 2
            R.id.type2_col1 -> {
                uncheckType(2, type_2_color)
                binding.type2Col1.setImageResource(R.drawable.radio_checked)
                type_2_color = 1

            }
            R.id.type2_col2 -> {
                uncheckType(2, type_2_color)
                binding.type2Col2.setImageResource(R.drawable.radio_checked)
                type_2_color = 2

            }

            R.id.type2_col3 -> {
                uncheckType(2, type_2_color)
                binding.type2Col3.setImageResource(R.drawable.radio_checked)
                type_2_color = 3

            }

            R.id.type2_col4 -> {
                uncheckType(2, type_2_color)
                binding.type2Col4.setImageResource(R.drawable.radio_checked)
                type_2_color = 4

            }

            //typ 3
            R.id.type3_col1 -> {
                uncheckType(3, type_3_color)
                binding.type3Col1.setImageResource(R.drawable.radio_checked)
                type_3_color = 1

            }
            R.id.type3_col2 -> {
                uncheckType(3, type_3_color)
                binding.type3Col2.setImageResource(R.drawable.radio_checked)
                type_3_color = 2

            }

            R.id.type3_col3 -> {
                uncheckType(3, type_3_color)
                binding.type3Col3.setImageResource(R.drawable.radio_checked)
                type_3_color = 3

            }

            R.id.type3_col4 -> {
                uncheckType(3, type_3_color)
                binding.type3Col4.setImageResource(R.drawable.radio_checked)
                type_3_color = 4

            }

            //typ 4
            R.id.type4_col1 -> {
                uncheckType(4, type_4_color)
                binding.type4Col1.setImageResource(R.drawable.radio_checked)
                type_4_color = 1

            }
            R.id.type4_col2 -> {
                uncheckType(4, type_4_color)
                binding.type4Col2.setImageResource(R.drawable.radio_checked)
                type_4_color = 2

            }

            R.id.type4_col3 -> {
                uncheckType(4, type_4_color)
                binding.type4Col3.setImageResource(R.drawable.radio_checked)
                type_4_color = 3

            }

            R.id.type4_col4 -> {
                uncheckType(4, type_4_color)
                binding.type4Col4.setImageResource(R.drawable.radio_checked)
                type_4_color = 4

            }

            R.id.calendar_icon -> {

                val calendar = Calendar.getInstance()
                val today_year = calendar.get(Calendar.YEAR)
                val today_month = calendar.get(Calendar.MONTH)
                val today_day = calendar.get(Calendar.DAY_OF_MONTH)

                var date = ""

                val dpd = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, sel_year, sel_month, sel_day ->
                        date = setUpDate(sel_day, sel_month, sel_year)

                        chosenItems.add(date)

                        val chosenItemsTemp = chosenItems.distinct()

                        if (chosenItems.size != chosenItemsTemp.size) {
                            // List contains duplicates
                            chosenItems.removeLast()

                        }

                    }, today_year, today_month, today_day
                )

                dpd.show()

                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, chosenItems)
                binding.dateSpinner.adapter = adapter
                binding.dateSpinner.setSelection(0)
                binding.dateSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                        // remove the selected item from the spinner's adapter
                        if(!adapter.getItem(position).equals("kliknij aby usunąć"))
                        {
                            adapter.remove(adapter.getItem(position))
                            binding.dateSpinner.setSelection(0)
                            // notify the adapter that the data set has changed
                            adapter.notifyDataSetChanged()
                        }

                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        binding.dateSpinner.setSelection(0)
                    }
                }
            }

        }


        binding.slider.setLabelFormatter { value -> //It is just an example
            if (value == 3.0f) "TEST" else java.lang.String.format(Locale.US, "%.0f", value)
        }
    }




    fun setUpDate(d: Int, m: Int, y: Int): String {
        var month = m.toString()
        var day = d.toString()

        month.takeIf { m + 1 < 10 }?.let { month = "0" + (m + 1) }
        day.takeIf { d < 10 }?.let { day = "0" + d }

        return "$y-$month-$day"
    }


    private fun uncheckType(type: Int, color: Int) {
        when (type) {
            1 -> {
                when (color) {
                    1 -> {
                        binding.type1Col1.setImageResource(R.drawable.radio_unchecked)

                    }
                    2 -> {
                        binding.type1Col2.setImageResource(R.drawable.radio_unchecked)

                    }
                    3 -> {
                        binding.type1Col3.setImageResource(R.drawable.radio_unchecked)

                    }
                    4 -> {
                        binding.type1Col4.setImageResource(R.drawable.radio_unchecked)

                    }
                }
            }

            2 -> {
                when (color) {
                    1 -> {
                        binding.type2Col1.setImageResource(R.drawable.radio_unchecked)

                    }
                    2 -> {
                        binding.type2Col2.setImageResource(R.drawable.radio_unchecked)

                    }
                    3 -> {
                        binding.type2Col3.setImageResource(R.drawable.radio_unchecked)

                    }
                    4 -> {
                        binding.type2Col4.setImageResource(R.drawable.radio_unchecked)

                    }
                }

            }

            3 -> {
                when (color) {
                    1 -> {
                        binding.type3Col1.setImageResource(R.drawable.radio_unchecked)

                    }
                    2 -> {
                        binding.type3Col2.setImageResource(R.drawable.radio_unchecked)

                    }
                    3 -> {
                        binding.type3Col3.setImageResource(R.drawable.radio_unchecked)

                    }
                    4 -> {
                        binding.type3Col4.setImageResource(R.drawable.radio_unchecked)

                    }
                }

            }

            4 -> {
                when (color) {
                    1 -> {
                        binding.type4Col1.setImageResource(R.drawable.radio_unchecked)

                    }
                    2 -> {
                        binding.type4Col2.setImageResource(R.drawable.radio_unchecked)

                    }
                    3 -> {
                        binding.type4Col3.setImageResource(R.drawable.radio_unchecked)

                    }
                    4 -> {
                        binding.type4Col4.setImageResource(R.drawable.radio_unchecked)

                    }
                }

            }
        }
    }
}