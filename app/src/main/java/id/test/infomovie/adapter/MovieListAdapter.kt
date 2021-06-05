package id.test.infomovie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.test.infomovie.BuildConfig
import id.test.infomovie.R

import android.os.Handler
import id.test.infomovie.data.config.Constant.Constant
import id.test.infomovie.data.model.Genres
import id.test.infomovie.data.model.MoviesGenres
import id.test.infomovie.data.model.Results

class MovieListAdapter(private val context : Context, private var list : MutableList<Results>)
    : RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(
        onItemClickCallback: OnItemClickCallback
    ) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClick(data: Results)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.satu_movie,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val satuMovie = list.get(position)
        holder.judul?.text = satuMovie.title
        Glide.with(context).load(BuildConfig.SMALL_IMAGE_URL+""+satuMovie.poster_path).into(holder.gambar)

        holder.itemView.setOnClickListener(){
            onItemClickCallback.onItemClick(satuMovie)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(var view : View) : RecyclerView.ViewHolder(view) {
        var judul: TextView? = null
        var gambar: ImageView

        init {
            judul = view.findViewById(R.id.name)
            gambar = view.findViewById(R.id.imageView)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position] == null) {
            Constant.VIEW_TYPE_LOADING
        } else {
            Constant.VIEW_TYPE_ITEM
        }
    }

    fun addData(dataViews: MutableList<Results>) {
        this.list.addAll(dataViews)
        notifyDataSetChanged()
    }

    fun getItemAtPosition(position: Int): Results {
        return list[position]
    }

    fun addLoadingView() {
        //add loading item
        Handler().post {
            //list.add(null)
            notifyItemInserted(list.size - 1)
        }
    }

    fun removeLoadingView() {
        //Remove loading item
        if (list.size != 0) {
            list.removeAt(list.size - 1)
            notifyItemRemoved(list.size)
        }
    }
}