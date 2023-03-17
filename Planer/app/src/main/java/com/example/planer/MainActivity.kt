package com.example.planer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.planer.databinding.ActivityMainBinding
import com.example.planer.gui.AddingTaskActivity
import com.example.planer.gui.pages.HomeFragment
import com.example.planer.gui.pages.CalendarFragment
import com.example.planer.gui.pages.DrawerFragment
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle

    private lateinit var db : AppDatabase


    private lateinit var pagerAdapters: PagerAdapters


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        //val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "TOP.sqlite")

        // dodanie fragmentów do listy
        pagerAdapters = PagerAdapters(supportFragmentManager)
        pagerAdapters.addFragment(HomeFragment())
        pagerAdapters.addFragment(DrawerFragment())
        pagerAdapters.addFragment(CalendarFragment())

        // przypisanie adaptera zajmującego się fragmentami do adaptera pagerView
        binding.pagerView.adapter = pagerAdapters


        // nawigacja - po kliknięciu na odpowiednią ikonę przenosi nas do danego fragmentu
        // strona główna (z recyclerView)
        binding.buttonHome.setOnClickListener{
            binding.pagerView.setCurrentItem(0)
        }

        // menu boczne - wysuwanie
        binding.buttonMenu.setOnClickListener{
            //binding.myPagerView.setCurrentItem(1)
            binding.drawerLayout.openDrawer(GravityCompat.START);
        }

        // kalendarz
        binding.buttonCalendar.setOnClickListener{
            binding.pagerView.setCurrentItem(2)
        }

        // dodanie tasków - przekierowanie do nowej aktywności
        binding.buttonAdd.setOnClickListener{
            //binding.myPagerView.setCurrentItem(3)
            lifecycleScope.launch {
                val intent = Intent(applicationContext, AddingTaskActivity::class.java)
                startActivity(intent)
            }

        }


//        binding.buttonHome.setOnFocusChangeListener { v, hasFocus ->
//            if (hasFocus)
//                binding.buttonHome.setColorFilter(R.color.green1)
//        }



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
