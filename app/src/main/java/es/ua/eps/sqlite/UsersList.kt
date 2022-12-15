package es.ua.eps.sqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.ua.eps.sqlite.adapters.RecyclerAdapter
import es.ua.eps.sqlite.database.*

class UsersList : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var adapter: RecyclerView.Adapter<*>? = null
    var layoutManager: RecyclerView.LayoutManager? = null

    val database by lazy {
        UserDatabase.getDatabase(this)
    }
    val repository by lazy {
        UserRepository(database.userDao())
    }
    private val userViewModel: UserViewModel by viewModels {
        UserModelFactory(repository)
    }
    var userList: List<UserEntity> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_list)
        title = "Users"
        recyclerView  = findViewById(R.id.users_list)
        layoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManager

        userViewModel.users.observe(this){
            userList = it
            adapter = RecyclerAdapter(it)
            recyclerView?.adapter = adapter
        }


        findViewById<Button>(R.id.bBackManage).setOnClickListener {
            finish()
        }

    }
}