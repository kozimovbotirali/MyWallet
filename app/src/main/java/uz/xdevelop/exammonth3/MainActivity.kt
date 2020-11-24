package uz.xdevelop.exammonth3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.xdevelop.exammonth3.presentation.ui.fragments.CardFragment
import uz.xdevelop.exammonth3.presentation.ui.fragments.SplashFragment
import uz.xdevelop.exammonth3.utils.navigate

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigate(SplashFragment())
//        navigate(CardFragment())
    }
}