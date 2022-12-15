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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import es.ua.eps.sqlite.database.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.channels.FileChannel

class MainActivity : AppCompatActivity() {

    val database by lazy {
        UserDatabase.getDatabase(this)
    }
    val repository by lazy {
        UserRepository(database.userDao())
    }
    private val userViewModel: UserViewModel by viewModels {
        UserModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = getString(R.string.app_name)

        val etUsername: EditText = findViewById(R.id.etUsername)
        val etPassword: EditText = findViewById(R.id.etPassword)


        findViewById<Button>(R.id.bLogin).setOnClickListener {
            val userName = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()
            if(userName.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "All inputs are required", Toast.LENGTH_SHORT).show()
            } else {
                val user = userViewModel.logIn(userName, password)
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
               val toast = if(backUp()){
                    Toast.makeText(this, "Backup successfully", Toast.LENGTH_SHORT)
                }else {
                    Toast.makeText(this, "Unable to make backup, allow permission", Toast.LENGTH_SHORT)
                }
                toast.show()
            }
        }
        if(id == R.id.action_restore_backup){
            if (Build.VERSION.SDK_INT >= 30 && !Environment.isExternalStorageManager()) {
                val uri = Uri.parse("package:" + this.packageName)
                startActivity(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri))
            } else {
                val toast: Toast?
                if(restore()){
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


    private fun backUp():Boolean {
        database.close()
        println("here")
        try {
            val sd: File = Environment.getExternalStorageDirectory()
            if (sd.canWrite()) {
                val backupDBPath = UserDatabase.DATABASE_NAME
                val currentDBPath = database.openHelper.writableDatabase.path

                val currentDB = File(currentDBPath)
                println("backUP " + currentDB.path)
                val backupDB = File(sd, backupDBPath)
                if (currentDB.exists()) {
                    val src: FileChannel = FileInputStream(currentDB).channel
                    val dst: FileChannel = FileOutputStream(backupDB).channel
                    dst.transferFrom(src, 0, src.size())
                    src.close()
                    dst.close()
                    return true
                }
                return false
            }
            return false
        } catch (e: Exception) {
            return false
        }
    }

    fun restore():Boolean {
        database.close()
        try {
            val sd = Environment.getExternalStorageDirectory()
            if (sd.canWrite()) {
                val backupDBPath = UserDatabase.DATABASE_NAME
                val currentDBPath = database.openHelper.writableDatabase.path
                val currentDB = File(currentDBPath)
                println("restore " + currentDB.path)
                val backupDB = File(sd, backupDBPath)
                if (currentDB.exists()) {
                    val src = FileInputStream(backupDB).channel
                    val dst = FileOutputStream(currentDB).channel
                    dst.transferFrom(src, 0, src.size())
                    src.close()
                    dst.close()
                    return true
                }
            }
            return false
        } catch (e: java.lang.Exception) {
            return false
        }
    }



    companion object {
        var LOGGED_USER: UserEntity? = null

    }
}