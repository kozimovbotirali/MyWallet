package uz.xdevelop.exammonth3.presentation.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.xdevelop.exammonth3.R
import uz.xdevelop.exammonth3.data.models.network_models.CardData
import uz.xdevelop.exammonth3.data.models.network_models.ResponseData
import uz.xdevelop.exammonth3.data.models.network_models.VerifyCard
import uz.xdevelop.exammonth3.data.source.remote.api.CardApi
import uz.xdevelop.exammonth3.data.source.remote.retrofit.ApiClient
import uz.xdevelop.exammonth3.databinding.FragmentVerifyCardBinding
import uz.xdevelop.exammonth3.utils.navigate
import uz.xdevelop.exammonth3.utils.network.ResultData
import uz.xdevelop.exammonth3.utils.setReadOnly
import java.util.concurrent.Executors

class VerifyCardFragment : BaseFragment<FragmentVerifyCardBinding>() {
    private val cardApi = ApiClient.retrofit.create(CardApi::class.java)
    private val executor = Executors.newSingleThreadExecutor()
    private val handle = Handler(Looper.getMainLooper())
    private var pan = ""

    override fun getLayoutId(): Int = R.layout.fragment_verify_card

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = requireArguments()
        bundle.getString("pan")?.let { pan = it }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        loadViews()
    }

    private fun loadViews() {
        (activity as AppCompatActivity).title = "Verify Card"
        binding.apply {
            inputPan.setReadOnly(true)
            inputPan.setMaskedText(pan)

            buttonVerify.setOnClickListener {
                if (textCode.unmaskedText.length != 6) {
                    layoutCode.error = "Fill the field!"
                    return@setOnClickListener
                }

                showProgressDialog()
                verifyCard(VerifyCard(pan, textCode.unmaskedText)) { res ->
                    res.onMessage { showMessage(res.getMessageOrNull()) }
                    res.onFailure { onResponseFailure(it) }
                    res.onData {
                        val str = "Successfully verified!"
                        val dialog = AlertDialog.Builder(context)
                            .setTitle("Info!")
                            .setMessage(str)
                            .setPositiveButton("OK") { _, _ ->
                                backToCard()
                            }
                            .create()
                        dialog.show()
                    }
                    hideProgressDialog()
                }
            }

            textCode.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    if (s.toString().isNotEmpty()) {
                        layoutCode.error = null
                        layoutCode.isErrorEnabled = false
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun onResponseFailure(t: Throwable) {
        val dialog = AlertDialog.Builder(context)
            .setTitle("Error!")
            .setIcon(R.drawable.ic_baseline_error_24)
            .setMessage(t.message)
            .setPositiveButton("OK") { _, _ -> }
            .create()
        dialog.show()
    }

    fun showMessage(str: String?) {
        val dialog = AlertDialog.Builder(context)
            .setTitle("Info!")
            .setMessage(str)
            .setPositiveButton("OK") { _, _ -> }
            .create()
        dialog.show()
    }

    fun showProgressDialog() = showProgress()

    fun hideProgressDialog() = hideProgress()

    private fun backToCard() {
        navigate(
            CardFragment(),
            addToBackStack = false,
            popBackStack = true,
            popUpToInclusive = true,
            popUpTo = AddCardFragment::class.java.simpleName
        )
    }

    private fun runOnWorkerThread(f: () -> Unit) {
        executor.execute(f)
    }

    private fun runOnUiThread(f: () -> Unit) {
        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            f()
        } else {
            handle.post { f() }
        }
    }

    private fun verifyCard(ver: VerifyCard, block: (ResultData<CardData>) -> Unit) {
        cardApi.verifyCardNumber(ver).enqueue(object : Callback<ResponseData<CardData>> {
            override fun onFailure(call: Call<ResponseData<CardData>>, t: Throwable) {
                block(ResultData.failure(t))
            }

            override fun onResponse(
                call: Call<ResponseData<CardData>>,
                response: Response<ResponseData<CardData>>
            ) {
                val res = response.body()
                when {
//                    res == null -> block(ResultData.resource(R.string.empty_body))
                    res == null -> block(ResultData.message("Returns empty body. Please connect with call center!"))
                    res.status == "ERROR" -> block(ResultData.message(res.message))
                    res.status == "OK" -> {
                        block(ResultData.data(res.data!!))
                    }
                }
            }
        })
    }
}