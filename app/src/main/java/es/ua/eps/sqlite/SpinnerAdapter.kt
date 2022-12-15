package es.ua.eps.sqlite

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import es.ua.eps.sqlite.database.UserEntity


class SpinnerAdapter(val context: Context?, val objects: List<UserEntity>) :
    BaseAdapter() {

    val inflater = (LayoutInflater.from(context))

    override fun getCount(): Int {
        return objects.size
    }

    override fun getItem(position: Int): UserEntity? {
        return objects[position]
    }

    override fun getItemId(position: Int): Long {
        return objects[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = inflater.inflate(R.layout.spinner_item, parent)
        val name: TextView = view.findViewById(R.id.spinnerText)
        name.text = objects[position].userName

        return view
    }

}