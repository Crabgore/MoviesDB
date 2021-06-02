package com.crabgore.moviesDB.ui.people.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crabgore.moviesDB.Const.Addresses.Companion.IMDB_NAME
import com.crabgore.moviesDB.Const.Constants.Companion.DECORATION
import com.crabgore.moviesDB.R
import com.crabgore.moviesDB.common.*
import com.crabgore.moviesDB.data.PeopleDetailsResponse
import com.crabgore.moviesDB.databinding.FragmentPeopleDetailsBinding
import com.crabgore.moviesDB.ui.base.BaseFragment
import com.crabgore.moviesDB.ui.items.CreditsItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import javax.inject.Inject

class PeopleDetailsFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<PeopleDetailsViewModel> { viewModelFactory }

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
        if (binding.fullImageLay.isVisible) binding.fullImageLay.hide()
        else super.backPressed()
    }

    private fun initUI() {
        binding.profile.setOnClickListener { showFullImage() }
    }

    private fun startObservers() {
        viewModel.apply {
            peopleLD.observe(viewLifecycleOwner, { data ->
                data?.let {
                    setInfo(it)
                }
            })

            movieCastLD.observe(viewLifecycleOwner, { data ->
                data?.let {
                    if (it.isNotEmpty()) setRV(binding.actorRv, it)
                    else binding.actorLayout.hide()
                }
            })

            movieCrewLD.observe(viewLifecycleOwner, { data ->
                data?.let {
                    if (it.isNotEmpty()) setRV(binding.crewRv, it)
                    else binding.crewLayout.hide()
                }
            })

            tvCastLD.observe(viewLifecycleOwner, { data ->
                data?.let {
                    if (it.isNotEmpty()) setRV(binding.tvCastRv, it, true)
                    else binding.tvCastLayout.hide()
                }
            })

            tvCrewLD.observe(viewLifecycleOwner, { data ->
                data?.let {
                    if (it.isNotEmpty()) setRV(binding.tvCrewRv, it, true)
                    else binding.tvCrewLayout.hide()
                }
            })

            observeLoader(3)
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

    private fun getData() {
        viewModel.getData(args.peopleId)
    }

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
            imdb.setOnClickListener {
                goToIMDB(people.imdbID)
            }
        }
    }

    private fun showFullImage() {
        val photo = viewModel.peopleLD.value?.profilePath
        photo?.let {
            loadImage(it, binding.fullPicture)
            binding.fullImageLay.show()
        }
    }

    private fun goToIMDB(url: String?) {
        val browse = Intent(Intent.ACTION_VIEW, Uri.parse(IMDB_NAME + url))
        startActivity(browse)
    }
}