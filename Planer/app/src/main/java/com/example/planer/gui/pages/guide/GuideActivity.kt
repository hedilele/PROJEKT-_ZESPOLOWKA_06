package com.example.planer.gui.pages.guide
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.planer.databinding.ActivityGuideBinding
import com.example.planer.gui.ViewPager2Adapter

class GuideActivity : AppCompatActivity() {

    var list = mutableListOf<String>()

    private lateinit var binding: ActivityGuideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set full screen
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager: ViewPager2 = binding.viewpager

        val fragments: ArrayList<Fragment> = arrayListOf(
            GuideHomePageFragment(),        // 0
            GuideCalendarFragment(),        // 1
            GuideFilterFragment(),          // 2
            GuidePomodoroFragment(),        // 3
            GuideSettingsFragment(),        // 4
            GuideScopeModeFragment(),       // 5
            GuideNotesFragment()            // 6
        )

        val adapterViewPager = ViewPager2Adapter(fragments, supportFragmentManager, lifecycle)
        viewPager.adapter = adapterViewPager

        list.add("Strona główna")       // 0
        list.add("Kalendarz")           // 1
        list.add("Filtrowanie")         // 2
        list.add("Pomodoro")            // 3
        list.add("Ustawienia")          // 4
        list.add("Przegląd")            // 5
        list.add("Notatki")             // 6

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
        binding.spinner.adapter = adapter
        binding.spinner.setSelection(0)
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long)
            {
                when(position)
                {
                    0 -> {
                        viewPager.setCurrentItem(0)
                    }
                    1 -> {
                        viewPager.setCurrentItem(1)
                    }
                    2 -> {
                        viewPager.setCurrentItem(2)
                    }
                    3 -> {
                        viewPager.setCurrentItem(3)
                    }
                    4-> {
                        viewPager.setCurrentItem(4)
                    }
                    5 -> {
                        viewPager.setCurrentItem(5)
                    }
                    6 -> {
                        viewPager.setCurrentItem(6)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                binding.spinner.setSelection(0)
            }
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when(position)
                {
                    0 -> {
                        binding.spinner.setSelection(0)
                    }
                    1 -> {
                        binding.spinner.setSelection(1)
                    }
                    2 -> {
                        binding.spinner.setSelection(2)
                    }
                    3 -> {
                        binding.spinner.setSelection(3)
                    }
                    4-> {
                        binding.spinner.setSelection(4)
                    }
                    5 -> {
                        binding.spinner.setSelection(5)
                    }
                    6 -> {
                        binding.spinner.setSelection(6)
                    }
                }
            }
        })

        binding.btnLeave.setOnClickListener{
            finish()
        }
    }
}