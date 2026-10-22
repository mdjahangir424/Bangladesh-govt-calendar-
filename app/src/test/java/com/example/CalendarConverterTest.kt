package com.example

import com.example.calendar.BengaliCalendarConverter
import com.example.calendar.HijriCalendarConverter
import com.example.util.BengaliDateFormatter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class CalendarConverterTest {

    @Test
    fun testPahelaBaishakhConversion() {
        // In Bangladesh, April 14 is always 1st of Boishakh
        val april14 = LocalDate.of(2026, 4, 14)
        val banglaDate = BengaliCalendarConverter.convert(april14)

        assertEquals(1, banglaDate.day)
        assertEquals(1, banglaDate.monthIndex)
        assertEquals("বৈশাখ", banglaDate.monthNameBn)
        assertEquals("Boishakh", banglaDate.monthNameEn)
        assertEquals(1433, banglaDate.year)
        assertEquals("গ্রীষ্ম", banglaDate.seasonBn)
    }

    @Test
    fun testDayBeforePahelaBaishakh() {
        // April 13 is the last day of Choitro (Choitro 30) of the previous Bengali year
        val april13 = LocalDate.of(2026, 4, 13)
        val banglaDate = BengaliCalendarConverter.convert(april13)

        assertEquals(30, banglaDate.day)
        assertEquals(12, banglaDate.monthIndex)
        assertEquals("চৈত্র", banglaDate.monthNameBn)
        assertEquals(1432, banglaDate.year)
    }

    @Test
    fun testVictoryDayConversion() {
        // 16 December in Bangladesh is 1st of Poush
        val dec16 = LocalDate.of(2026, 12, 16)
        val banglaDate = BengaliCalendarConverter.convert(dec16)

        assertEquals(1, banglaDate.day)
        assertEquals(9, banglaDate.monthIndex)
        assertEquals("পৌষ", banglaDate.monthNameBn)
        assertEquals("Poush", banglaDate.monthNameEn)
        assertEquals(1433, banglaDate.year)
        assertEquals("শীত", banglaDate.seasonBn)
    }

    @Test
    fun testLanguageMovementDay() {
        // 21 February in standard year (e.g. 2026) is 8 Falgun
        val feb21 = LocalDate.of(2026, 2, 21)
        val banglaDate = BengaliCalendarConverter.convert(feb21)

        assertEquals(8, banglaDate.day)
        assertEquals(11, banglaDate.monthIndex)
        assertEquals("ফাল্গুন", banglaDate.monthNameBn)
        assertEquals("বসন্ত", banglaDate.seasonBn)
    }

    @Test
    fun testLeapYearFalgunLength() {
        // In a leap year like 2024, Falgun has 30 days (Feb 14 to Mar 14)
        // Feb 29, 2024 should be 16 Falgun 1430
        val feb29 = LocalDate.of(2024, 2, 29)
        val banglaDate = BengaliCalendarConverter.convert(feb29)

        assertEquals(16, banglaDate.day)
        assertEquals(11, banglaDate.monthIndex)
        assertEquals("ফাল্গুন", banglaDate.monthNameBn)

        // March 14, 2024 is the 30th of Falgun in leap year
        val mar14 = LocalDate.of(2024, 3, 14)
        val mar14Date = BengaliCalendarConverter.convert(mar14)
        assertEquals(30, mar14Date.day)
        assertEquals(11, mar14Date.monthIndex)

        // March 15 is always 1st Choitro
        val mar15 = LocalDate.of(2024, 3, 15)
        val mar15Date = BengaliCalendarConverter.convert(mar15)
        assertEquals(1, mar15Date.day)
        assertEquals(12, mar15Date.monthIndex)
    }

    @Test
    fun testBengaliNumeralFormatting() {
        assertEquals("০", BengaliDateFormatter.toBengaliDigits(0))
        assertEquals("১২৩৪৫৬৭৮৯", BengaliDateFormatter.toBengaliDigits("123456789"))
        assertEquals("২০২৬", BengaliDateFormatter.toBengaliDigits(2026))
    }

    @Test
    fun testHijriConversionAndOffset() {
        val testDate = LocalDate.of(2026, 3, 20) // Expected around Eid-ul-Fitr 1447
        val hijriDateNoOffset = HijriCalendarConverter.convert(testDate, 0)
        assertNotNull(hijriDateNoOffset)
        assertTrue(hijriDateNoOffset.year in 1446..1448)

        // Test offset -1 day and +1 day
        val hijriPlusOne = HijriCalendarConverter.convert(testDate, 1)
        val hijriMinusOne = HijriCalendarConverter.convert(testDate, -1)
        assertNotNull(hijriPlusOne)
        assertNotNull(hijriMinusOne)
    }
}
