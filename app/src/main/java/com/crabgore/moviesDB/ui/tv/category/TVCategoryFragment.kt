package com.crabgore.moviesDB.ui.tv.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crabgore.moviesDB.Const.Constants.Companion.DECORATION
import com.crabgore.moviesDB.common.addDecoration
import com.crabgore.moviesDB.common.showToast
import com.crabgore.moviesDB.databinding.FragmentTVCategoryBinding
import com.crabgore.moviesDB.ui.base.BaseFragment
import com.crabgore.moviesDB.ui.items.MovieItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import javax.inject.Inject

class TVCategoryFragment  : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<TVCategoryViewModel> { viewModelFactory }

    private val args: TVCategoryFragmentArgs by navArgs()
    private val binding get() = _binding!! as FragmentTVCategoryBinding
    private lateinit var itemAdapter: ItemAdapter<MovieItem>
    private var offset = 10
    private var page = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTVCategoryBinding.inflate(inflater, container, false)
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
        binding.title.text = args.command
    }

    private fun startObservers() {
        viewModel.moviesLiveData.observe(viewLifecycleOwner, { data ->
            data?.let {
                if (page == 1) {
                    setRV(it)
                    page++
                } else updateRV(it)
            }
        })

        viewModel.isLastPageLiveData.observe(viewLifecycleOwner, { data ->
            data?.let {
                if (it) showToast(requireContext(), "That's all movies we have")
            }
        })

        observeLoader(viewModel, 1)
    }

    private fun getData() {
        viewModel.getData(args.command, page)
    }

    private fun setRV(list: List<MovieItem>) {
        itemAdapter = ItemAdapter()
        val fastAdapter = FastAdapter.with(itemAdapter)
        itemAdapter.add(list)

        binding.tvCategoryRv.apply {
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
                            viewModel.getData(args.command, page)
                            offset += 20
                            page++
                        }
                    }
                }
            })

            fastAdapter.onClickListener = { view, adapter, item, position ->
                val directions =
                    TVCategoryFragmentDirections.actionTVCategoryFragmentToTVDetailsFragment(
                        item.id
                    )
                navigateWithAction(directions)
                viewModel.moviesLiveData.value = null
                false
            }
        }
    }

    private fun updateRV(list: List<MovieItem>) {
        itemAdapter.add(list)
        binding.tvCategoryRv.adapter?.notifyDataSetChanged()
    }
}