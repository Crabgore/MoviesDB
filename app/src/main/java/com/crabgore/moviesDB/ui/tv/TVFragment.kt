package com.crabgore.moviesDB.ui.tv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView
import com.crabgore.moviesDB.Const.Constants.Companion.DECORATION
import com.crabgore.moviesDB.Const.MyPreferences.Companion.SEARCH_TV
import com.crabgore.moviesDB.common.addDecoration
import com.crabgore.moviesDB.common.Status.*
import com.crabgore.moviesDB.databinding.FragmentTVBinding
import com.crabgore.moviesDB.ui.base.BaseFragment
import com.crabgore.moviesDB.ui.items.MovieItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class TVFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<TVViewModel> { viewModelFactory }

    private val binding get() = _binding!! as FragmentTVBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTVBinding.inflate(inflater, container, false)
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
        binding.apply {
            nowPlayingTv.setOnClickListener {
                toFullList(nowPlayingTv.text.toString())
            }
            popularTv.setOnClickListener {
                toFullList(popularTv.text.toString())
            }
            topRatedTv.setOnClickListener {
                toFullList(topRatedTv.text.toString())
            }

            searchEt.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    val directions =
                        TVFragmentDirections.actionTVFragmentToSearchFragment(SEARCH_TV)
                    navigateWithAction(directions)
                }
            }
        }
    }

    private fun startObservers() {
        viewModel.apply {
            lifecycleScope.launch {
                onTheAirState.collect { resource ->
                    when (resource.status) {
                        SUCCESS -> resource.data?.let { setRV(binding.nowPlayingTvRv, it) }
                        ERROR -> Timber.d(resource.message)
                        LOADING -> showLoader()
                    }
                }
            }

            lifecycleScope.launch {
                popularState.collect { resource ->
                    when (resource.status) {
                        SUCCESS -> resource.data?.let { setRV(binding.popularTvRv, it) }
                        ERROR -> Timber.d(resource.message)
                        LOADING -> showLoader()
                    }
                }
            }

            lifecycleScope.launch {
                topRatedState.collect { resource ->
                    when (resource.status) {
                        SUCCESS -> resource.data?.let { setRV(binding.topRatedTvRv, it) }
                        ERROR -> Timber.d(resource.message)
                        LOADING -> showLoader()
                    }
                }
            }
        }
    }

    private fun getData() = viewModel.getData()

    private fun setRV(recyclerView: RecyclerView, moviesList: List<MovieItem>) {
        val itemAdapter = ItemAdapter<MovieItem>()
        val fastAdapter = FastAdapter.with(itemAdapter)
        itemAdapter.add(moviesList)

        recyclerView.apply {
            while (itemDecorationCount > 0) {
                removeItemDecorationAt(0)
            }
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = HORIZONTAL
            }
            adapter = fastAdapter
            addDecoration(this, DECORATION)
        }

        fastAdapter.onClickListener = { view, adapter, item, position ->
            val directions = TVFragmentDirections.actionTVFragmentToTVDetailsFragment(item.id)
            navigateWithAction(directions)
            false
        }

        hideLoader()
    }

    private fun toFullList(command: String) {
        val directions =
            TVFragmentDirections.actionTVFragmentToTVCategoryFragment(command)
        navigateWithAction(directions)
    }
}