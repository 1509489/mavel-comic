package com.pixelart.mavelcomics.ui.comicdetail

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.pixelart.mavelcomics.R
import com.pixelart.mavelcomics.common.toUrl
import com.pixelart.mavelcomics.databinding.FragmentComicDetailBinding
import com.pixelart.mavelcomics.datasource.NetworkResource
import com.pixelart.mavelcomics.models.Comic
import dagger.hilt.android.AndroidEntryPoint
import eightbitlab.com.blurview.RenderScriptBlur


@AndroidEntryPoint
class ComicDetailFragment : Fragment() {

    private var _binding: FragmentComicDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ComicDetailViewModel by viewModels()

    private lateinit var comicId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_COMIC_ID)) {
                comicId = it.getString(ARG_COMIC_ID).orEmpty()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentComicDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            homeButton.setOnClickListener { findNavController().navigateUp() }
        }
        observeComic()
    }

    private fun observeComic() {
        viewModel.fetchComic(comicId).observe(viewLifecycleOwner, Observer { networkResource ->
            when (networkResource) {
                is NetworkResource.Loading -> {
                }
                is NetworkResource.Success -> networkResource.data?.let { comic ->
                    binding.apply {
                        setBackgroundImage(comic)
                        incComicContent.tvComicTitle.text = comic.title
                        incComicContent.tvComicDescription.text =
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                Html.fromHtml(comic.description, Html.FROM_HTML_MODE_COMPACT)
                            } else {
                                Html.fromHtml(comic.description)
                            }
                    }
                }
                is NetworkResource.Error -> {

                }
            }
        })
    }

    private fun setBackgroundImage(comic: Comic) {
        Glide.with(requireContext())
            .asDrawable()
            .load(comic.thumbnail.toUrl())
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(R.drawable.home_background)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    binding.incComicContent.scrollView.background = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
        setupBlurView()
    }

    private fun setupBlurView() {
        binding.incComicContent.blurView.apply {
            setupWith(binding.incComicContent.scrollView)
                .setFrameClearDrawable(binding.incComicContent.scrollView.background)
                .setBlurAlgorithm(RenderScriptBlur(requireContext()))
                .setBlurAutoUpdate(true)
                .setOverlayColor(ContextCompat.getColor(requireContext(), R.color.blur_overlay))
                .setHasFixedTransformationMatrix(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_COMIC_ID = "comicId"
    }
}
