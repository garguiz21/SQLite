package es.ua.eps.sqlite

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import es.ua.eps.sqlite.database.*

class ManageUser : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var itemSelected: Int = 0
    private var userList: List<UserEntity> = ArrayList()
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
        setContentView(R.layout.activity_manage_user)
        title = getString(R.string.manage_users)

        val spinner = findViewById<Spinner>(R.id.spinner)
        spinner.onItemSelectedListener = this
        val bUpdateUser: Button = findViewById(R.id.bUpdateUser)
        val bDeleteUser: Button = findViewById(R.id.bDeleteUser)

        userViewModel.users.observe(this){
            userList = it
            val adapter = SpinnerAdapter(applicationContext, userList)
            spinner.adapter = adapter
            bUpdateUser.isEnabled = userList.isNotEmpty() == true
            bDeleteUser.isEnabled = userList.isNotEmpty() == true
        }


        findViewById<Button>(R.id.bNewUser).setOnClickListener {
            startActivity(Intent(this, NewUser::class.java))
            finish()
        }
        bUpdateUser.setOnClickListener {
            val updateIntent = Intent(this, UpdateUser::class.java)
            updateIntent.putExtra(UPDATE_USER_ID, itemSelected)
            startActivity(updateIntent)
            finish()
        }
        bDeleteUser.setOnClickListener {
            val builder = AlertDialog.Builder(this)
                .setTitle("Delete User")
                .setMessage("Do you really want to delete the selected user?")
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    userViewModel.deleteUser(userList[itemSelected])
                }.setNegativeButton(android.R.string.cancel) {_,_ ->}
            builder.show()
        }
        findViewById<Button>(R.id.bListUsers).setOnClickListener {
            startActivity(Intent(this, UsersList::class.java))
        }
        findViewById<Button>(R.id.bBackHome).setOnClickListener {
            finish()
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        itemSelected = userList[position].id
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    companion object{
       val UPDATE_USER_ID = "UPDATE_USER_ID"
    }
}
