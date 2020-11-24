package uz.xdevelop.exammonth3.data.models.network_models

data class ResponseData<T>(
    val status: String,
    val message: String = "Successful",
    val data: T? = null
)