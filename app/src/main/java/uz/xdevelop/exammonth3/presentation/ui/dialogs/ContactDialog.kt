package uz.xdevelop.exammonth3.presentation.ui.dialogs

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.android.synthetic.main.dialog_contact.view.*
import uz.xdevelop.exammonth3.data.source.local.room.entities.ContactData
import uz.xdevelop.exammonth3.utils.SingleBlock
import uz.xdevelop.exammonth3.R
import uz.xdevelop.exammonth3.app.MyApp

class ContactDialog(context: Context, actionName: String) : AlertDialog(context) {
    @SuppressLint("InflateParams")
    private val contentView =
        LayoutInflater.from(context).inflate(R.layout.dialog_contact, null, false)
    private var listener: SingleBlock<ContactData>? = null
    private var contactData: ContactData? = null
    private var maskFilled: Boolean = false
    private var phone: String = ""

    init {
        setView(contentView)
        setButton(BUTTON_POSITIVE, actionName) { _, _ ->
            if (contentView.inputFirstName.text.toString().isNotEmpty() &&
                contentView.inputLastName.text.toString().isNotEmpty() &&
                contentView.inputNumber.text.toString().isNotEmpty()
            ) {
                if (maskFilled) {
                    val data = contactData ?: ContactData()
                    data.firstName = contentView.inputFirstName.text.toString()
                    data.lastName = contentView.inputLastName.text.toString()
                    data.phoneNumber = phone
                    listener?.invoke(data)
                } else {
                    Toast.makeText(
                        MyApp.instance,
                        "Please enter correct number!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } else {
                Toast.makeText(MyApp.instance, "Fill all fields!!!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        setButton(BUTTON_NEGATIVE, "Cancel") { _, _ -> dismiss() }

        val listener = MaskedTextChangedListener(
            "{+998} [00] [000] [00] [00]",
            contentView.inputNumber,
            object : MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(
                    maskFilled: Boolean,
                    extractedValue: String,
                    formattedValue: String
                ) {
                    this@ContactDialog.maskFilled = maskFilled
                    if (maskFilled) {
                        phone = extractedValue
                    }
                }
            })

        contentView.inputNumber.apply {
            addTextChangedListener(listener)
            onFocusChangeListener = listener
        }
    }

    fun setContactData(contactData: ContactData) = with(contentView) {
        this@ContactDialog.contactData = contactData
        inputFirstName.setText(contactData.firstName)
        inputLastName.setText(contactData.lastName)
        inputNumber.setText(contactData.phoneNumber)
    }

    fun setOnClickListener(block: SingleBlock<ContactData>) {
        listener = block
    }
}