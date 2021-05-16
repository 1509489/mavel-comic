package com.pixelart.mavelcomics.ui.comiclist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.pixelart.mavelcomics.R
import com.pixelart.mavelcomics.databinding.FragmentComicListBinding
import com.pixelart.mavelcomics.models.Comic
import com.pixelart.mavelcomics.ui.comicdetail.ComicDetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComicListFragment : Fragment() {
    private var _binding: FragmentComicListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ComicListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentComicListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.itemList.apply {
            this.layoutManager = GridAutoFitLayoutManager(
                requireContext(),
                150,
                GridLayoutManager.HORIZONTAL,
                false
            )
            setHasFixedSize(true)
        }

        // Leaving this not using view binding as it relies on if the view is visible the current
        // layout configuration (layout, layout-sw600dp)
        val detailFragmentContainer: View? = view.findViewById(R.id.comic_detail_nav_container)
        observeComics(detailFragmentContainer)
    }

    private fun onComicClick(detailFragmentContainer: View?): (Comic) -> Unit = { comic ->
        val bundle = Bundle()
        bundle.putString(
            ComicDetailFragment.ARG_COMIC_ID,
            comic.id
        )
        if (detailFragmentContainer != null) {
            detailFragmentContainer.findNavController()
                .navigate(R.id.fragment_item_detail, bundle)
        } else {
            findNavController().navigate(R.id.comic_detail_fragment, bundle)
        }
    }

    private fun observeComics(detailFragmentContainer: View?) {
        val comicAdapter = ComicListPagingAdapter(onComicClick(detailFragmentContainer))
        binding.itemList.adapter = comicAdapter.withLoadStateHeaderAndFooter(
            header = ComicsLoadStateAdapter { comicAdapter.retry() },
            footer = ComicsLoadStateAdapter { comicAdapter.retry() }
        )

        comicAdapter.addLoadStateListener { loadState ->
            val isListEmpty = loadState.source.refresh is LoadState.NotLoading &&
                    comicAdapter.itemCount == 0
            val errorState = loadState.source.refresh is LoadState.Error

            binding.apply {
                itemList.isVisible = loadState.source.refresh is LoadState.NotLoading && !isListEmpty

                incStateView.apply {
                    pbLoading.isVisible = loadState.source.refresh is LoadState.Loading
                    btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                    tvMessage.isVisible = errorState || isListEmpty

                    tvMessage.text = if (errorState) {
                        (loadState.source.refresh as LoadState.Error).error.localizedMessage
                    } else getString(R.string.no_results)
                    btnRetry.setOnClickListener { comicAdapter.retry() }
                }
            }
        }

        viewModel.fetchComics().observe(viewLifecycleOwner, Observer { comicPagingData ->
            comicAdapter.submitData(viewLifecycleOwner.lifecycle, comicPagingData)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
