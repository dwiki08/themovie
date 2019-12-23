package com.dice.themovie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dice.themovie.R
import com.dice.themovie.model.network.ApiClient
import com.dice.themovie.model.TvShow
import kotlinx.android.synthetic.main.item_film.view.*

class TvShowAdapter (private val context: Context?): RecyclerView.Adapter<TvShowAdapter.TvShowViewHolder>(){

    private var tvShowList = listOf<TvShow>()

    companion object{
        internal var clickListener: ClickListener? = null
    }

    fun setOnClickListener(clickListener: ClickListener){
        TvShowAdapter.clickListener = clickListener
    }

    interface ClickListener{
        fun onItemClick(data: TvShow, view: View)
    }

    fun setData(items: List<TvShow>) {
        tvShowList = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_film, parent, false)
        return TvShowViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tvShowList.size
    }

    override fun onBindViewHolder(holder: TvShowViewHolder, position: Int) {
        holder.bind(tvShowList[position])
    }

    class TvShowViewHolder(private val view: View): RecyclerView.ViewHolder(view)  {
        fun bind(film: TvShow){
            with(view){
                Glide.with(context)
                    .load(ApiClient.POSTER_URL+film.poster_path)
                    .placeholder(R.color.placeholder)
                    .error(R.drawable.empty)
                    .into(imgPoster)

                tvTitle.text = film.original_name
                tvDateRelease.text = film.first_air_date

                view.setOnClickListener {
                    clickListener?.onItemClick(film, it)
                }
            }
        }
    }

}