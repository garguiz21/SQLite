package es.ua.eps.sqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.ua.eps.sqlite.adapters.RecyclerAdapter
import es.ua.eps.sqlite.database.SQLiteManager
import es.ua.eps.sqlite.database.UserDTO

class UsersList : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var adapter: RecyclerView.Adapter<*>? = null
    var layoutManager: RecyclerView.LayoutManager? = null

    var userList: ArrayList<UserDTO> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_list)
        title = "Users"
        val dataBase: SQLiteManager = SQLiteManager.getInstance(this)
        userList = dataBase.getUsersQuery()

        recyclerView  = findViewById(R.id.users_list)
        layoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManager

        adapter = RecyclerAdapter(userList)
        recyclerView?.adapter = adapter

        findViewById<Button>(R.id.bBackManage).setOnClickListener {
            finish()
        }

    }
}