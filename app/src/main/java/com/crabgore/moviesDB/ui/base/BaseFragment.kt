package com.crabgore.moviesDB.ui.base

import android.view.View
import androidx.activity.addCallback
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.crabgore.moviesDB.common.popBackStackAllInstances
import dagger.android.support.DaggerFragment

open class BaseFragment : DaggerFragment() {
    var _binding: ViewBinding? = null
    var viewState: View? = null
    var isViewWasNull = false

    private var isNavigated = false

    fun checkViewState(): View? {
        if (viewState == null) {
            viewState = _binding?.root
            isViewWasNull = true
        }
        return viewState
    }

    fun navigateWithAction(action: NavDirections) {
        isNavigated = true
        findNavController().navigate(action)
    }

    fun navigate(resId: Int) {
        isNavigated = true
        findNavController().navigate(resId)
    }

//    fun navigateWithAnimation(resId: Int) {
//        isNavigated = true
//        findNavController().navigate(resId, null, getNavOptions())
//    }

    fun popBack() {
        findNavController().popBackStack()
    }

//    private fun getNavOptions(): NavOptions {
//        val builder = NavOptions.Builder()
//        return builder.setEnterAnim(R.anim.slide_in_left).setExitAnim(R.anim.slide_out_right)
//            .setPopEnterAnim(
//                R.anim.slide_in_left
//            ).setPopExitAnim(R.anim.slide_out_right).build()
//    }

    open fun backPressed() {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        if (!isNavigated)
            requireActivity().onBackPressedDispatcher.addCallback(this) {
                val navController = findNavController()
                if (navController.currentBackStackEntry?.destination?.id != null) {
                    findNavController().popBackStackAllInstances(
                        navController.currentBackStackEntry?.destination?.id!!,
                        true
                    )
                } else
                    navController.popBackStack()
            }
    }
}