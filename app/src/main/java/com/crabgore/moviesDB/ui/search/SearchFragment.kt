package com.crabgore.moviesDB.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.crabgore.moviesDB.Const.Constants.Companion.DECORATION
import com.crabgore.moviesDB.Const.MyPreferences.Companion.SEARCH_MOVIE
import com.crabgore.moviesDB.Const.MyPreferences.Companion.SEARCH_TV
import com.crabgore.moviesDB.common.addDecoration
import com.crabgore.moviesDB.databinding.FragmentSearchBinding
import com.crabgore.moviesDB.ui.base.BaseFragment
import com.crabgore.moviesDB.ui.items.SearchItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import javax.inject.Inject

class SearchFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<SearchViewModel> { viewModelFactory }

    private val args: SearchFragmentArgs by navArgs()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var thisView: View? = null
    private var isViewWasNull = false
    private lateinit var itemAdapter: ItemAdapter<SearchItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        if (thisView == null) {
            thisView = binding.root
            isViewWasNull = true
        }
        return thisView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startObservers()
        if (isViewWasNull) {
            initUI()
            binding.searchEt.requestFocus()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        binding.searchEt.addTextChangedListener(watcher)
    }

    override fun onPause() {
        super.onPause()
        binding.searchEt.removeTextChangedListener(watcher)
    }

    private fun initUI() {
        initSearchAdapter()
    }

    private fun startObservers() {
        viewModel.searchLD.observe(viewLifecycleOwner, { data ->
            data?.let {
                setSearchRV(it)
            }
        })
    }

    private fun initSearchAdapter() {
        itemAdapter = ItemAdapter()
        val fastAdapter = FastAdapter.with(itemAdapter)

        binding.searchRv.apply {
            while (itemDecorationCount > 0) {
                removeItemDecorationAt(0)
            }
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = fastAdapter
            addDecoration(this, DECORATION)
        }

        fastAdapter.onClickListener = { _, _, item, _ ->
            val directions = when(args.searchId) {
                SEARCH_MOVIE -> SearchFragmentDirections.actionSearchFragmentToMovieDetailsFragment(item.id)
                SEARCH_TV -> SearchFragmentDirections.actionSearchFragmentToTVDetailsFragment(item.id)
                else ->  SearchFragmentDirections.actionSearchFragmentToPeopleDetailsFragment(item.id)
            }
            navigateWithAction(directions)
            false
        }
    }

    private fun setSearchRV(list: List<SearchItem>) {
        itemAdapter.clear()
        itemAdapter.add(list)
        binding.searchRv.adapter?.notifyDataSetChanged()
    }

    private fun search(text: Editable?) {
        viewModel.search(args.searchId, text.toString())
    }

    private val watcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            search(s)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }
}