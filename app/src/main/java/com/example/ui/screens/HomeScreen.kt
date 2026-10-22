package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CalendarDayInfo
import com.example.model.Holiday
import com.example.ui.components.DayDetailBottomSheet
import com.example.ui.components.HolidayBadge
import com.example.ui.components.WeekendBadge
import com.example.ui.theme.BdGreenDark
import com.example.ui.theme.BdGreenPrimary
import com.example.ui.theme.BdRedAccent
import com.example.ui.theme.WeekendGreen
import com.example.util.BengaliDateFormatter
import com.example.viewmodel.CalendarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    calendarViewModel: CalendarViewModel,
    isBengali: Boolean,
    onNavigateToCalendar: () -> Unit,
    onNavigateToHolidays: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by calendarViewModel.uiState.collectAsState()
    val todayInfo = uiState.todayInfo
    var selectedDayForSheet by remember { mutableStateOf<CalendarDayInfo?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen")
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Hero Today Card with Bangladesh emerald gradient
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hero_today_card"),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(BdGreenPrimary, BdGreenDark)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        // Top row: App title + Day of week
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isBengali) "আজকের দিন" else "TODAY",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.85f),
                                letterSpacing = 1.sp
                            )

                            if (todayInfo?.isWeekend == true) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.White.copy(alpha = 0.2f))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = if (isBengali) "সাপ্তাহিক ছুটি" else "Weekend",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Gregorian Date
                        Text(
                            text = if (todayInfo != null) {
                                if (isBengali) BengaliDateFormatter.formatGregorianBn(todayInfo.date)
                                else BengaliDateFormatter.formatGregorianEn(todayInfo.date)
                            } else "",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Bangla Date Strip
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White.copy(alpha = 0.14f))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.WbSunny,
                                contentDescription = "Bangla Date",
                                tint = Color(0xFFFFD54F),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = if (todayInfo != null) {
                                        if (isBengali) todayInfo.banglaDate.formatBn()
                                        else todayInfo.banglaDate.formatEn()
                                    } else "",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = if (todayInfo != null) {
                                        if (isBengali) "ঋতু: ${todayInfo.banglaDate.seasonBn}"
                                        else "Season: ${todayInfo.banglaDate.seasonEn}"
                                    } else "",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Hijri Date Strip
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White.copy(alpha = 0.14f))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.NightsStay,
                                contentDescription = "Hijri Date",
                                tint = Color(0xFF80DEEA),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = if (todayInfo != null) {
                                        if (isBengali) todayInfo.hijriDate.formatBn()
                                        else todayInfo.hijriDate.formatEn()
                                    } else "",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = if (isBengali) "(চাঁদ দেখার ওপর নির্ভরশীল)" else "(Estimated lunar)",
                                    fontSize = 10.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }

                        // Today's holiday alert if any
                        if (todayInfo?.hasHoliday == true) {
                            Spacer(modifier = Modifier.height(12.dp))
                            todayInfo.holidays.forEach { holiday ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(BdRedAccent.copy(alpha = 0.9f))
                                        .padding(10.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Celebration,
                                            contentDescription = "Holiday",
                                            tint = Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = if (isBengali) "আজ সরকারি ছুটি!" else "Today is a Public Holiday!",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White.copy(alpha = 0.9f)
                                            )
                                            Text(
                                                text = if (isBengali) holiday.nameBn else holiday.nameEn,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                    }
                }
            }
        }

        // Next Government Holiday Card & Countdown
        item {
            val next = uiState.nextHoliday
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("next_holiday_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.HourglassTop,
                                contentDescription = "Countdown",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isBengali) "পরবর্তী সরকারি ছুটি" else "Next Government Holiday",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        if (next != null) {
                            val days = next.second
                            val countdownText = if (isBengali) {
                                if (days == 0L) "আজই!"
                                else if (days == 1L) "আগামীকাল"
                                else "আর ${BengaliDateFormatter.toBengaliDigits(days)} দিন"
                            } else {
                                if (days == 0L) "Today!"
                                else if (days == 1L) "Tomorrow"
                                else "$days days left"
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = countdownText,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (next != null) {
                        val holiday = next.first
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isBengali) holiday.nameBn else holiday.nameEn,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (isBengali) {
                                        "তারিখ: ${holiday.startDate} • ${if (holiday.isMoonDependent) "চাঁদ দেখা সাপেক্ষ" else "নির্দিষ্ট"}"
                                    } else {
                                        "Date: ${holiday.startDate} • ${if (holiday.isMoonDependent) "Moon-dependent" else "Fixed"}"
                                    },
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            HolidayBadge(category = holiday.category, isBengali = isBengali)
                        }
                    } else {
                        Text(
                            text = if (isBengali) "তথ্য পর্যালোচনাধীন" else "No upcoming holiday found",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Quick Navigation to Monthly Calendar
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToCalendar() }
                    .testTag("quick_calendar_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Calendar",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isBengali) "মাসিক ক্যালেন্ডার ভিউ" else "Monthly Calendar View",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (isBengali) "ইংরেজি, বাংলা ও হিজরি তারিখ দেখুন" else "View Gregorian, Bangla & Hijri dates",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Open Calendar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Upcoming Holidays List Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isBengali) "আসন্ন সরকারি ছুটিসমূহ" else "Upcoming Government Holidays",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                OutlinedButton(
                    onClick = onNavigateToHolidays,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("button_view_all_holidays")
                ) {
                    Text(
                        text = if (isBengali) "সব দেখুন" else "View All",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Upcoming Holidays Items
        if (uiState.upcomingHolidays.isEmpty()) {
            item {
                Text(
                    text = if (isBengali) "এই মুহূর্তে কোনো আসন্ন ছুটির তথ্য নেই" else "No upcoming holidays recorded",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        } else {
            items(uiState.upcomingHolidays) { holiday ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val date = try { java.time.LocalDate.parse(holiday.startDate) } catch (e: Exception) { null }
                            if (date != null) {
                                calendarViewModel.selectDate(date)
                                selectedDayForSheet = uiState.selectedDayInfo
                            }
                        }
                        .testTag("upcoming_holiday_item_${holiday.id}"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isBengali) holiday.nameBn else holiday.nameEn,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "📅 ${holiday.startDate}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        HolidayBadge(category = holiday.category, isBengali = isBengali)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (selectedDayForSheet != null) {
        DayDetailBottomSheet(
            dayInfo = selectedDayForSheet,
            isBengali = isBengali,
            sheetState = sheetState,
            onDismiss = { selectedDayForSheet = null }
        )
    }
}
