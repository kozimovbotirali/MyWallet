package uz.xdevelop.exammonth3.utils

import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import uz.xdevelop.exammonth3.R


fun AppCompatActivity.navigate(
    fragment: Fragment,
    addToBackStack: Boolean = false,
    popBackStack: Boolean = false,
    popUpTo: String = "",
    popUpToInclusive: Boolean = false, @IdRes resId: Int = R.id.layoutFragment
) {
    supportFragmentManager.apply {
        val transaction = beginTransaction().replace(resId, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(this@navigate.javaClass.simpleName)
        }
        if (popBackStack) {
            popBackStack(
                popUpTo,
                if (popUpToInclusive) FragmentManager.POP_BACK_STACK_INCLUSIVE else 0
            )
        }
        transaction.commit()
    }
}

/**
 * Method to replace the fragment. The [fragment] is added to the container view with id
 * [containerViewId] and a [tag]. The operation is performed by the supportFragmentManager.
 * @return the fragment added.
 */
fun AppCompatActivity.replaceFragmentSafely(
    fragment: Fragment,
    tag: String = fragment.javaClass.simpleName,
    @IdRes containerViewId: Int = R.id.layoutFragment,
    @AnimRes enterAnimation: Int = 0,
    @AnimRes exitAnimation: Int = 0,
    @AnimRes popEnterAnimation: Int = 0,
    @AnimRes popExitAnimation: Int = 0,
    commitNow: Boolean = false,
    addToBackStack: Boolean = false
): Fragment {
    supportFragmentManager.beginTransaction().apply {
        setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
        replace(containerViewId, fragment)
        let {
            if (addToBackStack) {
                addToBackStack(tag)
            }
            if (supportFragmentManager.isStateSaved) {
                if (commitNow) commitNowAllowingStateLoss() else commitAllowingStateLoss()
            } else {
                if (commitNow) commitNow() else commit()
            }
        }
    }
    return fragment
}

/**
 * Method to add the fragment. The [fragment] is added to the container view with id
 * [containerViewId] and a [tag]. The operation is performed by the supportFragmentManager.
 * @return the fragment added.
 */
fun <T : Fragment> AppCompatActivity.addFragmentSafely(
    fragment: T,
    tag: String = fragment.javaClass.simpleName,
    @IdRes containerViewId: Int = R.id.layoutFragment,
    @AnimRes enterAnimation: Int = 0,
    @AnimRes exitAnimation: Int = 0,
    @AnimRes popEnterAnimation: Int = 0,
    @AnimRes popExitAnimation: Int = 0,
    commitNow: Boolean = false,
    addToBackStack: Boolean = false
): T {
    return (findFragmentByTag(tag) as T?) ?: fragment.also {
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
            add(containerViewId, it, tag)
            if (addToBackStack) {
                addToBackStack(tag)
            }
            if (supportFragmentManager.isStateSaved) {
                if (commitNow) commitNowAllowingStateLoss() else commitAllowingStateLoss()
            } else {
                if (commitNow) commitNow() else commit()
            }
        }
    }
}

/**
 * Method to get fragment by tag. The operation is performed by the supportFragmentManager.
 */
fun AppCompatActivity.findFragmentByTag(tag: String): Fragment? {
    return supportFragmentManager.findFragmentByTag(tag)
}