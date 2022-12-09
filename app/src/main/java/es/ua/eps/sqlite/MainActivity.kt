package es.ua.eps.sqlite

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import es.ua.eps.sqlite.database.SQLiteManager
import es.ua.eps.sqlite.database.UserDTO

class MainActivity : AppCompatActivity() {
    var dataBase: SQLiteManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = getString(R.string.app_name)
        dataBase = SQLiteManager.getInstance(this)

        val etUsername: EditText = findViewById(R.id.etUsername)
        val etPassword: EditText = findViewById(R.id.etPassword)


        findViewById<Button>(R.id.bLogin).setOnClickListener {
            val userName = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()
            if(userName.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "All inputs are requiered", Toast.LENGTH_SHORT).show()
            } else {
                val user = dataBase?.logIn(userName, password)
                if(user != null){
                    LOGGED_USER = user
                    startActivity(Intent(this, LoggedIn::class.java))
                } else {
                    Toast.makeText(this, "Error usuario/password incorrectos", Toast.LENGTH_SHORT).show()
                }
            }
        }
        findViewById<Button>(R.id.bClose).setOnClickListener {
            finishAffinity()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == R.id.action_create_backup){
            if (Build.VERSION.SDK_INT >= 30 && !Environment.isExternalStorageManager()) {
                val uri = Uri.parse("package:" + this.packageName)
                startActivity(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri))
            } else {
                val isSuccess = dataBase?.backUp()
                var toast: Toast? = null
                if(isSuccess == true){
                    toast = Toast.makeText(this, "Backup successfully", Toast.LENGTH_SHORT)
                }else {
                    toast = Toast.makeText(this, "Unable to make backup, allow permission", Toast.LENGTH_SHORT)
                }
                toast.show()
            }
        }
        if(id == R.id.action_restore_backup){
            if (Build.VERSION.SDK_INT >= 30 && !Environment.isExternalStorageManager()) {
                val uri = Uri.parse("package:" + this.packageName)
                startActivity(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri))
            } else {
                val isSuccess = dataBase?.restore()
                var toast: Toast? = null
                if(isSuccess == true){
                    toast = Toast.makeText(this, "Backup has been loaded", Toast.LENGTH_SHORT)
                }else {
                    toast = Toast.makeText(this, "Unable to load backup", Toast.LENGTH_SHORT)
                }
                toast.show()
            }
        }
        if(id == R.id.action_manage_users){
            startActivity(Intent(this, ManageUser::class.java))
        }
        return super.onOptionsItemSelected(item)
    }


    companion object {
        var LOGGED_USER: UserDTO? = null
    }
}