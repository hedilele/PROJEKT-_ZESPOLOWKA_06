package com.example.planer.gui.settings

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.MultipleDaysPickCallback
import com.example.planer.R
import com.example.planer.ViewModel.SettingsViewModel
import com.example.planer.databinding.ActivityUserSettingsBinding
import com.example.planer.entities.ExcludedDate
import com.example.planer.entities.Settings
import com.example.planer.entities.Types
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_user_settings.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

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

    private lateinit var dbDates: List<PrimeCalendar>
    private var markedDatePickerList: MutableList<PrimeCalendar> = mutableListOf()

    private val calendar = CivilCalendar(TimeZone.getDefault(), Locale("pl", "PL"))

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

        binding.pickExcludedDatesButton.setOnClickListener(this)

        binding.btnSave.setOnClickListener(this)

        val yesterday = CivilCalendar(TimeZone.getDefault(), Locale("pl", "PL"))
        yesterday.add(Calendar.DAY_OF_YEAR,-1)

        settingsViewModel.readSettingsFromDb().observe(this) { settings ->
            if (settings == null) {
                CoroutineScope(Dispatchers.IO).launch {
                    settingsViewModel.createSettingsIfDontExist() // TODO TO POWINNO BYĆ W MAIN
                }
            } else {
                localSettings = settings
                binding.slider.value = localSettings.dailyAvailableHours.toFloat()
            }
        }

        settingsViewModel.readExcludedDatesFromDb().observe(this) { exdates ->
            dbDates = exdates.map {
                val a = CivilCalendar()
                a.setTime(Date.from(
                    it.excludedDate
                        .atStartOfDay()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                ))
                a
            }
            // Automatycznie usuwa stare daty również potem w bazie
            markedDatePickerList = dbDates.filter { it.after(yesterday) }.toMutableList()
        }

        settingsViewModel.readTypesFromDb().observe(this) { types ->
            if (types == null) {
                CoroutineScope(Dispatchers.IO).launch {
                    settingsViewModel.createTypesIfDontExist() //TODO DO MAIN
                }
            } else {
                //TODO adapter typów zadań
            }
        }

        binding.slider.value = localSettings.dailyAvailableHours.toFloat()


    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.btn_save -> {
                val buttns = findViewById<LinearLayout>(R.id.button_layout)
                val savedNotif = Snackbar.make(buttns, "Zapisane!", BaseTransientBottomBar.LENGTH_SHORT)
                savedNotif.anchorView = buttns

                // Pobieram wartość dostępnych godzin ze slidera
                localSettings.dailyAvailableHours = binding.slider.value.toInt()

                // Zapisywanie ustawień do bazy
                CoroutineScope(Dispatchers.IO).launch {

                    // Zamieniam ten customowy calendar na ExcludedDate z LocalTime
                    val selectedDates: List<ExcludedDate> = markedDatePickerList.map {
                        val localDate = it.getTime()
                            .toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        ExcludedDate(excludedDate = localDate)
                    }

                    settingsViewModel.saveSettings(localSettings, localTypes, selectedDates)

                    savedNotif.show()
                }
            }

            R.id.pick_excluded_dates_button -> {

                val callback = MultipleDaysPickCallback { days ->
                    markedDatePickerList = days
                }

                val datePicker = PrimeDatePicker.dialogWith(calendar)
                    .pickMultipleDays(callback)
                    .initiallyPickedMultipleDays(markedDatePickerList)
                    .minPossibleDate(calendar)
                    .build()

                datePicker.show(supportFragmentManager, "Tak")

            }

            // TODO Adapter tego wszystkiego

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

        }


        binding.slider.setLabelFormatter { value -> //It is just an example
            if (value == 3.0f) "TEST" else java.lang.String.format(Locale.US, "%.0f", value)
        }
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