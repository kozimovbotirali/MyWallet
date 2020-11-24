package uz.xdevelop.exammonth3.presentation.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import uz.xdevelop.exammonth3.data.repositories.ForgotRepository
import uz.xdevelop.exammonth3.data.source.remote.api.AuthApi
import uz.xdevelop.exammonth3.data.source.remote.retrofit.ApiClient
import uz.xdevelop.exammonth3.presentation.mvp.contracts.ForgotContract
import uz.xdevelop.exammonth3.presentation.mvp.presenters.ForgotPresenter
import uz.xdevelop.exammonth3.R
import uz.xdevelop.exammonth3.databinding.FragmentForgotBinding
import uz.xdevelop.exammonth3.utils.navigate

class ForgotFragment : BaseFragment<FragmentForgotBinding>(), ForgotContract.View {
    override fun getLayoutId(): Int = R.layout.fragment_forgot

    private val apiSimple = ApiClient.retrofitSimple.create(AuthApi::class.java)
    private val repository: ForgotContract.Model by lazy { ForgotRepository(apiSimple) }
    private lateinit var presenter: ForgotContract.Presenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadViews()
    }

    private fun loadViews() {
        binding.apply {
            buttonVerify.setOnClickListener { presenter.clickRestore() }
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

            textPhone.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.toString().isNotEmpty()) {
                        layoutPhone.error = null
                        layoutPhone.isErrorEnabled = false
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            textPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.toString().isNotEmpty()) {
                        layoutPassword1.error = null
                        layoutPassword1.isErrorEnabled = false
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
        presenter = ForgotPresenter(this, repository)
    }

    override fun getCode(): String = binding.textCode.unmaskedText.toString()

    override fun getPhone(): String = binding.textPhone.unmaskedText.toString()

    override fun getPassword(): String = binding.textPassword.text.toString()

    override fun clear() {
        binding.apply {
            textCode.text.toString()
            textPhone.text.toString()
            textPassword.text.toString()
        }
    }

    override fun toBack() {
        navigate(
            LoginFragment(),
            popBackStack = true,
            popUpToInclusive = true,
            popUpTo = LoginFragment::class.java.simpleName
        )
    }

    override fun prepareRestore() {
        binding.apply {
            textCode.visibility = View.VISIBLE
            layoutPassword1.visibility = View.VISIBLE
            layoutPassword.visibility = View.VISIBLE
            buttonVerify.text = resources.getString(R.string.restore)
            textPhone.isEnabled = false
        }
    }

    override fun showProgressDialog() = showProgress()

    override fun hideProgressDialog() = hideProgress()

    override fun showMessage(string: String?) {
//        message(string)
        when {
            string!!.startsWith("Code's") -> {
                binding.layoutPassword.error = string
                binding.textCode.requestFocus()
            }
            string.startsWith("Phone's") -> {
                binding.layoutPhone.error = string
                binding.textPhone.requestFocus()
            }
            string.startsWith("Password's") -> {
                binding.layoutPassword1.error = string
                binding.textPassword.requestFocus()
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

}