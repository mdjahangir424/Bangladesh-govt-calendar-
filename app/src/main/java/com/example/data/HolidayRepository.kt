package com.example.data

import android.content.Context
import com.example.model.Holiday
import com.example.model.HolidayCategory
import org.json.JSONArray
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class HolidayRepository(private val context: Context) {

    private val holidaysCache = mutableMapOf<Int, List<Holiday>>()
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    init {
        preloadBundledHolidays()
    }

    private fun preloadBundledHolidays() {
        val years = listOf(2025, 2026, 2027)
        for (year in years) {
            val list = loadHolidaysFromAsset("holidays_$year.json")
            if (list.isNotEmpty()) {
                holidaysCache[year] = list
            }
        }
    }

    private fun loadHolidaysFromAsset(fileName: String): List<Holiday> {
        return try {
            val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonString)
            val result = mutableListOf<Holiday>()

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val categoryString = obj.optString("category", "GENERAL")
                val category = try {
                    HolidayCategory.valueOf(categoryString)
                } catch (e: Exception) {
                    HolidayCategory.GENERAL
                }

                result.add(
                    Holiday(
                        id = obj.getString("id"),
                        startDate = obj.getString("startDate"),
                        endDate = if (obj.has("endDate") && !obj.isNull("endDate")) obj.getString("endDate") else null,
                        nameBn = obj.getString("nameBn"),
                        nameEn = obj.getString("nameEn"),
                        category = category,
                        descriptionBn = obj.optString("descriptionBn", ""),
                        descriptionEn = obj.optString("descriptionEn", ""),
                        sourceTitle = obj.optString("sourceTitle", "জনপ্রশাসন মন্ত্রণালয় (MOPA)"),
                        sourceUrl = obj.optString("sourceUrl", "https://mopa.gov.bd/pages/public-holiday/"),
                        isMoonDependent = obj.optBoolean("isMoonDependent", false),
                        isVerified = obj.optBoolean("isVerified", true)
                    )
                )
            }
            result.sortedBy { it.startDate }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getAvailableYears(): List<Int> {
        return holidaysCache.keys.sorted()
    }

    fun isYearVerified(year: Int): Boolean {
        val holidays = holidaysCache[year] ?: return false
        if (holidays.isEmpty()) return false
        return holidays.all { it.isVerified }
    }

    fun getHolidaysForYear(year: Int): List<Holiday> {
        return holidaysCache[year] ?: emptyList()
    }

    fun getHolidaysForMonth(year: Int, month: Int): List<Holiday> {
        val yearHolidays = getHolidaysForYear(year)
        val monthPrefix = String.format("%04d-%02d", year, month)
        return yearHolidays.filter { holiday ->
            val startMonth = holiday.startDate.substring(0, 7)
            val endMonth = holiday.endDate?.substring(0, 7)
            startMonth == monthPrefix || endMonth == monthPrefix
        }
    }

    fun getHolidaysForDate(date: LocalDate): List<Holiday> {
        val year = date.year
        val yearHolidays = getHolidaysForYear(year)
        val dateStr = date.format(dateFormatter)

        return yearHolidays.filter { holiday ->
            if (holiday.endDate == null) {
                holiday.startDate == dateStr
            } else {
                try {
                    val start = LocalDate.parse(holiday.startDate, dateFormatter)
                    val end = LocalDate.parse(holiday.endDate, dateFormatter)
                    !date.isBefore(start) && !date.isAfter(end)
                } catch (e: Exception) {
                    holiday.startDate == dateStr
                }
            }
        }
    }

    fun getNextUpcomingHoliday(fromDate: LocalDate): Pair<Holiday, Long>? {
        val currentYearHolidays = getHolidaysForYear(fromDate.year)
        val nextYearHolidays = getHolidaysForYear(fromDate.year + 1)
        val allUpcoming = (currentYearHolidays + nextYearHolidays).filter { holiday ->
            try {
                val start = LocalDate.parse(holiday.startDate, dateFormatter)
                val end = holiday.endDate?.let { LocalDate.parse(it, dateFormatter) } ?: start
                !end.isBefore(fromDate)
            } catch (e: Exception) {
                false
            }
        }.sortedBy { it.startDate }

        val next = allUpcoming.firstOrNull() ?: return null
        val startDate = LocalDate.parse(next.startDate, dateFormatter)
        val days = ChronoUnit.DAYS.between(fromDate, startDate)
        return Pair(next, days)
    }

    fun searchHolidays(query: String, year: Int?): List<Holiday> {
        val pool = if (year != null) {
            getHolidaysForYear(year)
        } else {
            holidaysCache.values.flatten()
        }

        if (query.isBlank()) return pool

        val cleanQuery = query.trim().lowercase()
        return pool.filter { holiday ->
            holiday.nameBn.lowercase().contains(cleanQuery) ||
                    holiday.nameEn.lowercase().contains(cleanQuery) ||
                    holiday.descriptionBn.lowercase().contains(cleanQuery) ||
                    holiday.descriptionEn.lowercase().contains(cleanQuery) ||
                    holiday.startDate.contains(cleanQuery)
        }
    }

    /**
     * Interface for verified remote updates. In absence of a public authorized JSON endpoint,
     * returns an informative failure explaining official gazette requirements.
     */
    suspend fun checkForUpdates(): Result<String> {
        return Result.failure(
            IllegalStateException(
                "No external update server configured. Government holiday updates are verified directly against Ministry of Public Administration (mopa.gov.bd) gazettes and bundled into official app updates."
            )
        )
    }
}
