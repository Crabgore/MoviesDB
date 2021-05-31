package com.crabgore.moviesDB.ui.tv.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crabgore.moviesDB.Const.Addresses.Companion.IMAGES_API_HOST
import com.crabgore.moviesDB.Const.Addresses.Companion.ORIGINAL_IMAGES_API_HOST
import com.crabgore.moviesDB.Const.Constants.Companion.DECORATION
import com.crabgore.moviesDB.R
import com.crabgore.moviesDB.common.addDecoration
import com.crabgore.moviesDB.common.formatDate
import com.crabgore.moviesDB.data.TVDetailsResponse
import com.crabgore.moviesDB.databinding.FragmentTVDetailsBinding
import com.crabgore.moviesDB.ui.base.BaseFragment
import com.crabgore.moviesDB.ui.items.CreditsItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.squareup.picasso.Picasso
import javax.inject.Inject

class TVDetailsFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<TVDetailsViewModel> { viewModelFactory }

    private val args: TVDetailsFragmentArgs by navArgs()
    private lateinit var picasso: Picasso
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
        if (binding.fullImageLay.visibility == View.VISIBLE) binding.fullImageLay.visibility = GONE
        else popBack()
    }

    private fun initUI() {
        picasso = Picasso.get()
        binding.poster.setOnClickListener { showFullImage() }
    }

    private fun startObservers() {
        viewModel.TVLD.observe(viewLifecycleOwner, { data ->
            data?.let {
                setInfo(it)
            }
        })

        viewModel.castLD.observe(viewLifecycleOwner, { data ->
            data?.let {
                if (it.isNotEmpty()) setRV(binding.actorRv, it)
                else binding.actorLayout.visibility = GONE
            }
        })

        viewModel.crewLD.observe(viewLifecycleOwner, { data ->
            data?.let {
                if (it.isNotEmpty()) setRV(binding.crewRv, it)
                else binding.crewLayout.visibility = GONE
            }
        })

        observeLoader(viewModel, 2)
    }

    private fun getData() {
        viewModel.getData(args.tvId)
    }

    private fun setInfo(tv: TVDetailsResponse) {
        tv.backdropPath?.let {
            picasso.load(ORIGINAL_IMAGES_API_HOST + it).into(binding.backdrop)
        }
        tv.posterPath?.let {
            picasso.load(IMAGES_API_HOST + it).fit().centerCrop().into(binding.poster)
        }
        binding.title.text = tv.name
        tv.genres?.let {
            val genres = StringBuilder()
            for (i in it.indices) {
                genres.append("${it[i].name}")
                if (i < it.size - 1) genres.append(", ")
            }
            binding.genres.text = genres.toString()
        }
        binding.seasons.text =
            requireContext().getString(R.string.season_count, tv.numberOfSeasons.toString())
        binding.episodes.text =
            requireContext().getString(R.string.episodes_count, tv.numberOfEpisodes.toString())
        tv.productionCountries?.let {
            val countries = StringBuilder()
            for (i in it.indices) {
                countries.append("${it[i].name}")
                if (i < it.size - 1) countries.append(", ")
            }
            binding.country.text = requireContext().getString(R.string.country, countries)
        }
        tv.firstAirDate?.let {
            binding.year.text = requireContext().getString(R.string.year, formatDate(it))
        }
        tv.episodeRunTime?.let {
            if (it.isNotEmpty()) binding.duration.text = requireContext().getString(
                R.string.episode_duration,
                it[0].toString()
            )
        }
        binding.rating.text = tv.voteAverage.toString()
        binding.description.text = tv.overview
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
            picasso.load(ORIGINAL_IMAGES_API_HOST + it).into(binding.fullPicture)
            binding.fullImageLay.visibility = View.VISIBLE
        }
    }
}