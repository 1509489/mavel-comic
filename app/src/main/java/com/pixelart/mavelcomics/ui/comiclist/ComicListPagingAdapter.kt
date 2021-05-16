package com.pixelart.mavelcomics.ui.comiclist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.pixelart.mavelcomics.R
import com.pixelart.mavelcomics.common.toUrl
import com.pixelart.mavelcomics.databinding.ComicListItemBinding
import com.pixelart.mavelcomics.models.Comic

class ComicListPagingAdapter(
    private val onComicClick: (comic: Comic) -> Unit
) : PagingDataAdapter<Comic, ComicListPagingAdapter.ComicListHolder>(DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Comic>() {
            override fun areItemsTheSame(oldItem: Comic, newItem: Comic): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Comic, newItem: Comic): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ComicListPagingAdapter.ComicListHolder {
        val binding = ComicListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ComicListHolder(binding)
    }

    override fun onBindViewHolder(holder: ComicListPagingAdapter.ComicListHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ComicListHolder(
        private val binding: ComicListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(comic: Comic) {
            binding.apply {
                Glide.with(root.context)
                    .load(comic.thumbnail.toUrl())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.home_background)
                    .error(R.drawable.image_error)
                    .into(ivComic)
                ivComic.setOnClickListener { onComicClick.invoke(comic) }
            }
        }
    }
}
