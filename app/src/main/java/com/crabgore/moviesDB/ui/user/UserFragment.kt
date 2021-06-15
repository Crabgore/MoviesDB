package com.crabgore.moviesDB.ui.user

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crabgore.moviesDB.Const
import com.crabgore.moviesDB.Const.Addresses.Companion.GRAVATAR_IMAGES_HOST
import com.crabgore.moviesDB.Const.Addresses.Companion.IMAGES_API_HOST
import com.crabgore.moviesDB.R
import com.crabgore.moviesDB.common.*
import com.crabgore.moviesDB.data.AccountResponse
import com.crabgore.moviesDB.data.Status.*
import com.crabgore.moviesDB.databinding.FragmentUserBinding
import com.crabgore.moviesDB.ui.base.BaseFragment
import com.crabgore.moviesDB.ui.items.MovieItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class UserFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<UserViewModel> { viewModelFactory }

    private val binding get() = _binding!! as FragmentUserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return checkViewState()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isViewWasNull) {
            initUI()
            checkSession()
        }
    }

    private fun initUI() {
        binding.apply {
            loginBtn.setOnClickListener { login() }
            logoutBtn.setOnClickListener { viewModel.logout() }
            favMovieTv.setOnClickListener { toMovieCategory() }
            favTvTv.setOnClickListener { toTvCategory() }
            val register =
                HtmlCompat.fromHtml(getString(R.string.register), HtmlCompat.FROM_HTML_MODE_LEGACY)
            this.register.apply {
                text = register
                movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }

    private fun checkSession() {
        if (viewModel.getSession() == null) binding.topLoginLayout.show()
        else {
            startObservers()
            getData()
        }
    }

    private fun login(userName: String, pass: String) {
        startObservers()
        viewModel.login(userName, pass)
    }

    private fun login() {
        if (binding.loginEt.text != null && binding.loginEt.text.toString() != "" && binding.passEt.text != null && binding.passEt.text.toString() != "") {
            login(binding.loginEt.text.toString(), binding.passEt.text.toString())
        } else showToast(requireContext(), requireContext().getString(R.string.enter_data))
    }

    private fun startObservers() {
        viewModel.apply {
            lifecycleScope.launch {
                accountState.collect { resource ->
                    when (resource.status) {
                        SUCCESS -> resource.data?.let { setInfo(it) }
                        ERROR -> Timber.d(resource.message)
                        LOADING -> showLoader()
                    }
                }
            }

            lifecycleScope.launch {
                favMoviesState.collect { resource ->
                    when (resource.status) {
                        SUCCESS -> resource.data?.let { setRV(binding.favMovieRv, it) }
                        ERROR -> Timber.d(resource.message)
                        LOADING -> {
                        }
                    }
                }
            }

            lifecycleScope.launch {
                favTVState.collect { resource ->
                    when (resource.status) {
                        SUCCESS -> resource.data?.let { setRV(binding.favTvRv, it) }
                        ERROR -> Timber.d(resource.message)
                        LOADING -> {
                        }
                    }
                }
            }

            loggingError.observe(viewLifecycleOwner, { data ->
                data?.let {
                    if (it) {
                        hideLoader()
                        binding.topLoginLayout.show()
                        showToast(requireContext(), requireContext().getString(R.string.wrong_data))
                        loggingError.value = null
                    }
                }
            })

            logoutLD.observe(viewLifecycleOwner, { data ->
                data?.let {
                    if (it) navigate(R.id.moviesFragment)
                }
            })

            sessionIdLD.observe(viewLifecycleOwner, { data ->
                data?.let {
                    binding.topLoginLayout.hide()
                    getData()
                }
            })
        }
    }

    private fun getData() = viewModel.getData()

    private fun setInfo(response: AccountResponse) {
        response.avatar?.tmdb?.avatarPath?.let {
            loadAvatar(IMAGES_API_HOST + it, binding.avatar)
        } ?: loadAvatar(GRAVATAR_IMAGES_HOST + response.avatar?.gravatar?.hash, binding.avatar)
        binding.nickName.text = response.username

        hideLoader()
    }

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
                orientation = LinearLayoutManager.HORIZONTAL
            }
            adapter = fastAdapter
            addDecoration(this, Const.Constants.DECORATION)
        }

        fastAdapter.onClickListener = { view, adapter, item, position ->
            val directions =
                if (recyclerView.id == binding.favMovieRv.id)
                    UserFragmentDirections.actionLoginFragmentToMovieDetailsFragment(item.id)
                else UserFragmentDirections.actionLoginFragmentToTVDetailsFragment(item.id)
            navigateWithAction(directions)
            false
        }
    }

    private fun toMovieCategory() {
        val directions =
            UserFragmentDirections.actionLoginFragmentToMoviesCategoryFragment(binding.favMovieTv.text.toString())
        navigateWithAction(directions)
    }

    private fun toTvCategory() {
        val directions =
            UserFragmentDirections.actionLoginFragmentToTVCategoryFragment(binding.favTvTv.text.toString())
        navigateWithAction(directions)
    }
}