package es.ua.eps.sqlite.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.ua.eps.sqlite.R
import es.ua.eps.sqlite.database.UserDTO

class RecyclerAdapter(val users: ArrayList<UserDTO>): RecyclerView.Adapter<RecyclerAdapter.ViewHolder?>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var username: TextView
        var fullname: TextView
        var email: TextView

        fun bind(l: UserDTO) {
            username.text = l.user_name
            fullname.text = l.full_name
            email.text = l.email
        }

        init {
            username = v.findViewById(R.id.tvUserNameRow)
            fullname = v.findViewById(R.id.tvFullNameRow)
            email = v.findViewById(R.id.tvEmailRow)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int {
        return users.size
    }
}