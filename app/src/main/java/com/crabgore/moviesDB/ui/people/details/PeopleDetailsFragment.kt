package com.crabgore.moviesDB.ui.people.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crabgore.moviesDB.Const.Addresses.Companion.IMDB_NAME
import com.crabgore.moviesDB.Const.Constants.Companion.DECORATION
import com.crabgore.moviesDB.R
import com.crabgore.moviesDB.common.*
import com.crabgore.moviesDB.common.Status.*
import com.crabgore.moviesDB.data.people.models.PeopleDetailsResponse
import com.crabgore.moviesDB.databinding.FragmentPeopleDetailsBinding
import com.crabgore.moviesDB.ui.base.BaseFragment
import com.crabgore.moviesDB.ui.items.CreditsItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.android.synthetic.main.full_image_layout.view.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class PeopleDetailsFragment : BaseFragment() {
    val viewModel: PeopleDetailsViewModel by viewModel()

    private val args: PeopleDetailsFragmentArgs by navArgs()
    private val binding get() = _binding!! as FragmentPeopleDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPeopleDetailsBinding.inflate(inflater, container, false)
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
        if (binding.fullImageLay.fullImageLay.isVisible) binding.fullImageLay.fullImageLay.hide()
        else super.backPressed()
    }

    private fun initUI() {
        binding.profile.setOnClickListener { showFullImage() }
    }

    private fun startObservers() {
        viewModel.apply {
            lifecycleScope.launch {
                peopleState.collect { resource ->
                    when (resource.status) {
                        SUCCESS -> resource.data?.let { setInfo(it) }
                        ERROR -> Timber.d(resource.message)
                        LOADING -> showLoader()
                    }
                }
            }

            lifecycleScope.launch {
                movieCastState.collect { resource ->
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
                movieCrewState.collect { resource ->
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

            lifecycleScope.launch {
                tvCastState.collect { resource ->
                    when (resource.status) {
                        SUCCESS -> resource.data?.let {
                            if (it.isNotEmpty()) setRV(binding.tvCastRv, it, true)
                            else binding.tvCastLayout.hide()
                        }
                        ERROR -> Timber.d(resource.message)
                        LOADING -> {
                        }
                    }
                }
            }

            lifecycleScope.launch {
                tvCrewState.collect { resource ->
                    when (resource.status) {
                        SUCCESS -> resource.data?.let {
                            if (it.isNotEmpty()) setRV(binding.tvCrewRv, it, true)
                            else binding.tvCrewLayout.hide()
                        }
                        ERROR -> Timber.d(resource.message)
                        LOADING -> {
                        }
                    }
                }
            }
        }
    }

    private fun setRV(recyclerView: RecyclerView, list: List<CreditsItem>, isTv: Boolean = false) {
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
                if (isTv) PeopleDetailsFragmentDirections.actionPeopleDetailsFragmentToTVDetailsFragment(
                    item.id
                )
                else PeopleDetailsFragmentDirections.actionPeopleDetailsFragmentToMovieDetailsFragment(
                    item.id
                )
            navigateWithAction(directions)
            false
        }
    }

    private fun getData() = viewModel.getData(args.peopleId)

    private fun setInfo(people: PeopleDetailsResponse) {
        binding.apply {
            people.profilePath?.let {
                loadImageWithPlaceHolder(it, profile)
            }
            name.text = people.name
            people.knownForDepartment?.let {
                department.text = it
            }
            gender.text =
                if (people.gender == 1) requireContext().getString(R.string.gender_female)
                else requireContext().getString(R.string.gender_male)
            country.text =
                requireContext().getString(R.string.place_of_birth, people.placeOfBirth)
            people.birthday?.let {
                birthday.text = requireContext().getString(R.string.birthday, formatDate(it))
            }
            people.deathday?.let {
                deathday.text = it
            }
            people.biography?.let {
                if (it != "") biography.text = it
                else biographyCard.hide()
            } ?: biographyCard.hide()
            people.imdbID?.let {
                imdb.show()
                imdb.setOnClickListener { goToIMDB(people.imdbID) }
            }
            people.homepage?.let {
                if (it != "") {
                    homepage.show()
                    homepage.setOnClickListener { goToHomepage(people.homepage) }
                }
            }
        }

        hideLoader()
    }

    private fun showFullImage() {
        val photo = viewModel.peopleState.value.data?.profilePath
        photo?.let {
            loadImage(it, binding.fullImageLay.fullImageLay.full_picture)
            binding.fullImageLay.fullImageLay.show()
        }
    }

    private fun goToIMDB(url: String?) {
        val browse = Intent(Intent.ACTION_VIEW, Uri.parse(IMDB_NAME + url))
        startActivity(browse)
    }

    private fun goToHomepage(url: String?) {
        val browse = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browse)
    }
}