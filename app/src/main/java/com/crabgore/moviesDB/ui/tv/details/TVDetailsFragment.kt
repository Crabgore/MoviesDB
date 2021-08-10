package com.crabgore.moviesDB.ui.tv.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crabgore.moviesDB.Const.Constants.Companion.DECORATION
import com.crabgore.moviesDB.R
import com.crabgore.moviesDB.common.*
import com.crabgore.moviesDB.common.Status.*
import com.crabgore.moviesDB.data.tv.models.TVDetailsResponse
import com.crabgore.moviesDB.databinding.FragmentTVDetailsBinding
import com.crabgore.moviesDB.databinding.FullImageLayoutBinding
import com.crabgore.moviesDB.ui.base.BaseFragment
import com.crabgore.moviesDB.ui.items.CreditsItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class TVDetailsFragment : BaseFragment() {
    val viewModel: TVDetailsViewModel by viewModel()

    private val args: TVDetailsFragmentArgs by navArgs()
    private val binding get() = _binding!! as FragmentTVDetailsBinding
    private lateinit var includeBinding: FullImageLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTVDetailsBinding.inflate(inflater, container, false)
        includeBinding = binding.fullImageLay
        return checkViewState()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startObservers()
        if (isViewWasNull) {
            initUI()
            getData()
        }
    }

    override fun backPressed() {
        if (binding.fullImageLay.fullImageLay.isVisible) binding.fullImageLay.fullImageLay.hide()
        else super.backPressed()
    }

    private fun initUI() {
        binding.apply {
            poster.setOnClickListener { showFullImage() }
            markAsFavoriteBtn.setOnClickListener { markAsFavorite() }
        }
    }

    private fun startObservers() {
        viewModel.apply {
            lifecycleScope.launch {
                tvState.collect { resource ->
                    when (resource.status) {
                        SUCCESS -> resource.data?.let { setInfo(it) }
                        ERROR -> Timber.d(resource.message)
                        LOADING -> showLoader()
                    }
                }
            }

            lifecycleScope.launch {
                castState.collect { resource ->
                    when (resource.status) {
                        SUCCESS -> resource.data?.let {
                            if (it.isNotEmpty()) setRV(binding.actorRv, it)
                            else binding.actorLayout.hide()
                        }
                        ERROR -> Timber.d(resource.message)
                        LOADING -> {
                        }
                    }
                }
            }

            lifecycleScope.launch {
                crewState.collect { resource ->
                    when (resource.status) {
                        SUCCESS -> resource.data?.let {
                            if (it.isNotEmpty()) setRV(binding.crewRv, it)
                            else binding.crewLayout.hide()
                        }
                        ERROR -> Timber.d(resource.message)
                        LOADING -> {
                        }
                    }
                }
            }

            isInFavoritesLD.observe(viewLifecycleOwner, { data ->
                data?.let {
                    changeButtonStatus(it)
                }
            })
        }
    }

    private fun getData() = viewModel.getData(args.tvId)

    private fun setInfo(tv: TVDetailsResponse) {
        binding.apply {
            tv.backdropPath?.let {
                loadImage(it, backdrop)
            }
            tv.posterPath?.let {
                loadImageWithPlaceHolder(it, poster)
            }
            title.text = tv.name
            tv.genres?.let {
                val genres = StringBuilder()
                for (i in it.indices) {
                    genres.append("${it[i].name}")
                    if (i < it.size - 1) genres.append(", ")
                }
                this.genres.text = genres.toString()
            }
            seasons.text =
                requireContext().getString(R.string.season_count, tv.numberOfSeasons.toString())
            episodes.text =
                requireContext().getString(R.string.episodes_count, tv.numberOfEpisodes.toString())
            tv.productionCountries?.let {
                val countries = StringBuilder()
                for (i in it.indices) {
                    countries.append("${it[i].name}")
                    if (i < it.size - 1) countries.append(", ")
                }
                country.text = requireContext().getString(R.string.country, countries)
            }
            tv.networks?.let {
                val networks = StringBuilder()
                for (i in it.indices) {
                    networks.append("${it[i].name}")
                    if (i < it.size - 1) networks.append(", ")
                }
                network.text = requireContext().getString(R.string.network, networks)
            }
            tv.firstAirDate?.let {
                year.text = requireContext().getString(R.string.year, formatDate(it))
            }
            tv.episodeRunTime?.let {
                if (it.isNotEmpty()) duration.text = requireContext().getString(
                    R.string.episode_duration,
                    it[0].toString()
                )
            }
            rating.text = tv.voteAverage.toString()
            tv.overview?.let {
                if (it != "") description.text = it
                else overviewCard.hide()
            } ?: overviewCard.hide()
            tv.homepage?.let {
                if (it != "") {
                    homepage.show()
                    homepage.setOnClickListener { goToHomepage(tv.homepage) }
                }
            }
        }
        hideLoader()
    }

    private fun setRV(recyclerView: RecyclerView, list: List<CreditsItem>) {
        val itemAdapter = ItemAdapter<CreditsItem>()
        val fastAdapter = FastAdapter.with(itemAdapter)
        itemAdapter.add(list)

        recyclerView.apply {
            while (itemDecorationCount > 0) {
                removeItemDecorationAt(0)
            }
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
            adapter = fastAdapter
            addDecoration(this, DECORATION)
        }

        fastAdapter.onClickListener = { view, adapter, item, position ->
            val directions =
                TVDetailsFragmentDirections.actionTVDetailsFragmentToPeopleDetailsFragment(item.id)
            navigateWithAction(directions)
            false
        }
    }

    private fun showFullImage() {
        val photo = viewModel.tvState.value.data?.posterPath
        photo?.let {
            loadImage(it, includeBinding.fullPicture)
            binding.fullImageLay.fullImageLay.show()
        }
    }

    private fun changeButtonStatus(boolean: Boolean) {
        binding.markAsFavoriteBtn.apply {
            if (boolean) {
                icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorite_fill)
                text = requireContext().getString(R.string.remove_from_favorites)
            } else {
                icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorite)
                text = requireContext().getString(R.string.mark_as_favorite)
            }
        }
    }

    private fun markAsFavorite() {
        if (viewModel.checkSession() == null) navigate(R.id.loginFragment)
        else viewModel.markAsFavorite(args.tvId)
    }

    private fun goToHomepage(url: String?) {
        val browse = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browse)
    }
}