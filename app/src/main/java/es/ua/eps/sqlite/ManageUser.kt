package es.ua.eps.sqlite

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import es.ua.eps.sqlite.database.SQLiteManager
import es.ua.eps.sqlite.database.UserDTO

class ManageUser : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var itemSelected: Int = 0
    var userList: ArrayList<UserDTO> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_user)
        title = getString(R.string.manage_users)
        val dataBase: SQLiteManager = SQLiteManager.getInstance(this)
        userList = dataBase.getUsersQuery()

        val spinner = findViewById<Spinner>(R.id.spinner)
        spinner.onItemSelectedListener = this

        val adapter = SpinnerAdapter(applicationContext, userList)
        spinner.adapter = adapter



        val bUpdateUser: Button = findViewById(R.id.bUpdateUser)
        bUpdateUser.isEnabled = userList.isNotEmpty()
        val bDeleteUser: Button = findViewById(R.id.bDeleteUser)
        bDeleteUser.isEnabled = userList.isNotEmpty()

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
                    val isSuccessfully = dataBase.deleteUserExec(itemSelected)
                    println(isSuccessfully)
                    if(isSuccessfully){
                        userList = dataBase.getUsersRawQuery()
                        spinner.adapter = SpinnerAdapter(applicationContext, userList)
                        bUpdateUser.isEnabled = userList.isNotEmpty()
                        bDeleteUser.isEnabled = userList.isNotEmpty()
                    }
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
