package uz.xdevelop.exammonth3.data.models

import java.io.Serializable

data class IntoData(
    var image: Int = 0,
    var header: String = "",
    var info: String = ""
) : Serializable