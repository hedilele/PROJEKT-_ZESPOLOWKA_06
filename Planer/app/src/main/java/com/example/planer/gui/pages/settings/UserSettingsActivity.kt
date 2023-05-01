package com.example.planer.gui.pages.settings

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.ZoneId

import java.util.*
import kotlin.properties.Delegates


class UserSettingsActivity : AppCompatActivity(), View.OnClickListener,
    TypeAdapter.OnButtonClickListener {

    private lateinit var binding: ActivityUserSettingsBinding
    private var unsavedSettings by Delegates.notNull<Boolean>()

    private lateinit var typeAdapter: TypeAdapter
    private val settingsViewModel: SettingsViewModel by viewModels()

    private var localSettings: Settings = Settings()
    private var changedTypes: MutableList<Types> = mutableListOf()
    private lateinit var dbDates: List<PrimeCalendar>
    private var markedDatePickerList: MutableList<PrimeCalendar> = mutableListOf()

    private val calendar = CivilCalendar(TimeZone.getDefault(), Locale("pl", "PL"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        unsavedSettings = false

        typeAdapter = TypeAdapter(emptyList())
        typeAdapter.setOnItemClickListener(this)

        binding.typesRecyclerView.adapter = typeAdapter
        binding.typesRecyclerView.layoutManager = LinearLayoutManager(this)

        binding.pickExcludedDatesButton.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)

        binding.resetUnavailableDatesButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Czy chcesz usunąć wszystkie wybrane dni?")
            builder.setPositiveButton("Tak") { _, _ ->
                markedDatePickerList.clear()
                unsavedSettings = true
            }
            builder.setNegativeButton("Nie") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }

        val yesterday = CivilCalendar(TimeZone.getDefault(), Locale("pl", "PL"))
        yesterday.add(Calendar.DAY_OF_YEAR, -1)

        settingsViewModel.readSettingsFromDb().observe(this) { settings ->
            localSettings = settings
            binding.slider.value = localSettings.dailyAvailableHours.toFloat()
        }

        settingsViewModel.readExcludedDatesFromDb().observe(this) { exdates ->
            dbDates = exdates.map {
                val a = CivilCalendar()
                a.setTime(
                    Date.from(
                        it.excludedDate
                            .atStartOfDay()
                            .atZone(ZoneId.systemDefault())
                            .toInstant()
                    )
                )
                a
            }
            // Automatycznie usuwa stare daty również potem w bazie
            markedDatePickerList = dbDates.filter { it.after(yesterday) }.toMutableList()
        }

        settingsViewModel.readTypesFromDb().observe(this) { types ->
            typeAdapter.updateList(types)
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.btn_save -> {
                val buttns = findViewById<LinearLayout>(R.id.button_layout)
                val savedNotif =
                    Snackbar.make(buttns, "Zapisane!", BaseTransientBottomBar.LENGTH_SHORT)
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

                    settingsViewModel.saveSettings(localSettings, changedTypes, selectedDates)

                    savedNotif.show()
                }
            }

            R.id.pick_excluded_dates_button -> {

                val callback = MultipleDaysPickCallback { days ->
                    if (markedDatePickerList.sorted() != days.sorted()) {
                        markedDatePickerList = days
                        unsavedSettings = true
                    }
                }

                val datePicker = PrimeDatePicker.dialogWith(calendar)
                    .pickMultipleDays(callback)
                    .initiallyPickedMultipleDays(markedDatePickerList)
                    .minPossibleDate(calendar)
                    .build()

                datePicker.show(supportFragmentManager, "Tak")

            }

        }

        binding.slider.setLabelFormatter { value -> //It is just an example
            if (value == 3.0f) "TEST" else java.lang.String.format(Locale.US, "%.0f", value)
        }
    }

    override suspend fun onChosenColorButtonClick(type: Types, holder: TypeAdapter.ViewHolder) {
        MaterialColorPickerDialog
            .Builder(this)
            .setColors(
                arrayListOf(
                    "#f6e58d",
                    "#ffbe76",
                    "#ff7979",
                    "#badc58",
                    "#dff9fb",
                    "#7ed6df",
                    "#e056fd",
                    "#686de0",
                    "#30336b",
                    "#95afc0"
                )
            )
            .setColorListener { color, colorhex ->
                holder.chosenColorButton.backgroundTintList = ColorStateList.valueOf(color)
                // Jeśli jest element z takim id to zamienia kolor, jeśli nie ma to dodaje typ ze zmienionym kolorem
                changedTypes.firstOrNull { it.id == type.id }?.apply { this.colour = colorhex }
                    ?: changedTypes.add(Types(type.id, type.name, colorhex))
                unsavedSettings = true
            }
            .setColorShape(ColorShape.SQAURE)
            .show()
    }
}