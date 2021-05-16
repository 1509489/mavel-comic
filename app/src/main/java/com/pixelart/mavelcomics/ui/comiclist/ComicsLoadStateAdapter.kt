package com.pixelart.mavelcomics.ui.comiclist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pixelart.mavelcomics.R
import com.pixelart.mavelcomics.databinding.ViewLoadStateFooterItemBinding

class ComicsLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<ComicsLoadStateAdapter.ComicsLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: ComicsLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): ComicsLoadStateViewHolder {
        val binding = ViewLoadStateFooterItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ComicsLoadStateViewHolder(binding)
    }

    inner class ComicsLoadStateViewHolder(
        private val binding: ViewLoadStateFooterItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            binding.apply {
                btnRetry.setOnClickListener { retry.invoke() }
                if (loadState is LoadState.Error) {
                    errorMsg.text = loadState.error.localizedMessage
                }
                progressBar.isVisible = loadState is LoadState.Loading
                btnRetry.isVisible = loadState is LoadState.Error
                errorMsg.isVisible = loadState is LoadState.Error
            }
        }
    }
}
