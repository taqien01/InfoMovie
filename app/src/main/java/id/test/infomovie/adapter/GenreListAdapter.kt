package id.test.infomovie.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import id.test.infomovie.R
import id.test.infomovie.data.model.Genres

class GenreListAdapter(private val context : Context, private var list : MutableList<Genres>) :
    RecyclerView.Adapter<GenreListAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(
        onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClick(data: Genres)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.satu_genre,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val getGenre = list.get(position)
        holder.nameGenre?.text = getGenre.name

        holder.itemView.setOnClickListener(){
            onItemClickCallback.onItemClick(getGenre)
        }
    }

    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var nameGenre: TextView? = null

        init {
            nameGenre = view.findViewById(R.id.name)
        }

    }

}