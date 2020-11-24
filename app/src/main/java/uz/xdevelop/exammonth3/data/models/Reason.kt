package uz.xdevelop.exammonth3.data.models

abstract class Reason : Throwable() {

    abstract val messageRes: Int
}
