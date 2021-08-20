package com.b1u3_22.nox

import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQuery
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.b1u3_22.nox.databinding.ActivityMainBinding
import com.b1u3_22.nox.db.internalDB
import com.b1u3_22.nox.db.settings.setting
import com.b1u3_22.nox.db.status.status

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database: internalDB = internalDB.getDatabaseInstance(this)
        var statusVal: status = database.statusDAO().getStatus()

        if (statusVal == null) {
            database.statusDAO().insertStatus(status(statusID = 0, status = true))
            statusVal = status(0, true)
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { _ ->
            // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //    .setAction("Action", null).show()
            if (database.statusDAO().getStatus().status!!){
                try {
                    findViewById<TextView>(R.id.status).text = getString(R.string.home_nox_status_off)
                } catch (e: NullPointerException){

                }
                this.binding.appBarMain.fab.backgroundTintList = ColorStateList.valueOf(getColor(R.color.red))
                database.statusDAO().updateStatus(status(statusID = 0, status = false))
            }else{
                try {
                    findViewById<TextView>(R.id.status).text = getString(R.string.home_nox_status_on)
                } catch (e: NullPointerException){

                }

                this.binding.appBarMain.fab.backgroundTintList = ColorStateList.valueOf(getColor(R.color.green))
                database.statusDAO().updateStatus(status(statusID = 0, status = true))
            }

        }

        if (statusVal.status!!){
            this.binding.appBarMain.fab.backgroundTintList = ColorStateList.valueOf(getColor(R.color.green))
        }else{
            this.binding.appBarMain.fab.backgroundTintList = ColorStateList.valueOf(getColor(R.color.red))
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_settings, R.id.nav_about,
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Reading values for settings for switches
        var allowAlarmsSetting: setting = database.settingsDAO().getSetting("allowAlarms")
        var allowOffsetSetting: setting = database.settingsDAO().getSetting("allowOffset")
        var allowWakeTimeSetting: setting = database.settingsDAO().getSetting("allowWakeTime")

        // Reading values for settings for timePickers
        var bedTimeSetting: setting = database.settingsDAO().getSetting("bedTime")
        var sleepTimeSetting: setting = database.settingsDAO().getSetting("sleepTime")
        var offsetTimeSetting: setting = database.settingsDAO().getSetting("offsetTime")
        var wakeTimeSetting: setting = database.settingsDAO().getSetting("wakeTime")

        println(allowAlarmsSetting)
        println(allowOffsetSetting)
        println(allowWakeTimeSetting)

        // Checking if they exist, if not insert default value
        // Switches
        if (allowAlarmsSetting == null){
            allowAlarmsSetting = setting(name = "allowAlarms")
            database.settingsDAO().insertSetting(allowAlarmsSetting)
        }

        if (allowOffsetSetting == null){
            allowOffsetSetting = setting(name = "allowOffset")
            database.settingsDAO().insertSetting(allowOffsetSetting)
        }

        if (allowWakeTimeSetting == null){
            allowWakeTimeSetting = setting(name = "allowWakeTime")
            database.settingsDAO().insertSetting(allowWakeTimeSetting)
        }

        // timePickers
        if (bedTimeSetting == null){
            bedTimeSetting = setting(name = "bedTime", value = "23:00")
            database.settingsDAO().insertSetting(bedTimeSetting)
        }
        if (sleepTimeSetting == null){
            sleepTimeSetting = setting(name = "sleepTime", value = "8:00")
            database.settingsDAO().insertSetting(sleepTimeSetting)
        }
        if (offsetTimeSetting == null){
            offsetTimeSetting = setting(name = "offsetTime", value = "00:15")
            database.settingsDAO().insertSetting(offsetTimeSetting)
        }
        if (wakeTimeSetting == null){
            wakeTimeSetting = setting(name = "wakeTime", value = "8:00")
            database.settingsDAO().insertSetting(wakeTimeSetting)
        }

        println(database.settingsDAO().getSettings())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}