package uz.xdevelop.exammonth3.presentation.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import uz.xdevelop.exammonth3.data.repositories.VerifyRepository
import uz.xdevelop.exammonth3.data.source.remote.api.AuthApi
import uz.xdevelop.exammonth3.data.source.remote.retrofit.ApiClient
import uz.xdevelop.exammonth3.presentation.mvp.contracts.VerifyContract
import uz.xdevelop.exammonth3.presentation.mvp.presenters.VerifyPresenter
import uz.xdevelop.exammonth3.R
import uz.xdevelop.exammonth3.databinding.FragmentVerifyBinding
import uz.xdevelop.exammonth3.utils.navigate

class VerifyFragment : BaseFragment<FragmentVerifyBinding>(), VerifyContract.View {

    private val apiSimple = ApiClient.retrofitSimple.create(AuthApi::class.java)
    private val repository: VerifyContract.Model by lazy { VerifyRepository(apiSimple) }
    private lateinit var presenter: VerifyContract.Presenter
    var pNumber = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadViews()
    }

    private fun loadViews() {

        binding.apply {
            buttonVerify.setOnClickListener { presenter.clickVerify() }
            textCode.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.toString().isNotEmpty()) {
                        layoutPassword.error = null
                        layoutPassword.isErrorEnabled = false
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
        presenter = VerifyPresenter(this, repository)
        binding.textPhone.setMaskedText(pNumber)
    }

    override fun getCode(): String = binding.textCode.unmaskedText.toString()
    override fun getPhone(): String = binding.textPhone.unmaskedText.toString()

    override fun clear() {
        binding.textCode.text?.clear()
    }

    override fun openMain() {
        navigate(
            CardFragment(),
            popBackStack = true,
            popUpToInclusive = true,
            popUpTo = LoginFragment::class.java.simpleName//sh emas
        )
    }

    override fun showProgressDialog() = showProgress()

    override fun hideProgressDialog() = hideProgress()

    override fun showMessage(string: String?) {
//        message(string)
        when {
            string!!.contains("Code's") -> {
                binding.layoutPassword.error = string
                binding.textCode.requestFocus()
            }
            else -> {
                val dialog = AlertDialog.Builder(context)
                    .setTitle("Info")
                    .setMessage(string)
                    .setPositiveButton("OK") { _, _ -> }
                    .create()
                dialog.show()
            }
        }
    }

    override fun showErrorMessage(string: String?) {
//        message(string)
        val dialog = AlertDialog.Builder(context)
            .setTitle("Error")
            .setIcon(R.drawable.ic_baseline_error_24)
            .setMessage(string)
            .setPositiveButton("OK") { _, _ -> }
            .create()
        dialog.show()
    }

    override fun getLayoutId(): Int = R.layout.fragment_verify
}