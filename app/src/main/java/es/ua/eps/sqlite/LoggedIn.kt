package es.ua.eps.sqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class LoggedIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged_in)
        title = getString(R.string.user_data)

        val tvUser: TextView = findViewById(R.id.tvUser)
        val tvUserName: TextView = findViewById(R.id.tvUsername)

        tvUser.text = MainActivity.LOGGED_USER?.full_name
        tvUserName.text = MainActivity.LOGGED_USER?.user_name

        findViewById<Button>(R.id.bBack).setOnClickListener {
            finish()
        }
    }
}