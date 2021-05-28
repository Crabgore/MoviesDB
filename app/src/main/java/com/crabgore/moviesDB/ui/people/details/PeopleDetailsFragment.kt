package com.crabgore.moviesDB.ui.people.details

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
import com.crabgore.moviesDB.Const.Constants.Companion.DECORATION
import com.crabgore.moviesDB.R
import com.crabgore.moviesDB.common.addDecoration
import com.crabgore.moviesDB.common.formatDate
import com.crabgore.moviesDB.data.PeopleDetailsResponse
import com.crabgore.moviesDB.databinding.FragmentPeopleDetailsBinding
import com.crabgore.moviesDB.ui.base.BaseFragment
import com.crabgore.moviesDB.ui.items.CreditsItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.squareup.picasso.Picasso
import javax.inject.Inject

class PeopleDetailsFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<PeopleDetailsViewModel> { viewModelFactory }

    private val args: PeopleDetailsFragmentArgs by navArgs()
    private lateinit var picasso: Picasso
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

    private fun initUI() {
        picasso = Picasso.get()
    }

    private fun startObservers() {
        viewModel.peopleLD.observe(viewLifecycleOwner, { data ->
            data?.let {
                setInfo(it)
            }
        })

        viewModel.movieCastLD.observe(viewLifecycleOwner, { data ->
            data?.let {
                if (it.isNotEmpty()) setRV(binding.actorRv, it)
                else binding.actorLayout.visibility = GONE
            }
        })

        viewModel.movieCrewLD.observe(viewLifecycleOwner, { data ->
            data?.let {
                if (it.isNotEmpty()) setRV(binding.crewRv, it)
                else binding.crewLayout.visibility = GONE
            }
        })

        viewModel.tvCastLD.observe(viewLifecycleOwner, { data ->
            data?.let {
                if (it.isNotEmpty()) setRV(binding.tvCastRv, it, true)
                else binding.tvCastLayout.visibility = GONE
            }
        })

        viewModel.tvCrewLD.observe(viewLifecycleOwner, { data ->
            data?.let {
                if (it.isNotEmpty()) setRV(binding.tvCrewRv, it, true)
                else binding.tvCrewLayout.visibility = GONE
            }
        })

        observeLoader(viewModel, 3)
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
        people.profilePath?.let {
            picasso.load(IMAGES_API_HOST + it).fit().centerCrop().into(binding.profile)
        }
        binding.name.text = people.name
        binding.gender.text =
            if (people.gender == 1) requireContext().getString(R.string.gender_female)
            else requireContext().getString(R.string.gender_male)
        binding.country.text = requireContext().getString(R.string.place_of_birth, people.placeOfBirth)
        people.birthday?.let {
            binding.birthday.text = requireContext().getString(R.string.birthday, formatDate(it))
        }
        people.deathday?.let {
            binding.deathday.text = it
        }
        binding.biography.text = people.biography
    }
}