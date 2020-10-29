package com.bisapp.threadingexamples

import android.Manifest
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : AppCompatActivity() {

    var perms = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions()
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))




    }

    companion object{
        const val RC_CALL_APP_PERM: Int = 4
        var hasPermissions = false;
    }

    @AfterPermissionGranted(RC_CALL_APP_PERM)
    fun requestPermissions() {
        if (EasyPermissions.hasPermissions(this,*perms)) {
            hasPermissions = true
        } else {
            //if permission is denied
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.enable_storage_access),
                RC_CALL_APP_PERM,
                *perms
            )
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}