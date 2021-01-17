package com.example.timglog

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var   topBar:LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(MainActivity::class.java.name, "oncreate ")

        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { _ ->
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_nav_home_to_nav_newtask)
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_stats), drawerLayout)
//        appBarConfiguration = AppBarConfiguration(setOf(
//            R.id.nav_home), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->  //3
            if (destination.id in arrayOf(
                            R.id.nav_home
                    )
            ) {
                fab.show()
            } else {
                fab.hide()
            }


        }

        val taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        topBar= findViewById(R.id.top_bar)
        topBar.setOnClickListener{
            navController.navigate(R.id.nav_task)
        }
        val barTime :TextView = findViewById(R.id.bar_time)
        val barTitle :TextView = findViewById(R.id.bar_title)

        taskViewModel.duration_text.observe(this, Observer {  barTime.text = it })
        taskViewModel.title.observe(this, Observer {  barTitle.text = it })

        if(taskViewModel.isRunning()){
            showTopBar()

            navController.navigate(R.id.action_nav_home_to_nav_task)
        }




    }

    fun hideTopBar(){
        topBar.visibility = View.GONE
    }
    fun showTopBar(){
        topBar.visibility = View.VISIBLE
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}