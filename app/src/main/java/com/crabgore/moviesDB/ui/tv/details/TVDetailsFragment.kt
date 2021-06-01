package com.crabgore.moviesDB.ui.tv.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crabgore.moviesDB.Const.Constants.Companion.DECORATION
import com.crabgore.moviesDB.R
import com.crabgore.moviesDB.common.*
import com.crabgore.moviesDB.data.TVDetailsResponse
import com.crabgore.moviesDB.databinding.FragmentTVDetailsBinding
import com.crabgore.moviesDB.ui.base.BaseFragment
import com.crabgore.moviesDB.ui.items.CreditsItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import javax.inject.Inject

class TVDetailsFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<TVDetailsViewModel> { viewModelFactory }

    private val args: TVDetailsFragmentArgs by navArgs()
    private val binding get() = _binding!! as FragmentTVDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTVDetailsBinding.inflate(inflater, container, false)
        return checkViewState()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isViewWasNull) {
            initUI()
            startObservers()
            getData()
        }
    }

    override fun backPressed() {
        if (binding.fullImageLay.isVisible) binding.fullImageLay.hide()
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
            TVLD.observe(viewLifecycleOwner, { data ->
                data?.let {
                    setInfo(it)
                }
            })

            isInFavoritesLD.observe(viewLifecycleOwner, { data ->
                data?.let {
                    changeButtonStatus(it)
                }
            })

            castLD.observe(viewLifecycleOwner, { data ->
                data?.let {
                    if (it.isNotEmpty()) setRV(binding.actorRv, it)
                    else binding.actorLayout.hide()
                }
            })

            crewLD.observe(viewLifecycleOwner, { data ->
                data?.let {
                    if (it.isNotEmpty()) setRV(binding.crewRv, it)
                    else binding.crewLayout.hide()
                }
            })

            observeLoader(2)
        }
    }

    private fun getData() {
        viewModel.getData(args.tvId)
    }

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
            description.text = tv.overview
        }
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
        val photo = viewModel.TVLD.value?.posterPath
        photo?.let {
            loadImage(it, binding.fullPicture)
            binding.fullImageLay.show()
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
}