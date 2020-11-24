@file:Suppress("DEPRECATION")

package uz.xdevelop.exammonth3.presentation.ui.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import uz.xdevelop.exammonth3.R
import uz.xdevelop.exammonth3.data.models.Reason

/**
 * Parent of all fragments.
 *
 * Purpose of [BaseFragment] is to simplify view creation and provide easy access to fragment's
 * [nav Controller] and [binding].
 */
abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    private val progressDialog by lazy {
        ProgressDialog(context)
    }

    //region Abstractions

    @LayoutRes
    abstract fun getLayoutId(): Int
    //endregion

    //region Properties

    protected lateinit var binding: T
    //endregion

    //region Functions

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    protected fun showSnackBarWithAction(reason: Reason, block: () -> Unit) {
        Snackbar.make(
            binding.root,
            resources.getString(reason.messageRes),
            Snackbar.LENGTH_INDEFINITE
        ).setAction(R.string.retry) {
            block()
        }.show()
    }

    fun showProgress() {
        progressDialog.setMessage("Loading...")
        progressDialog.show()
    }

    fun hideProgress() {
        progressDialog.dismiss()
    }

    fun message(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    }
    //endregion
}
