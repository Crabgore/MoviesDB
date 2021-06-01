package com.crabgore.moviesDB.ui.user

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crabgore.moviesDB.Const
import com.crabgore.moviesDB.Const.Addresses.Companion.GRAVATAR_IMAGES_HOST
import com.crabgore.moviesDB.Const.Addresses.Companion.IMAGES_API_HOST
import com.crabgore.moviesDB.R
import com.crabgore.moviesDB.common.CircleTransform
import com.crabgore.moviesDB.common.addDecoration
import com.crabgore.moviesDB.common.showToast
import com.crabgore.moviesDB.data.AccountResponse
import com.crabgore.moviesDB.databinding.FragmentUserBinding
import com.crabgore.moviesDB.ui.base.BaseFragment
import com.crabgore.moviesDB.ui.items.MovieItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.squareup.picasso.Picasso
import javax.inject.Inject

class UserFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<UserViewModel> { viewModelFactory }

    private val binding get() = _binding!! as FragmentUserBinding
    private lateinit var picasso: Picasso

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
        picasso = Picasso.get()
        binding.loginBtn.setOnClickListener { login() }
        binding.logoutBtn.setOnClickListener { viewModel.logout() }
        binding.favMovieTv.setOnClickListener { toMovieCategory() }
        binding.favTvTv.setOnClickListener { toTvCategory() }
        val register =
            HtmlCompat.fromHtml(getString(R.string.register), HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.register.apply {
            text = register
            movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun checkSession() {
        if (viewModel.getSession() == null) binding.topLoginLayout.visibility = VISIBLE
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
        } else showToast(requireContext(), "DATA")
    }

    private fun startObservers() {
        viewModel.accountLD.observe(viewLifecycleOwner, { data ->
            data?.let {
                setInfo(it)
            }
        })

        viewModel.logoutLD.observe(viewLifecycleOwner, { data ->
            data?.let {
                if (it) navigate(R.id.moviesFragment)
            }
        })

        viewModel.sessionIdLD.observe(viewLifecycleOwner, { data ->
            data?.let {
                binding.topLoginLayout.visibility = GONE
                getData()
            }
        })

        viewModel.favMoviesLD.observe(viewLifecycleOwner, { data ->
            data?.let {
                setRV(binding.favMovieRv, it)
            }
        })

        viewModel.favTVLD.observe(viewLifecycleOwner, { data ->
            data?.let {
                setRV(binding.favTvRv, it)
            }
        })

        observeLoader(viewModel, 3)
    }

    private fun getData() {
        viewModel.getData()
    }

    private fun setInfo(response: AccountResponse) {
        response.avatar?.tmdb?.avatarPath?.let {
            picasso.load(IMAGES_API_HOST + it).fit().centerCrop().transform(CircleTransform())
                .into(binding.avatar)
        } ?: let {
            picasso.load(GRAVATAR_IMAGES_HOST + response.avatar?.gravatar?.hash).fit().centerCrop()
                .transform(CircleTransform()).into(binding.avatar)
        }
        binding.nickName.text = response.username
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
                if (recyclerView == binding.favMovieRv)
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