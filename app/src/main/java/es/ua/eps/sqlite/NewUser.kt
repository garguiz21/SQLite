package es.ua.eps.sqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import es.ua.eps.sqlite.database.SQLiteManager

class NewUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)
        title = getString(R.string.new_user)

        val dataBase = SQLiteManager.getInstance(this)

        val etFullName: EditText = findViewById(R.id.etUserInput)
        val etUserName: EditText = findViewById(R.id.etUserNameInput)
        val etEmail: EditText = findViewById(R.id.etEmailInput)
        val etPassword: EditText = findViewById(R.id.etPasswordInput)


        findViewById<Button>(R.id.bUpdateUserAdd).setOnClickListener {
            val fullName: String = etFullName.text.toString().trim()
            val userName: String = etUserName.text.toString().trim()
            val email: String = etEmail.text.toString().trim()
            val password: String = etPassword.text.toString().trim()
            if(!fullName.isEmpty() && !userName.isEmpty() && !email.isEmpty() && !password.isEmpty()){
                if(dataBase.insertUser(userName, fullName, password, email)) {
                    Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show()
                    etFullName.setText("")
                    etUserName.setText("")
                    etEmail.setText("")
                    etPassword.setText("")
                } else {
                    Toast.makeText(this, "Couldn´t add user", Toast.LENGTH_SHORT).show()
                }
            }
        }
        findViewById<Button>(R.id.bBackManageUserUpdate).setOnClickListener {
            finish()
        }
    }
}