package com.crabgore.moviesDB.ui.people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crabgore.moviesDB.Const.Constants.Companion.DECORATION
import com.crabgore.moviesDB.Const.MyPreferences.Companion.SEARCH_PEOPLE
import com.crabgore.moviesDB.R
import com.crabgore.moviesDB.common.addDecoration
import com.crabgore.moviesDB.common.showToast
import com.crabgore.moviesDB.common.Resource
import com.crabgore.moviesDB.common.Status.*
import com.crabgore.moviesDB.databinding.FragmentPeopleBinding
import com.crabgore.moviesDB.ui.base.BaseFragment
import com.crabgore.moviesDB.ui.items.PeopleItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class PeopleFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<PeopleViewModel> { viewModelFactory }

    private val binding get() = _binding!! as FragmentPeopleBinding
    private lateinit var itemAdapter: ItemAdapter<PeopleItem>
    private var offset = 10
    private var page = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPeopleBinding.inflate(inflater, container, false)
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

    private fun initUI() {
        binding.searchEt.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val directions = PeopleFragmentDirections.actionPeopleFragmentToSearchFragment(
                    SEARCH_PEOPLE
                )
                navigateWithAction(directions)
            }
        }
    }

    private fun startObservers() {
        viewModel.apply {
            lifecycleScope.launch {
                peopleState.collect { resource ->
                    when (resource.status) {
                        SUCCESS -> if (page == 1) {
                            resource.data?.let { setRV(it) }
                            page++
                        } else resource.data?.let { updateRV(it) }
                        ERROR -> Timber.d(resource.message)
                        LOADING -> showLoader()
                    }
                }
            }

            isLastPageLiveData.observe(viewLifecycleOwner, { data ->
                data?.let {
                    if (it) {
                        showToast(
                            requireContext(),
                            requireContext().getString(R.string.full_people)
                        )
                        isLastPageLiveData.value = null
                    }
                }
            })
        }
    }

    private fun getData() = viewModel.getData(page)

    private fun setRV(list: List<PeopleItem>) {
        itemAdapter = ItemAdapter()
        val fastAdapter = FastAdapter.with(itemAdapter)
        itemAdapter.add(list)

        binding.popularPeopleRv.apply {
            val lManager = GridLayoutManager(requireContext(), 3)
            while (itemDecorationCount > 0) {
                removeItemDecorationAt(0)
            }
            setHasFixedSize(true)
            layoutManager = lManager
            adapter = fastAdapter
            addDecoration(this, DECORATION)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val position = lManager.findLastVisibleItemPosition()
                        if (position >= offset) {
                            viewModel.getData(page)
                            offset += 20
                            page++
                        }
                    }
                }
            })

            fastAdapter.onClickListener = { view, adapter, item, position ->
                val directions =
                    PeopleFragmentDirections.actionPeopleFragmentToPeopleDetailsFragment(
                        item.id
                    )
                navigateWithAction(directions)
                viewModel.peopleState.value =
                    Resource(status = SUCCESS, data = null, message = null)
                false
            }
        }

        hideLoader()
    }

    private fun updateRV(list: List<PeopleItem>) {
        itemAdapter.add(list)
        binding.popularPeopleRv.adapter?.notifyDataSetChanged()
    }
}