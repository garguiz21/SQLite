package es.ua.eps.sqlite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import es.ua.eps.sqlite.database.SQLiteManager
import es.ua.eps.sqlite.database.UserDTO

class UpdateUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user)
        title = getString(R.string.update_user)
        val userId: Int = intent.getIntExtra(ManageUser.UPDATE_USER_ID, 0)

        val dataBase = SQLiteManager.getInstance(this)

        val etFullName: EditText = findViewById(R.id.etUserUpdateInput)
        val etUserName: EditText = findViewById(R.id.etUserNameUpdateInput)
        val etEmail: EditText = findViewById(R.id.etEmailUpdateInput)
        val etPassword: EditText = findViewById(R.id.etPasswordUpdateInput)

        val user: UserDTO = dataBase.getUserQuery(userId.toInt())!!

        etFullName.setText(user.full_name)
        etUserName.setText(user.user_name)
        etEmail.setText(user.email)
        etPassword.setText(user.password)

        findViewById<Button>(R.id.bUpdateUserAdd).setOnClickListener {
            val fullName: String = etFullName.text.toString().trim()
            val userName: String = etUserName.text.toString().trim()
            val email: String = etEmail.text.toString().trim()
            val password: String = etPassword.text.toString().trim()
            if(!fullName.isEmpty() && !userName.isEmpty() && !email.isEmpty() && !password.isEmpty()){
                if(dataBase.updateUserExec(userId, userName, fullName, password, email)) {
                    Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show()
                    etFullName.setText("")
                    etUserName.setText("")
                    etEmail.setText("")
                    etPassword.setText("")
                } else {
                    Toast.makeText(this, "Couldn´t update user", Toast.LENGTH_SHORT).show()
                }
            }
        }
        findViewById<Button>(R.id.bBackManageUserUpdate).setOnClickListener {
            startActivity(Intent(this, ManageUser::class.java))
            finish()
        }

    }
}