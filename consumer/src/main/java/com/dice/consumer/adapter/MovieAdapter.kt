package com.dice.consumer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dice.consumer.R
import com.dice.consumer.model.Movie
import kotlinx.android.synthetic.main.item_film.view.*

class MovieAdapter (private val context: Context?): RecyclerView.Adapter<MovieAdapter.FilmViewHolder>(){
    private val movieList = ArrayList<Movie>()

    companion object{
        internal var clickListener: ClickListener? = null
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        MovieAdapter.clickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(data: Movie, v: View)
    }

    fun setData(items: ArrayList<Movie>) {
        movieList.clear()
        movieList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_film, parent, false)
        return FilmViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    class FilmViewHolder(private val view: View):RecyclerView.ViewHolder(view)  {
        fun bind(film: Movie){
            with(view){
                Glide.with(context)
                    .load("https://image.tmdb.org/t/p/w185/"+film.poster_path)
                    .placeholder(R.color.placeholder)
                    .error(R.drawable.empty)
                    .into(imgPoster)

                tvTitle.text = film.title
                tvDateRelease.text = film.release_date

                view.setOnClickListener {
                    clickListener?.onItemClick(film, it)
                }
            }
        }
    }
}