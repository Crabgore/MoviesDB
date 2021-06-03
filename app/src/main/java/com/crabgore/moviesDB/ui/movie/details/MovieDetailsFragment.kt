package com.crabgore.moviesDB.ui.movie.details

import android.content.Intent
import android.net.Uri
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
import com.crabgore.moviesDB.Const.Addresses.Companion.IMDB_TITLE
import com.crabgore.moviesDB.Const.Constants.Companion.DECORATION
import com.crabgore.moviesDB.R
import com.crabgore.moviesDB.common.*
import com.crabgore.moviesDB.data.MovieDetailsResponse
import com.crabgore.moviesDB.databinding.FragmentMovieDetailsBinding
import com.crabgore.moviesDB.ui.base.BaseFragment
import com.crabgore.moviesDB.ui.items.CreditsItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import javax.inject.Inject

class MovieDetailsFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<MovieDetailsViewModel> { viewModelFactory }

    private val args: MovieDetailsFragmentArgs by navArgs()
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
            movieLD.observe(viewLifecycleOwner, { data ->
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

            observeLoader(1)
        }
    }

    private fun getData() {
        viewModel.getData(args.movieId)
    }

    private fun setInfo(movie: MovieDetailsResponse) {
        binding.apply {
            movie.backdropPath?.let {
                loadImage(it, backdrop)
            }
            movie.posterPath?.let {
                loadImageWithPlaceHolder(it, poster)
            }
            title.text = movie.title
            movie.genres?.let {
                val genres = StringBuilder()
                for (i in it.indices) {
                    genres.append("${it[i].name}")
                    if (i < it.size - 1) genres.append(", ")
                }
                this.genres.text = genres.toString()
            }
            movie.productionCountries?.let {
                val countries = StringBuilder()
                for (i in it.indices) {
                    countries.append("${it[i].name}")
                    if (i < it.size - 1) countries.append(", ")
                }
                country.text = requireContext().getString(R.string.country, countries)
            }
            movie.budget?.let {
                budget.text = requireContext().getString(R.string.budget, it.toString())
            }
            movie.releaseDate?.let {
                year.text = requireContext().getString(R.string.year, formatDate(it))
            }
            movie.runtime?.let {
                duration.text =
                    requireContext().getString(R.string.duration, it.toString())
            }
            rating.text = movie.voteAverage.toString()
            movie.overview?.let {
                if (it != "") description.text = it
                else overviewCard.hide()
            } ?: overviewCard.hide()
            movie.imdbID?.let {
                imdb.show()
                imdb.setOnClickListener { goToIMDB(movie.imdbID) }
            }
            movie.homepage?.let {
                if (it != "") {
                    homepage.show()
                    homepage.setOnClickListener { goToHomepage(movie.homepage) }
                }
            }
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
                MovieDetailsFragmentDirections.actionMovieDetailsFragmentToPeopleDetailsFragment(
                    item.id
                )
            navigateWithAction(directions)
            false
        }
    }

    private fun showFullImage() {
        val photo = viewModel.movieLD.value?.posterPath
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
        else viewModel.markAsFavorite(args.movieId)
    }

    private fun goToIMDB(url: String?) {
        val browse = Intent(Intent.ACTION_VIEW, Uri.parse(IMDB_TITLE + url))
        startActivity(browse)
    }

    private fun goToHomepage(url: String?) {
        val browse = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browse)
    }
}