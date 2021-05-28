package com.crabgore.moviesDB.ui.movie.details

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
import com.crabgore.moviesDB.data.MovieDetailsResponse
import com.crabgore.moviesDB.databinding.FragmentMovieDetailsBinding
import com.crabgore.moviesDB.ui.base.BaseFragment
import com.crabgore.moviesDB.ui.items.CreditsItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_movie_details.*
import javax.inject.Inject

class MovieDetailsFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<MovieDetailsViewModel> { viewModelFactory }

    private val args: MovieDetailsFragmentArgs by navArgs()
    private lateinit var picasso: Picasso
    private val binding get() = _binding!! as FragmentMovieDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
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

    private fun initUI() {
        picasso = Picasso.get()
    }

    private fun startObservers() {
        viewModel.movieLD.observe(viewLifecycleOwner, { data ->
            data?.let {
                setInfo(it)
            }
        })

        viewModel.castLD.observe(viewLifecycleOwner, { data ->
            data?.let {
                if (it.isNotEmpty()) setRV(binding.actorRv, it)
                else actor_layout.visibility = GONE
            }
        })

        viewModel.crewLD.observe(viewLifecycleOwner, { data ->
            data?.let {
                if (it.isNotEmpty()) setRV(binding.crewRv, it)
                else crew_layout.visibility = GONE
            }
        })
    }

    private fun getData() {
        viewModel.getData(args.movieId)
    }

    private fun setInfo(movie: MovieDetailsResponse) {
        movie.backdropPath?.let {
            picasso.load(ORIGINAL_IMAGES_API_HOST + it).into(binding.backdrop)
        }
        movie.posterPath?.let {
            picasso.load(IMAGES_API_HOST + it).fit().centerCrop().into(binding.poster)
        }
        binding.title.text = movie.title
        movie.genres?.let {
            val genres = StringBuilder()
            for (i in it.indices) {
                genres.append("${it[i].name}")
                if (i < it.size - 1) genres.append(", ")
            }
            binding.genres.text = genres.toString()
        }
        movie.productionCountries?.let {
            val countries = StringBuilder()
            for (i in it.indices) {
                countries.append("${it[i].name}")
                if (i < it.size - 1) countries.append(", ")
            }
            binding.country.text = requireContext().getString(R.string.country, countries)
        }
        movie.releaseDate?.let {
            binding.year.text = requireContext().getString(R.string.year, formatDate(it))
        }
        binding.duration.text = requireContext().getString(R.string.duration, movie.runtime.toString())
        binding.rating.text = movie.voteAverage.toString()
        binding.description.text = movie.overview
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
                MovieDetailsFragmentDirections.actionMovieDetailsFragmentToPeopleDetailsFragment(
                    item.id
                )
            navigateWithAction(directions)
            false
        }
    }
}