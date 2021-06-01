package com.crabgore.moviesDB.common

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.crabgore.moviesDB.Const.Addresses.Companion.IMAGES_API_HOST
import com.crabgore.moviesDB.Const.Addresses.Companion.ORIGINAL_IMAGES_API_HOST
import com.crabgore.moviesDB.R
import com.crabgore.moviesDB.data.Cast
import com.crabgore.moviesDB.data.MovieCast
import com.crabgore.moviesDB.data.TVCast
import com.crabgore.moviesDB.ui.items.CreditsItem
import com.squareup.picasso.Picasso
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * показываем короткий Тост
 */
fun showToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

/**
 * приводим ошибку ответа сервера к читаемому формату
 */
fun parseError(throwable: Throwable?): String? {
    return (throwable as HttpException).response()?.errorBody()?.string()?.replace("{", "")
        ?.replace("}", "")?.replace("\"", "")?.replace(":", ": ")
}

/**
 * добавляем расстояние между элементами в RecyclerView
 */
fun addDecoration(recyclerView: RecyclerView, spacing: Int) {
    recyclerView.setPadding(spacing, spacing, spacing, spacing)
    recyclerView.clipToPadding = false
    recyclerView.clipChildren = false
    recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect, itemPosition: Int, parent: RecyclerView
        ) {
            outRect.set(spacing, spacing, spacing, spacing)
        }
    })
}

/**
 * Загрузка картинок используя Picasso
 */
fun loadImage(url: String, imageView: ImageView) {
    Picasso.get().load(ORIGINAL_IMAGES_API_HOST + url).noPlaceholder().into(imageView)
}

fun loadImageWithPlaceHolder(url: String, imageView: ImageView) {
    Picasso.get().load(IMAGES_API_HOST + url).placeholder(R.drawable.ic_no_photo_night).fit()
        .centerCrop().into(imageView)
}

fun loadAvatar(url: String, imageView: ImageView) {
    Picasso.get().load(url).fit().centerCrop().transform(CircleTransform())
        .into(imageView)
}

/**
 * Скрываем/показываем view
 */
fun View.show() {
    visibility = VISIBLE
}

fun View.hide() {
    visibility = GONE
}

/**
 * Форматирование дат для старых/новых апи
 */
fun formatDate(string: String): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
        if (string != "") formatter.format(LocalDate.parse(string)) else ""
    } else {
        val stringToDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateToStringFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        if (string != "") dateToStringFormat.format(stringToDateFormat.parse(string)) else ""
    }
}

/**
 * проверка на содержание
 */
fun Cast.isContains(items: List<CreditsItem>): Boolean {
    var result = false

    for (i in items.indices) {
        if (items[i].id == this.id) {
            items[i].job += ", ${this.job}"
            result = true
            break
        }
    }

    return result
}

fun MovieCast.isContains(items: List<CreditsItem>): Boolean {
    var result = false

    for (i in items.indices) {
        if (items[i].id == this.id) {
            items[i].job += ", ${this.job}"
            result = true
            break
        }
    }

    return result
}

fun TVCast.isContains(items: List<CreditsItem>): Boolean {
    var result = false

    for (i in items.indices) {
        if (items[i].id == this.id) {
            items[i].job += ", ${this.job}"
            result = true
            break
        }
    }

    return result
}

//Hiding keyboard
fun Fragment.hideKeyboard() = view?.let { activity?.hideKeyboard(it) }

fun Activity.hideKeyboard() = hideKeyboard(currentFocus ?: View(this))

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}