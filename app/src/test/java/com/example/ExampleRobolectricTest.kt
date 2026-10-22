package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.HolidayRepository
import com.example.util.AppConstants
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.LocalDate

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun testAppStringsAndCreatorConstants() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Bangladesh Govt Calendar", appName)

        // Verify permanent creator info
        assertEquals("Md Jahangir Alam", AppConstants.CREATOR_NAME)
        assertEquals("+8801778600028", AppConstants.CREATOR_PHONE)
        assertEquals("mdjahangiralam2528@gmail.com", AppConstants.CREATOR_EMAIL)
    }

    @Test
    fun testBundledHolidayRepositoryOfflineLoading() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repository = HolidayRepository(context)

        // 2026 holidays must be loaded from assets
        val holidays2026 = repository.getHolidaysForYear(2026)
        assertTrue("2026 holidays should be populated", holidays2026.isNotEmpty())

        // 2026 is verified
        assertTrue("2026 holidays should be verified", repository.isYearVerified(2026))

        // Check specific key holiday: Mother Language Day 2026-02-21
        val feb21Holidays = repository.getHolidaysForDate(LocalDate.of(2026, 2, 21))
        assertEquals(1, feb21Holidays.size)
        assertEquals("শহীদ দিবস ও আন্তর্জাতিক মাতৃভাষা দিবস", feb21Holidays.first().nameBn)

        // 2027 holidays should be flagged as unverified
        val holidays2027 = repository.getHolidaysForYear(2027)
        assertTrue("2027 holidays should be loaded", holidays2027.isNotEmpty())
        assertFalse("2027 holidays must be flagged as unverified", repository.isYearVerified(2027))
    }
}
