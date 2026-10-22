package com.example.model

data class HijriDate(
    val day: Int,
    val monthIndex: Int, // 1 to 12
    val monthNameBn: String,
    val monthNameEn: String,
    val year: Int,
    val isEstimated: Boolean = true
) {
    fun formatBn(): String {
        val bnDay = com.example.util.BengaliDateFormatter.toBengaliDigits(day)
        val bnYear = com.example.util.BengaliDateFormatter.toBengaliDigits(year)
        return "$bnDay $monthNameBn $bnYear হিজরি"
    }

    fun formatEn(): String {
        return "$day $monthNameEn $year AH"
    }
}
