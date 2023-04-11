package com.example.planer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.example.planer.databinding.ActivityMainBinding
import com.example.planer.gui.AddingTaskActivity
import com.example.planer.gui.HideNextPage
import com.example.planer.gui.pages.CalendarFragment
import com.example.planer.gui.pages.DrawerFragment
import com.example.planer.gui.pages.FilterFragment
import com.example.planer.gui.pages.HomeFragment
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle

    private lateinit var db : AppDatabase

    private lateinit var pagerAdapters: PagerAdapters

    var byDrawer = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "TOP.sqlite")

        // dodanie fragmentów do listy
        pagerAdapters = PagerAdapters(supportFragmentManager)
        pagerAdapters.addFragment(HomeFragment())       // 1
        pagerAdapters.addFragment(CalendarFragment())   // 2
        pagerAdapters.addFragment(FilterFragment())     // 3

        // przypisanie adaptera zajmującego się fragmentami do adaptera pagerView
        binding.pagerView.adapter = pagerAdapters




        // nawigacja - po kliknięciu na odpowiednią ikonę przenosi nas do danego fragmentu
        // strona główna (z recyclerView)
        binding.buttonHome.setOnClickListener{
            byDrawer = 0

            //możliwosc scrollowania wlaczona
            binding.pagerView.setOnTouchListener { arg0, arg1 -> false }

            binding.pagerView.setCurrentItem(0)

        }

        // menu boczne - wysuwanie
        binding.buttonMenu.setOnClickListener{
            //binding.myPagerView.setCurrentItem(1)
            binding.drawerLayout.openDrawer(GravityCompat.START);
            //byDrawer = 0
        }

        // kalendarz
        binding.buttonCalendar.setOnClickListener{
            binding.pagerView.setCurrentItem(1)

            //możliwosc scrollowania wlaczona
            binding.pagerView.setOnTouchListener { arg0, arg1 -> false }
            byDrawer = 0
        }

        // dodanie tasków - przekierowanie do nowej aktywności
        binding.buttonAdd.setOnClickListener{
            //binding.myPagerView.setCurrentItem(3)

            byDrawer = 0

            //możliwosc scrollowania wlaczona
            binding.pagerView.setOnTouchListener { arg0, arg1 -> false }

            lifecycleScope.launch {
                val intent = Intent(applicationContext, AddingTaskActivity::class.java)
                startActivity(intent)
            }

        }


       binding.pagerView.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
           override fun onPageScrollStateChanged(state: Int) {
               // This method will be invoked when the scroll state changes.
           }

           override fun onPageScrolled(
               position: Int,
               positionOffset: Float,
               positionOffsetPixels: Int
           ) {
               // This method will be invoked when the current page is scrolled.
               if (position == 1 && byDrawer == 0)
               {
                   binding.pagerView.setCurrentItem(1)
                   binding.pagerView.setPageTransformer(false, HideNextPage())
                   return
               }
           }

           override fun onPageSelected(position: Int) {
               // This method will be invoked when a new page becomes selected.

           }
       })





        // menu boczne - mechanizm odpowiedzialny za wysuwanie
        binding.apply {
            toggle = ActionBarDrawerToggle(
                this@MainActivity,
                drawerLayout,
                R.string.open,
                R.string.close
            )
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            binding?.navigation?.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.item1 -> {
                        Toast.makeText(this@MainActivity, "111", Toast.LENGTH_SHORT).show()
                        binding.pagerView.setCurrentItem(3)

                        //możliwosc scrollowania wylaczona
                        binding.pagerView.setOnTouchListener { arg0, arg1 -> true }

                        byDrawer = 1

                        //supportFragmentManager.beginTransaction().replace(R.id.ll_ftagments, FilterFragment()).commitAllowingStateLoss()

                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                    }
                    R.id.item2 -> {
                        Toast.makeText(this@MainActivity, "222", Toast.LENGTH_SHORT).show()
                    }
                    R.id.item3 -> {
                        Toast.makeText(this@MainActivity, "333", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
        }


}
}



// może mi się jeszcze przydać
/*
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle

    private lateinit var db : AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_main)
        setContentView(binding?.root)

// menu boczne
        binding.apply {
            toggle = ActionBarDrawerToggle(this@MainActivity, drawerLayout, R.string.open, R.string.close)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            binding?.navigation?.setNavigationItemSelectedListener {
                when(it.itemId) {
                    R.id.item1 -> {
                        Toast.makeText(this@MainActivity, "111", Toast.LENGTH_SHORT).show()
                    }
                    R.id.item2 -> {
                        Toast.makeText(this@MainActivity, "222", Toast.LENGTH_SHORT).show()
                    }
                    R.id.item3 -> {
                        Toast.makeText(this@MainActivity, "333", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
        }

// baza danych
        val tasksDao = (application as DatabaseApp).db.tasksDAO()

        binding?.btnAdd?.setOnClickListener{
            //addHabit(habitsDao)
            val intent = Intent(this, AddingTaskActivity::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch{
            tasksDao.fetchAll().collect{
                val list = ArrayList(it)
                setupListOfDataIntoRecyclerView(list, tasksDao)
            }
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            true
        }

        return super.onOptionsItemSelected(item)
    }
    private fun setupListOfDataIntoRecyclerView(taskList:ArrayList<Tasks>, tasksDAO: TasksDAO) {

        if(taskList.isNotEmpty()){
            val taskAdapter = Adapter(taskList,
                {updateId -> },
                {deleteId ->
                    lifecycleScope.launch{
                        tasksDAO.findTaskById(deleteId).collect{
                            if(it != null) {
                                deleteRecord(deleteId, tasksDAO, it)
                            }
                        }
                    }
                })

            binding?.taskList?.layoutManager = LinearLayoutManager(this)
            binding?.taskList?.adapter = taskAdapter
            binding?.taskList?.visibility = View.VISIBLE

        }
        else{
            binding?.taskList?.visibility = View.GONE
        }

    }

    fun deleteRecord(id:Int ,tasksDAO: TasksDAO, tasks: Tasks) {

        /*
            val builder = AlertDialog.Builder(this)
            //set title for alert dialog
            builder.setTitle("Delete Record")
            //set message for alert dialog
            builder.setMessage("Are you sure you wants to delete ${employee.name}.")
            builder.setIcon(android.R.drawable.ic_dialog_alert)

         */
        //performing positive action
        // builder.setPositiveButton("Yes") { dialogInterface, _ ->
        lifecycleScope.launch {
            tasksDAO.delete(tasksDAO.findTaskById(id).first())


            //  dialogInterface.dismiss() // Dialog will be dismissed
        }

    }

}*/
