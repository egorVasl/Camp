package com.example.singupactivity.ui.main.Fragment

import android.content.Context
import android.content.Context.ACCESSIBILITY_SERVICE
import android.content.res.Configuration
import android.os.Build
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.util.Log
import android.view.View
import android.view.accessibility.AccessibilityManager
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.fragment.app.Fragment
import by.belinvestbank.R
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.setAccessibilityInfo(action: (AccessibilityNodeInfoCompat) -> Unit = { }) {
    setTextInputAccessibilityDelegate(object : TextInputLayout.AccessibilityDelegate(this) {
        override fun onInitializeAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(host, info)

            if (info.isPassword) {
                info.isShowingHintText = true
            } else {
                info.roleDescription = " "
            }

            if (text.isEmpty()) currencyCheck(info)
            else if(currencyCheck(info, text))
            else if (!currencyCheck(info)) {
                info.text = "${info.hintText} ${info.text.toString().setNumberDescription()} "
            }

            action(info)
            info.hintText = " "
            info.isShowingHintText = false
        }
    })
}

fun View.currencyCheck(info: AccessibilityNodeInfoCompat, sum: String = "", hint: String = ""): Boolean {
    val rub = context.getString(R.string.rub)
    val byn = context.getString(R.string.byn)
    val usd = context.getString(R.string.usd)
    val eur = context.getString(R.string.eur)

    val talkHint = info.hintText ?: hint

    when {
        info.hintText?.contains(rub) == true || info.text.contains(rub) -> {
            info.text = "${talkHint.toString().replace(rub, "")} ${context.getBalanceText(sum, rub)}"
            return true
        }

        info.hintText?.contains(byn) == true || info.text.contains(byn) -> {
            info.text = "${talkHint.toString().replace(byn, "")} ${context.getBalanceText(sum, byn)}"
            return true
        }

        info.hintText?.contains(usd) == true || info.text.contains(usd) -> {
            info.text = "${talkHint.toString().replace(usd, "")} ${context.getBalanceText(sum, usd)}"
            return true
        }

        info.hintText?.contains(eur) == true || info.text.contains(eur) -> {
            info.text = "${talkHint.toString().replace(eur, "")} ${context.getBalanceText(sum, eur)}"
            return true
        }
        else -> return false
    }
}

fun String.currencyCheck(context: Context): Boolean{
    val currencies = context.resources.getStringArray(R.array.pfm_currencies)
    return currencies.any { this.contains(it) }
}

fun TextInputLayout.setCardNumberDescription() {
    setTextInputAccessibilityDelegate(object : TextInputLayout.AccessibilityDelegate(this) {
        override fun onInitializeAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(host, info)
            var description = text.toList().joinToString("") { if (it.isDigit()) "$it " else it.toString() }
            description = description.replace(Regex("\\*\\*\\*\\*"), context.getString(R.string.four_last_card_digits))
            info.text = info.hintText.toString() + description
            info.hintText = " "
        }
    })
}

fun View.setAccessibilityInfo(newInfo: String = "", action: (AccessibilityNodeInfoCompat) -> Unit = { }) {
    ViewCompat.setAccessibilityDelegate(this, object : AccessibilityDelegateCompat() {
        override fun onInitializeAccessibilityNodeInfo(host: View?, info: AccessibilityNodeInfoCompat?) {
            super.onInitializeAccessibilityNodeInfo(host, info)
            info!!.contentDescription = newInfo
            action(info)
        }
    })
}

fun View.setAccessibilityText(newInfo: String) {
    ViewCompat.setAccessibilityDelegate(this, object : AccessibilityDelegateCompat() {
        override fun onInitializeAccessibilityNodeInfo(host: View?, info: AccessibilityNodeInfoCompat?) {
            super.onInitializeAccessibilityNodeInfo(host, info)
            info!!.text = newInfo
        }
    })
}

fun View.setAdditionalAccessibilityInfo(additionalText: String) {
    ViewCompat.setAccessibilityDelegate(this, object : AccessibilityDelegateCompat() {
        override fun onInitializeAccessibilityNodeInfo(host: View?, info: AccessibilityNodeInfoCompat?) {
            super.onInitializeAccessibilityNodeInfo(host, info)
            info!!.apply {
                contentDescription = "$contentDescription $additionalText"
            }
        }
    })
}

fun TextView.setAdditionalAccessibilityInfo(additionalText: String) {
    ViewCompat.setAccessibilityDelegate(this, object : AccessibilityDelegateCompat() {
        override fun onInitializeAccessibilityNodeInfo(host: View?, info: AccessibilityNodeInfoCompat?) {
            super.onInitializeAccessibilityNodeInfo(host, info)
            info!!.apply {
                text = "$text $additionalText"
            }
        }
    })
}

fun SwitchCompat.setSwitchContentDesc(isChecked: Boolean) {
    if (isChecked) {
        setAccessibilityText("$text ${context.getString(R.string.talkBack_switch_on)}")
//            switchView.contentDescription = getString(R.string.talkBack_switch_on)
    } else {
        setAccessibilityText("$text ${context.getString(R.string.talkBack_switch_off)}")
//            switchView.contentDescription = getString(R.string.talkBack_switch_off)
    }
}

fun View.setAccessibilityButtonInfo() = setAdditionalAccessibilityInfo(resources.getString(R.string.button_additional_description))
fun TextView.setAccessibilityButtonInfo() = setAdditionalAccessibilityInfo(resources.getString(R.string.button_additional_description))

fun TextView.setAccessibilityHeaderInfo() = setAdditionalAccessibilityInfo(resources.getString(R.string.header_additional_description))

fun Toolbar.setNavigationAccessibilityInfo(newInfo: String) = navigationIconView?.setAccessibilityInfo(newInfo)

val Context.isTalkBackEnabled: Boolean
    get() = (getSystemService(ACCESSIBILITY_SERVICE) as AccessibilityManager).isTouchExplorationEnabled

val Fragment.isTalkBackEnabled: Boolean
    get() = act.isTalkBackEnabled

fun TextView.setCardNumberDescription() {
    var description = text.toList().joinToString("") { if (it.isDigit()) "$it " else it.toString() }
    description = description.replace(Regex("\\*\\*\\*\\*"), context.getString(R.string.four_last_card_digits))
    contentDescription = description
}

fun TextView.setNumberDescription(): String {
    val numberDescription =  this.text.toList().joinToString("") {
        when {
            it.isDigit() -> "$it "
            else -> it.toString()
        }
    }
    this.contentDescription = numberDescription
    return numberDescription
}


fun String.setCardNumberDescription(context: Context): String {
    var description = this.toList().joinToString("") { if (it.isDigit()) "$it " else it.toString() }
    description = description.replace(Regex("\\*\\*\\*\\*"), context.getString(R.string.four_last_card_digits))
    return description
}

fun Context.currencyTalkback(currency: String): String {
    return when (currency) {
        getString(R.string.byn) -> getString(R.string.currency) + getString(R.string.currency_byn)
        getString(R.string.usd) -> getString(R.string.currency) + getString(R.string.currency_usd)
        getString(R.string.eur) -> getString(R.string.currency) + getString(R.string.currency_eur)
        getString(R.string.rub) -> getString(R.string.currency) + getString(R.string.currency_rub)
        else -> ""
    }
}

fun String.setNumberDescription(): String =
        this.toList().joinToString("") {
            when {
                it.isDigit() -> "$it "
                else -> it.toString()
            }
        }

fun String.setAllSymbolsSeparately(context: Context): String =
        this.toList().joinToString("") {
            when(it.toString()){
                "-" -> context.getString(R.string.hyphen)
                "*" -> context.getString(R.string.asterisk)
                "k" -> ", k"
                else -> "$it "
            }
        }

fun TextView.setDescriptionWithDigitsAndAsterisks(separateAll: Boolean = false) {
    var description = text
    description = if (separateAll) {
        description.toList().joinToString(", ")
    } else {
        description.toList().joinToString("") { if (it.isDigit()) "$it " else it.toString() }
    }
    description = description.replace(Regex("\\*"), "${context.getString(R.string.asterisk)} ")
    contentDescription = description
}

val Context.isInversionModeEnabled: Boolean
    get() = if (Build.VERSION.SDK_INT >= 21) {
        val accessibilityEnabled: Int = try {
            Settings.Secure.getInt(contentResolver,
                    Settings.Secure.ACCESSIBILITY_DISPLAY_INVERSION_ENABLED)
        } catch (e: SettingNotFoundException) {
            Log.d(getString(R.string.app_name), "Error finding setting ACCESSIBILITY_DISPLAY_INVERSION_ENABLED: " + e.message)
            Log.d(getString(R.string.app_name), "Checking negative color enabled status")
            val semHighContrast = "high_contrast"
            Settings.System.getInt(contentResolver, semHighContrast, 0)
        }

        accessibilityEnabled == 1
    } else false

val Fragment.isInversionModeEnabled: Boolean
    get() = act.isInversionModeEnabled

val Context.isNightModeEnabled: Boolean
    get() = resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

val Fragment.isNightModeEnabled: Boolean
    get() = ctx.isNightModeEnabled