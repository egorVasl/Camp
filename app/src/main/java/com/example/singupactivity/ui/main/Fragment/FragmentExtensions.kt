
package com.example.singupactivity.ui.main.Fragment


import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.singupactivity.R


inline val Fragment.act: FragmentActivity
    get() = requireActivity()

inline val Fragment.ctx: FragmentActivity
    get() = requireActivity()
fun FragmentActivity.replaceFragmentInActivity(@IdRes containerViewId: Int, fragment: Fragment,
                                               addToBackStack: Boolean = false, animate: Boolean = false, name: String? = null) {
    supportFragmentManager.transact {
        if (animate) setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        else setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        replace(containerViewId, fragment)
        if (addToBackStack) addToBackStack(name)
    }
}

fun FragmentActivity.replaceFragmentInActivityWithoutAnimation(@IdRes containerViewId: Int, fragment: Fragment,
                                                               addToBackStack: Boolean = false, animate: Boolean = false, name: String? = null) {
    supportFragmentManager.transact {
        replace(containerViewId, fragment)
        if (addToBackStack) addToBackStack(name)
    }
}


fun FragmentActivity.addFragmentToActivity(@IdRes containerViewId: Int, fragment: Fragment) {
    if (!isFragmentInActivity(containerViewId)) {
        supportFragmentManager.transact {
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            add(containerViewId, fragment)
        }
    }
}

fun FragmentActivity.addFragmentToActivityCart(@IdRes containerViewId: Int, fragment: Fragment) {
    supportFragmentManager.transact {
        add(containerViewId, fragment)
    }
}

fun FragmentActivity.addFragmentToActivityInFragment(@IdRes containerViewId: Int, oldFragment: Fragment?, fragment: Fragment,
                                                     addToBackStack: Boolean = false, name: String? = null, animate: Boolean = true) {
    supportFragmentManager.transact {
        if (animate) setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        add(containerViewId, fragment)
        if (addToBackStack) addToBackStack(name)

        if (oldFragment != null) {
            hide(oldFragment)
        }
    }
    hideKeyboard()
}

fun FragmentActivity.addFragmentWithAnimation(@IdRes containerViewId: Int, fragment: Fragment, @AnimatorRes @AnimRes vararg animations: Int,
                                              addToBackStack: Boolean = false, name: String? = null) {
    supportFragmentManager.transact {
        when (animations.size) {
            2 -> setCustomAnimations(animations[0], animations[1])
            4 -> setCustomAnimations(animations[0], animations[1], animations[2], animations[3])
        }
        add(containerViewId, fragment)
        if (addToBackStack) addToBackStack(name)
    }
    hideKeyboard()
}

fun FragmentActivity.addFragmentToActivity(fragment: Fragment, tag: String) {
    supportFragmentManager.transact {
        add(fragment, tag)
    }
}


private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    try {
        beginTransaction().apply {
            action()
        }.commit()
    } catch (e: IllegalStateException) {

    }
}

fun FragmentActivity.isFragmentInActivity(@IdRes containerViewId: Int): Boolean {
    return supportFragmentManager.findFragmentById(containerViewId) != null
}

fun FragmentActivity.getFragmentFromActivity(@IdRes containerViewId: Int): Fragment? {
    return supportFragmentManager.findFragmentById(containerViewId)
}
