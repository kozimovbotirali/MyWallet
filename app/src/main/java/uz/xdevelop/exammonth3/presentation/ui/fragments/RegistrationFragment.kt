package uz.xdevelop.exammonth3.presentation.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import uz.xdevelop.exammonth3.data.repositories.RegisterRepository
import uz.xdevelop.exammonth3.data.source.remote.api.AuthApi
import uz.xdevelop.exammonth3.data.source.remote.retrofit.ApiClient
import uz.xdevelop.exammonth3.presentation.mvp.contracts.RegisterContract
import uz.xdevelop.exammonth3.presentation.mvp.presenters.RegisterPresenter
import uz.xdevelop.exammonth3.R
import uz.xdevelop.exammonth3.databinding.FragmentRegistrationBinding
import uz.xdevelop.exammonth3.utils.navigate

class RegistrationFragment : BaseFragment<FragmentRegistrationBinding>(), RegisterContract.View {
    private val apiSimple = ApiClient.retrofitSimple.create(AuthApi::class.java)
    private val repository: RegisterContract.Model by lazy { RegisterRepository(apiSimple) }
    private lateinit var presenter: RegisterContract.Presenter

    override fun getLayoutId(): Int = R.layout.fragment_registration

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadViews()
    }

    private fun loadViews() {
        binding.apply {
            btnLogin.setOnClickListener { presenter.clickLogin() }
            buttonRegister.setOnClickListener { presenter.clickRegister() }
            textLastName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.toString().isNotEmpty()) {
                        layoutLastName.error = null
                        layoutLastName.isErrorEnabled = false
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            textFirstName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.toString().isNotEmpty()) {
                        layoutFirstName.error = null
                        layoutFirstName.isErrorEnabled = false
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
        }
        presenter = RegisterPresenter(this, repository)
    }

    override fun getPassword1(): String = binding.textPassword2.text.toString()

    override fun getPassword2(): String = binding.textPassword1.text.toString()

    override fun getPhone(): String = binding.textPhone.unmaskedText.toString()

    override fun getName(): String = binding.textFirstName.text.toString()

    override fun getSurname(): String = binding.textLastName.text.toString()

    override fun clear() {
        binding.apply {
            textFirstName.text?.clear()
            textLastName.text?.clear()
            textPhone.text?.clear()
            textPassword1.text?.clear()
            textPassword2.text?.clear()
        }
    }

    override fun openLogin() {
        navigate(
            LoginFragment(),
            popBackStack = true,
            popUpToInclusive = true,
            popUpTo = LoginFragment::class.java.simpleName
        )
    }

    override fun openVerify() {
        val fr = VerifyFragment()
        fr.pNumber = getPhone()
        navigate(
            fr,
            addToBackStack = true
        )
    }

    override fun showProgressDialog() = showProgress()

    override fun hideProgressDialog() = hideProgress()

    override fun showMessage(string: String?) {
//        message(string)
        when {
            string!!.startsWith("Phone") -> {
                binding.layoutPhone.error = string
                binding.textPhone.requestFocus()
            }
            string.startsWith("First") -> {
                binding.layoutFirstName.error = string
                binding.textFirstName.requestFocus()
            }
            string.startsWith("Last") -> {
                binding.layoutLastName.error = string
                binding.textLastName.requestFocus()
            }
            else -> {
                val dialog = AlertDialog.Builder(context)
                    .setTitle("Info!")
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
            .setTitle("Error!")
            .setIcon(R.drawable.ic_baseline_error_24)
            .setMessage(string)
            .setPositiveButton("OK") { _, _ -> }
            .create()
        dialog.show()
    }
}