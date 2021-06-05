package id.test.infomovie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.test.infomovie.R
import id.test.infomovie.data.model.review.ResultsDetail

class ReviewListAdapter(private val context: Context, private val list: MutableList<ResultsDetail>) :
    RecyclerView.Adapter<ReviewListAdapter.ViewHolder>() {

    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var nama: TextView? = null
        var rating: TextView? = null
        var tanggal: TextView? = null
        var review: TextView? = null

        init {
            nama = view.findViewById(R.id.txt_nama)
            rating = view.findViewById(R.id.txt_rating)
            tanggal = view.findViewById(R.id.txt_tanggal)
            review = view.findViewById(R.id.txt_review)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.satu_review,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val  r = list.get(position)

        holder.nama?.text = r.author
        holder.rating?.text = r.author_details.rating+" / 10"
        holder.tanggal?.text = r.created_at.substring(0, 10)
        holder.review?.text = r.content

    }

    override fun getItemCount(): Int {
        return list.size
    }
}