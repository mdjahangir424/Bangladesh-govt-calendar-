package com.example.model

data class BanglaDate(
    val day: Int,
    val monthIndex: Int, // 1 to 12
    val monthNameBn: String,
    val monthNameEn: String,
    val year: Int,
    val seasonBn: String,
    val seasonEn: String
) {
    fun formatBn(): String {
        val bnDay = com.example.util.BengaliDateFormatter.toBengaliDigits(day)
        val bnYear = com.example.util.BengaliDateFormatter.toBengaliDigits(year)
        return "$bnDay $monthNameBn $bnYear বঙ্গাব্দ"
    }

    fun formatEn(): String {
        return "$day $monthNameEn $year BS"
    }
}
