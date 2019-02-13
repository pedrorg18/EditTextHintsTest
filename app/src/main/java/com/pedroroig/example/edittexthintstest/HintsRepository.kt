package com.pedroroig.example.edittexthintstest

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log


class HintsRepository(private val ctx: Context) {

    private val tag = "HINTS-ADAPTER"

    private val MAXIMUM_NUMBER_OF_HINTS = 3
    private val STRING_DELIMITER = "|"

    private val sharedPref: SharedPreferences by lazy {
        ctx.getSharedPreferences(
            ctx.getString(R.string.hints_preference_file_key), Context.MODE_PRIVATE
        )
    }

    fun getHints(): MutableList<String> {
        val hintsString = sharedPref.getString(ctx.getString(R.string.hints_key), "")!!
        var hints: List<String> = ArrayList()
        if (!hintsString.isEmpty()) {
            hints = hintsString.split(STRING_DELIMITER)
        }
        Log.i(tag, "getHints - list: ${hints.toMutableList()}")
        return hints.toMutableList()
    }

    fun storeHint(hint: String) {
        Log.i(tag, "storeHint - hint: $hint")
        val hintsList = getHints()
        if(!hintsList.contains(hint)) {
            if (hintsList.size == MAXIMUM_NUMBER_OF_HINTS)
                hintsList.removeAt(0)
            hintsList.add(hint)
        } else {
            // If the hint exists, just move it to the last position
            hintsList.remove(hint)
            hintsList.add(hint)
        }
        storeHintList(hintsList)
    }

    private fun storeHintList(hintsList: MutableList<String>) {
        Log.i(tag, "storeHintList - List: $hintsList")
        with(sharedPref.edit()) {
            val hintsString = TextUtils.join(STRING_DELIMITER, hintsList)
            putString(ctx.getString(R.string.hints_key), hintsString)
            apply()
        }
    }

    fun deleteHint(hint: String) {
        Log.i(tag, "deleteHint - hint: $hint")
        val hintsList = getHints()
        if(hintsList.contains(hint)) {
            hintsList.remove(hint)
            storeHintList(hintsList)
        } else {
            throw IllegalArgumentException("The received hint does not exist: $hint")
        }
    }
}