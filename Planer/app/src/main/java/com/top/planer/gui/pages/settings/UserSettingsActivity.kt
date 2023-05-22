package com.top.planer.gui.pages.settings

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.SparseIntArray
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primedatepicker.common.BackgroundShapeType
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.MultipleDaysPickCallback
import com.aminography.primedatepicker.picker.theme.LightThemeFactory
import com.top.planer.R
import com.top.planer.ViewModel.SettingsViewModel
import com.top.planer.databinding.ActivityUserSettingsBinding
import com.top.planer.entities.ExcludedDate
import com.top.planer.entities.Settings
import com.top.planer.entities.Types
import com.top.planer.gui.pages.scope.UnscrollableLinearLayoutManager
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import de.raphaelebner.roomdatabasebackup.core.RoomBackup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.util.*

class UserSettingsActivity : AppCompatActivity(), View.OnClickListener,
    TypeAdapter.OnButtonClickListener {

    private lateinit var binding: ActivityUserSettingsBinding
    private var unsavedSettings: MutableLiveData<Boolean> = MutableLiveData(false)

    private lateinit var typeAdapter: TypeAdapter
    private val settingsViewModel: SettingsViewModel by viewModels()
    private var localSettings: Settings = Settings()
    private var changedTypes: MutableList<Types> = mutableListOf()
    private lateinit var dbDates: List<PrimeCalendar>
    private var markedDatePickerList: MutableList<PrimeCalendar> = mutableListOf()
    private val backup = RoomBackup(this)

    private val calendar = CivilCalendar(TimeZone.getDefault(), Locale("pl", "PL"))

    override fun onCreate(savedInstanceState: Bundle?) {

        // Set full screen
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        super.onCreate(savedInstanceState)

        binding = ActivityUserSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        typeAdapter = TypeAdapter(emptyList())
        typeAdapter.setOnItemClickListener(this)

        binding.typesRecyclerView.adapter = typeAdapter
        binding.typesRecyclerView.layoutManager = UnscrollableLinearLayoutManager(this)

        binding.pickExcludedDatesButton.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
        binding.btnExit.setOnClickListener(this)

        binding.slider.addOnChangeListener { slider: Slider, _: Float, _: Boolean ->
            if (slider.value != localSettings.dailyAvailableHours.toFloat()) {
                unsavedSettings.setValue(true)
            }
        }

        binding.slider.setLabelFormatter(LabelFormatter { binding.slider.value.toInt().toString() + " godz" })

        binding.resetUnavailableDatesButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Czy chcesz usunąć wszystkie wybrane dni?")
            builder.setPositiveButton("Tak") { _, _ ->
                markedDatePickerList.clear()
                unsavedSettings.setValue(true)
            }
            builder.setNegativeButton("Nie") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }

        val yesterday = CivilCalendar(TimeZone.getDefault(), Locale("pl", "PL"))
        yesterday.add(Calendar.DAY_OF_YEAR, -1)

        unsavedSettings.observe(this) {
            if (it) {
                binding.unsavedChangesText.visibility = VISIBLE
            } else {
                binding.unsavedChangesText.visibility = GONE
            }
        }

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

    fun onExportButtonClicked(view: View) {
        val buttns = findViewById<LinearLayout>(R.id.button_layout)
        lifecycleScope.launch {
            if (settingsViewModel.exportDb(backup, this@UserSettingsActivity)) {
                val exportNotif =
                    Snackbar.make(buttns, "Eksport udany!", Snackbar.LENGTH_SHORT)
                exportNotif.anchorView = buttns
                exportNotif.show()
            } else {
                val exportNotif =
                    Snackbar.make(buttns, "Niepowodzenie!", Snackbar.LENGTH_SHORT)
                exportNotif.anchorView = buttns
                exportNotif.show()
            }
        }
    }

    fun onImportButtonClicked(view: View) {
        lifecycleScope.launch {
            val buttns = findViewById<LinearLayout>(R.id.button_layout)
            if (settingsViewModel.importDb(backup, this@UserSettingsActivity)) {
                // w rzeczywisotści restartuję apkę
                val importNotif =
                    Snackbar.make(buttns, "Zaimportowano! Restartowanie bazy danych...", Snackbar.LENGTH_SHORT)
                importNotif.anchorView = buttns
                importNotif.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        super.onDismissed(transientBottomBar, event)
                        settingsViewModel.restartApp(backup, this@UserSettingsActivity) // restart po zniknięciu snackbara
                    }
                })
                importNotif.show()
            } else {
                val importNotif =
                    Snackbar.make(buttns, "Niepowodzenie!", Snackbar.LENGTH_SHORT)
                importNotif.anchorView = buttns
                importNotif.show()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.btn_save -> {
                currentFocus?.clearFocus()
                val buttns = findViewById<LinearLayout>(R.id.button_layout)
                val savedNotif =
                    Snackbar.make(buttns, "Zapisane!", Snackbar.LENGTH_SHORT)
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

                    if (settingsViewModel.saveSettings(
                            localSettings,
                            changedTypes,
                            selectedDates
                        )
                    ) {
                        savedNotif.show()
                        unsavedSettings.postValue(false)
                    }
                }
                unsavedSettings.setValue(false)
            }

            R.id.btn_exit -> {
                finish()
            }

            R.id.pick_excluded_dates_button -> {

                val themeFactory = object : LightThemeFactory() {

                    override val pickedDayBackgroundShapeType: BackgroundShapeType
                        get() = BackgroundShapeType.ROUND_SQUARE

                    override val calendarViewPickedDayBackgroundColor: Int
                        get() = getColor(R.color.brown_important_urgent_on)

                    override val selectionBarMultipleDaysItemBackgroundColor: Int
                        get() = getColor(R.color.brown_important_urgent_on)


                    override val calendarViewWeekLabelTextColors: SparseIntArray
                        get() = SparseIntArray(7).apply {
                            val black = getColor(R.color.black)
                            put(Calendar.SATURDAY, black)
                            put(Calendar.SUNDAY, black)
                            put(Calendar.MONDAY, black)
                            put(Calendar.TUESDAY, black)
                            put(Calendar.WEDNESDAY, black)
                            put(Calendar.THURSDAY, black)
                            put(Calendar.FRIDAY, black)
                        }

                    override val calendarViewShowAdjacentMonthDays: Boolean
                        get() = true

                    override val selectionBarBackgroundColor: Int
                        get() = getColor(R.color.brown_important_urgent_off)


                }

                val callback = MultipleDaysPickCallback { days ->
                    if (markedDatePickerList.sorted() != days.sorted()) {
                        markedDatePickerList = days
                        unsavedSettings.setValue(true)
                    }
                }

                val datePicker = PrimeDatePicker.dialogWith(calendar)
                    .pickMultipleDays(callback)
                    .initiallyPickedMultipleDays(markedDatePickerList)
                    .minPossibleDate(calendar)
                    .firstDayOfWeek(Calendar.MONDAY)
                    .applyTheme(themeFactory)
                    .build()

                datePicker.show(supportFragmentManager, "Tak")

            }
        }
    }

    override suspend fun onChosenColorButtonClick(type: Types, holder: TypeAdapter.ViewHolder) {
        MaterialColorPickerDialog
            .Builder(this)
            .setColors(
                arrayListOf(
                    "#005F73",
                    "#099396",
                    "#93D2BD",
                    "#9FC088",
                    "#A13333",
                    "#B04759",
                    "#7A3E65",
                    "#BA94D1",
                    "#F16AA6",
                    "#F97B22",
                    "#ED9B00",
                    "#E7B10A",
                    "#064635",
                    "#519259",
                    "#698269",
                    "#898121"
                )
            )

            .setColorListener { color, colorhex ->
                holder.chosenColorButton.backgroundTintList = ColorStateList.valueOf(color)
                // Jeśli jest element z takim id to zamienia kolor, jeśli nie ma to dodaje typ ze zmienionym kolorem
                changedTypes.firstOrNull { it.id == type.id }?.apply { this.colour = colorhex }
                    ?: changedTypes.add(Types(type.id, type.name, colorhex))
                unsavedSettings.setValue(true)
            }
            .setColorShape(ColorShape.SQAURE)
            .show()
    }

    override suspend fun onTypeNameChange(type: Types, holder: TypeAdapter.ViewHolder) {
        changedTypes.firstOrNull {it.id == type.id}?.apply { this.name = type.name }
            ?: changedTypes.add(type)
    }
}