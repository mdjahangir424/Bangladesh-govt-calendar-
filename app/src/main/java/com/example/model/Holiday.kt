package com.example.model

enum class HolidayCategory {
    GENERAL,       // সাধারণ ছুটি (General Public Holiday)
    EXECUTIVE,     // নির্বাহী আদেশে সরকারি ছুটি (Executive Order Holiday)
    OBSERVANCE,    // জাতীয় দিবস / স্মরণীয় দিন (National Observance)
    OPTIONAL       // ঐচ্ছিক ছুটি (Optional Holiday)
}

data class Holiday(
    val id: String,
    val startDate: String,       // YYYY-MM-DD
    val endDate: String? = null, // YYYY-MM-DD
    val nameBn: String,
    val nameEn: String,
    val category: HolidayCategory,
    val descriptionBn: String,
    val descriptionEn: String,
    val sourceTitle: String,
    val sourceUrl: String,
    val isMoonDependent: Boolean,
    val isVerified: Boolean
)
