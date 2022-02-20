//package com.example.singupactivity.ui.main.Fragment
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.graphics.Rect
//import android.graphics.drawable.Drawable
//import android.util.AttributeSet
//import android.view.MotionEvent
//import android.view.View
//import android.view.accessibility.AccessibilityNodeInfo
//import android.view.inputmethod.InputMethodManager
//import android.widget.AdapterView
//import android.widget.ListView
//import androidx.core.content.ContextCompat
//import androidx.core.view.AccessibilityDelegateCompat
//import androidx.core.view.ViewCompat
//import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
//import com.example.singupactivity.R
//import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView
//import java.util.*
//
//
//class MaterialSpinner : MaterialAutoCompleteTextView, AdapterView.OnItemClickListener {
//
//    companion object {
//        private const val MAX_CLICK_DURATION = 200
//    }
//
//    private var startClickTime: Long = 0
//    private var isPopup: Boolean = false
//    var position = ListView.INVALID_POSITION
//        private set
//    private var listener: AdapterView.OnItemSelectedListener? = null
//
//    constructor(context: Context) : super(context) {
//        onItemClickListener = this
//        setColors()
//    }
//
//    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
//        onItemClickListener = this
//        setColors()
//    }
//
//    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
//        onItemClickListener = this
//        setColors()
//    }
//
//    override fun enoughToFilter(): Boolean {
//        return true
//    }
//
//    private fun setColors() {
//        setMetTextColor(ContextCompat.getColor(context, R.color.blackGrayColorDayNight))
//        setBaseColor(ContextCompat.getColor(context, R.color.blackGrayColorDayNight))
//    }
//
//    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
//        super.onFocusChanged(focused, direction, previouslyFocusedRect)
//        disableInput()
//        if (focused && isPopup) {
//            dismissDropDown()
//        } else {
//            isPopup = false
//        }
//    }
//
//    private fun disableInput() {
//        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(windowToken, 0)
//        keyListener = null
//        try {
//            performFiltering("", 0)
//        } catch (e: NullPointerException) {
//            e.printStackTrace()
//        }
//    }
//
//    @SuppressLint("ClickableViewAccessibility")
//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        if (!isEnabled) return false
//
//        when (event.action) {
//            MotionEvent.ACTION_DOWN -> {
//                startClickTime = Calendar.getInstance().timeInMillis
//            }
//            MotionEvent.ACTION_UP -> {
//                try {
//                    val clickDuration = Calendar.getInstance().timeInMillis - startClickTime
//                    if (clickDuration < MAX_CLICK_DURATION) {
//                        if (isPopup) {
//                            dismissDropDown()
//                            isPopup = false
//                        } else {
//                            requestFocus()
//                            isPopup = true
//                            showDropDown()
//                        }
//                    }
//
//                    if (context.isTalkBackEnabled) performClick()
//                } catch (e: NullPointerException) {
//                    e.printStackTrace()
//                }
//
//            }
//        }
//
//        return super.onTouchEvent(event)
//    }
//
//    override fun performClick(): Boolean {
//        super.performClick()
//        try {
//            if (context.isTalkBackEnabled) {
//                if (isPopup) {
//                    dismissDropDown()
//                    isPopup = false
//                } else {
//                    requestFocus()
//                    isPopup = true
//                    showDropDown()
//                }
//            }
//        } catch (e: NullPointerException) {
//            e.printStackTrace()
//        }
//
//        return true
//    }
//
//    override fun onItemClick(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
//        this.position = position
//        isPopup = false
//        if (listener != null) {
//            listener!!.onItemSelected(adapterView, view, position, id)
//        }
//    }
//
//    override fun setOnItemSelectedListener(l: AdapterView.OnItemSelectedListener) {
//        super.setOnItemSelectedListener(l)
//        isPopup = false
//        listener = l
//    }
//
//    override fun setCompoundDrawablesWithIntrinsicBounds(left: Drawable?, top: Drawable?, right: Drawable?, bottom: Drawable?) {
//        val dropDownIcon = right ?: ContextCompat.getDrawable(context, R.drawable.ic_arrow_dropdown)
//        dropDownIcon?.let {
//            super.setCompoundDrawablesWithIntrinsicBounds(left, top, it, bottom)
//        } ?: super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
//    }
//
//    fun setDropDrownIcon(icon: Int) {
//        val dropDownIcon = ContextCompat.getDrawable(context, icon)
//        setCompoundDrawablesWithIntrinsicBounds(null, null, dropDownIcon, null)
//    }
//
//    fun removeIcon() {
//        setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
//    }
//
//    override fun showDropDown() {
//        if (isPopup) {
//            super.showDropDown()
//        }
//    }
//
//    fun setFiltering() {
//        performFiltering("", 0)
//    }
//
//    //TalkBack
//    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo?) {
//        super.onInitializeAccessibilityNodeInfo(info)
//        val hint = this.hint
//        ViewCompat.setAccessibilityDelegate(this, object : AccessibilityDelegateCompat() {
//            override fun onInitializeAccessibilityNodeInfo(
//                host: View,
//                info: AccessibilityNodeInfoCompat
//            ) {
//                super.onInitializeAccessibilityNodeInfo(host, info)
//                if (text.isEmpty()) {
//                    return
//                } else {
//
//                    info!!.text = "$hint ${info.text}"
//                    this@MaterialSpinner.currencyCheck(info, hint = hint.toString())
//                    info.hintText = " "
//                    info.isShowingHintText = false
//                }
//            }
//        })
//    }
//}
