
package com.example.singupactivity.ui.main.Fragment


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity



inline val Fragment.act: FragmentActivity
    get() = requireActivity()

inline val Fragment.ctx: FragmentActivity
    get() = requireActivity()
//
//fun Fragment.setBackgroundOnViewGroup(context: Context, viewGroup: ViewGroup) {
//    val bitmap = context.loadBackgroundsFromInternalStorage()
//    val bgDrawable = BitmapDrawable(resources, bitmap.second.bmp)
//    viewGroup.background = bgDrawable
//}
//
//fun BaseFragment.addEvent(title: String) {
//    try {
//        val intent = Intent(Intent.ACTION_INSERT)
//            .setData(CalendarContract.Events.CONTENT_URI)
//            .putExtra(CalendarContract.Events.TITLE, title)
//        startActivityForResult(intent, 0)
//    } catch (e: ActivityNotFoundException) {
//        showSnackBarMessage(R.string.activity_not_found)
//    }
//}
//
//fun Fragment.showContacts(requestCode: Int) {
//    val pickContact = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
////        .apply {
////        type = ContactsContract.Contacts.CONTENT_TYPE
////    }
//
//    if (pickContact.resolveActivity(ctx.packageManager) != null) {
//        startActivityForResult(pickContact, requestCode)
//    } else {
//        ctx.toast(R.string.activity_not_found)
//    }
//}
//
//fun Fragment.givePermissionsInSettingsDialog(
//    message: String = getString(R.string.open_settings_contact_permission),
//    positiveBtnText: String = getString(R.string.open_settings)
//) {
//    this.ctx.showTwoActionDialog(positiveBtnText = positiveBtnText,
//        message = message, positiveBtnFun = {
//            val intent = Intent().apply {
//                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//                val uri = Uri.fromParts(
//                    "package", ctx.packageName,
//                    null
//                )
//                data = uri
//            }
//            startActivity(intent)
//        })
//}
//
//fun ContentResolver.defaultQuery(
//    uri: Uri,
//    projection: List<String>? = null,
//    selection: String? = null,
//    selectionArgs: List<String>? = null,
//    sortOrder: String? = null
//): Cursor? {
//    return query(
//        uri,
//        projection?.toTypedArray(),
//        selection,
//        selectionArgs?.toTypedArray(),
//        sortOrder
//    )
//}
//
//fun Fragment.getContactID(contactUri: Uri): String? {
//    return act.contentResolver.defaultQuery(contactUri, ContactsContract.Contacts._ID.asList())
//        ?.let { cursor ->
//            cursor.use {
//                if (it.moveToFirst()) {
//                    it.getString(0)
//                } else {
//                    null
//                }
//            }
//        }
//}
//
//fun Fragment.getContactPhone(contactID: String): String? {
//    var number: String? = null
//    try {
//        act.contentResolver.defaultQuery(
//            Phone.CONTENT_URI,
//            listOf(Phone.NUMBER),
//            "${Phone.CONTACT_ID} = $contactID"
//        )?.let { cursor ->
//            cursor.use {
//                if (it.moveToFirst()) {
//                    number = it.getString(0).removeSpaces().replace("-", "")
//                }
//            }
//        }
//    } catch (e: Exception) {
//    }
//
//    return number
//}
//
//fun Fragment.getContactPhones(contactID: String): List<Pair<String, String>> {
//    val phones = mutableListOf<Pair<String, String>>()
//
//    try {
//        act.contentResolver.defaultQuery(
//            Phone.CONTENT_URI,
//            listOf(Phone.TYPE, Phone.LABEL, Phone.NUMBER),
//            "${Phone.CONTACT_ID} = $contactID"
//        )?.let { cursor ->
//            cursor.use {
//                if (it.moveToFirst()) {
//                    do {
//                        val type: Int = it.getInt(0)
//                        val label: String = getContactPhoneLabel(type, it.getStringOrNull(1))
//                        val number: String = it.getString(2)
//                        phones += label to number.cropPhoneForErip()
//                    } while (it.moveToNext())
//                }
//            }
//        }
//    } catch (e: Exception) {
//    }
//
//    return phones
//}
//
//fun Fragment.getContactPhoneLabel(type: Int, defaultLabel: String?): String = when (type) {
//    Phone.TYPE_CUSTOM -> defaultLabel ?: ""
//    else -> Phone.getTypeLabel(act.resources, type, "").toString()
//}
//
//val Fragment.locale: Locale
//    get() = ctx.locale
//
//@Suppress("DEPRECATION")
//val Context.locale: Locale
//    @SuppressLint("NewApi")
//    get() = when {
//        Build.VERSION.SDK_INT < 24 -> resources.configuration.locale
//        else -> resources.configuration.locales[0]
//    }
//
//private const val ITEM_CAMERA = 0
//private const val ITEM_NFC = 1
//
//fun Fragment.scanCardType(needFullInfo: Boolean = false, codeAddition: Int = 0) {
//    if (!ctx.isNfcEnabled) {
//        cameraScan(codeAddition)
//    } else {
//        act.selector(
//            getString(R.string.choose_scanning_way),
//            resources.getStringArray(R.array.scanning_way_array).toList()
//        ) { _, i ->
//            when (i) {
//                ITEM_CAMERA -> {
//                    cameraScan(codeAddition)
//                }
//                ITEM_NFC -> {
//                    nfcScan(needFullInfo, codeAddition)
//                }
//            }
//        }
//    }
//}
//
//fun Fragment.showCustomAlertDialog(
//    title: String,
//    message: String,
//    action: (DialogInterface) -> Unit = { }
//) {
//    AlertDialog.Builder(ctx)
//        .setTitle(title)
//        .setMessage(message)
//        .setPositiveButton(getLocalizedString(R.string.continue_text)) { dialogInterface, _ ->
//            action(dialogInterface)
//            dialogInterface.dismiss()
//        }
//        .show()
//}
//
//fun Fragment.cameraScan(codeAddition: Int = 0) {
//    val cameraScanIntent = Intent(ctx, CardIOActivity::class.java)
//    cameraScanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true)
//    cameraScanIntent.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, false)
//    cameraScanIntent.putExtra(
//        CardIOActivity.EXTRA_GUIDE_COLOR,
//        ContextCompat.getColor(ctx, R.color.colorAccent)
//    )
//    cameraScanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true)
//    cameraScanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_CONFIRMATION, true)
//    startActivityForResult(cameraScanIntent, CAMERA_SCAN_REQUEST_CODE + codeAddition)
//}
//
//fun Fragment.nfcScan(needFullInfo: Boolean = false, codeAddition: Int = 0) {
//    val nfcScanIntent = NfcScannerActivity.getIntent(act, needFullInfo)
//    startActivityForResult(nfcScanIntent, NFC_SCAN_REQUEST_CODE + codeAddition)
//}
//
//internal fun Fragment.getLocalizedString(@StringRes resId: Int) = ctx.getLocalizedString(resId)
//
//internal fun Fragment.getLocalizedString(@StringRes resId: Int, vararg formatArgs: Any?) =
//    ctx.getLocalizedString(resId, *formatArgs)
//
//internal fun Fragment.getLocalizedStringArray(@ArrayRes resId: Int): Array<String> =
//    ctx.getLocalizedStringArray(resId)