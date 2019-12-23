package com.dice.themovie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dice.themovie.R
import com.dice.themovie.model.network.ApiClient
import com.dice.themovie.model.Movie
import kotlinx.android.synthetic.main.item_film.view.*

class FavoriteMovieAdapter (private val context: Context?): RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteFilmViewHolder>(){
    private var movieList = listOf<Movie>()

    companion object{
        internal var clickListener: ClickListener? = null
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        FavoriteMovieAdapter.clickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(data: Movie, v: View)
    }

    fun setData(items: List<Movie>) {
        movieList = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteFilmViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_film, parent, false)
        return FavoriteFilmViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: FavoriteFilmViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    class FavoriteFilmViewHolder(private val view: View):RecyclerView.ViewHolder(view)  {
        fun bind(film: Movie){
            with(view){
                Glide.with(context)
                    .load(ApiClient.POSTER_URL+film.poster_path)
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