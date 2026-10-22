package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CalendarDayInfo
import com.example.ui.components.DayDetailBottomSheet
import com.example.ui.components.HolidayBadge
import com.example.ui.theme.BdGreenPrimary
import com.example.ui.theme.BdRedAccent
import com.example.ui.theme.HolidayExecutiveAmber
import com.example.ui.theme.HolidayGeneralRed
import com.example.ui.theme.HolidayObservanceBlue
import com.example.ui.theme.WeekendGreen
import com.example.util.BengaliDateFormatter
import com.example.viewmodel.CalendarViewModel
import java.time.DayOfWeek

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    calendarViewModel: CalendarViewModel,
    isBengali: Boolean,
    modifier: Modifier = Modifier
) {
    val uiState by calendarViewModel.uiState.collectAsState()
    var showYearMonthDialog by remember { mutableStateOf(false) }
    var selectedDayForSheet by remember { mutableStateOf<CalendarDayInfo?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val currentYear = uiState.viewMonth.year
    val currentMonth = uiState.viewMonth.month

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("calendar_screen")
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 12.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Month Navigation Header Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("month_navigation_header"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { calendarViewModel.changeViewMonth(-1) },
                        modifier = Modifier.testTag("button_prev_month")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Previous Month",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Month & Year Clickable Picker
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { showYearMonthDialog = true }
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                            .testTag("button_select_year_month")
                    ) {
                        Text(
                            text = if (isBengali)
                                "${BengaliDateFormatter.getGregorianMonthBn(currentMonth)} ${BengaliDateFormatter.toBengaliDigits(currentYear)}"
                            else
                                "${currentMonth.name.lowercase().replaceFirstChar { it.uppercase() }} $currentYear",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (isBengali) "বছর ও মাস পরিবর্তন করুন ▼" else "Tap to change year/month ▼",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = { calendarViewModel.changeViewMonth(1) },
                        modifier = Modifier.testTag("button_next_month")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Next Month",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Today Button & Legend
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Legend
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LegendDot(color = WeekendGreen, label = if (isBengali) "সাপ্তাহিক" else "Weekend")
                        LegendDot(color = HolidayGeneralRed, label = if (isBengali) "সাধারণ" else "General")
                        LegendDot(color = HolidayExecutiveAmber, label = if (isBengali) "নির্বাহী" else "Executive")
                    }

                    OutlinedButton(
                        onClick = { calendarViewModel.jumpToToday() },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("button_jump_today")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Today,
                            contentDescription = "Today",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isBengali) "আজ" else "Today",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Weekday Headers (Starting with Friday per Bangladesh national standard)
        val weekdays = listOf(
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY,
            DayOfWeek.SUNDAY,
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            weekdays.forEach { dayOfWeek ->
                val isWeekendDay = dayOfWeek == DayOfWeek.FRIDAY || dayOfWeek == DayOfWeek.SATURDAY
                val label = if (isBengali)
                    BengaliDateFormatter.getWeekdayShortBn(dayOfWeek)
                else
                    BengaliDateFormatter.getWeekdayShortEn(dayOfWeek)

                Text(
                    text = label,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = if (isWeekendDay) WeekendGreen else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // 7-Column Calendar Grid
        val gridDays = uiState.daysInGrid
        val rows = gridDays.chunked(7)

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            rows.forEach { rowDays ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    rowDays.forEach { dayInfo ->
                        CalendarDayCell(
                            dayInfo = dayInfo,
                            isSelected = dayInfo.date == uiState.selectedDate,
                            isBengali = isBengali,
                            onClick = {
                                calendarViewModel.selectDate(dayInfo.date)
                                selectedDayForSheet = dayInfo
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Selected Day Details Summary Card
        val selectedInfo = uiState.selectedDayInfo
        if (selectedInfo != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedDayForSheet = selectedInfo }
                    .testTag("selected_day_summary_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isBengali)
                                BengaliDateFormatter.formatGregorianBn(selectedInfo.date)
                            else
                                BengaliDateFormatter.formatGregorianEn(selectedInfo.date),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (selectedInfo.isToday) {
                            Text(
                                text = if (isBengali) "আজ" else "Today",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "বাংলা: ${if (isBengali) selectedInfo.banglaDate.formatBn() else selectedInfo.banglaDate.formatEn()}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "হিজরি: ${if (isBengali) selectedInfo.hijriDate.formatBn() else selectedInfo.hijriDate.formatEn()}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    if (selectedInfo.hasHoliday) {
                        Spacer(modifier = Modifier.height(8.dp))
                        selectedInfo.holidays.forEach { holiday ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "★ " + (if (isBengali) holiday.nameBn else holiday.nameEn),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = HolidayGeneralRed
                                )
                                HolidayBadge(category = holiday.category, isBengali = isBengali)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isBengali) "বিস্তারিত তথ্য দেখতে স্পর্শ করুন ▸" else "Tap for full details ▸",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Detail Bottom Sheet
    if (selectedDayForSheet != null) {
        DayDetailBottomSheet(
            dayInfo = selectedDayForSheet,
            isBengali = isBengali,
            sheetState = sheetState,
            onDismiss = { selectedDayForSheet = null }
        )
    }

    // Month & Year Picker Dialog
    if (showYearMonthDialog) {
        YearMonthPickerDialog(
            currentYear = currentYear,
            currentMonth = currentMonth.value,
            isBengali = isBengali,
            onDismiss = { showYearMonthDialog = false },
            onSelect = { year, month ->
                calendarViewModel.setViewYearMonth(year, month)
                showYearMonthDialog = false
            }
        )
    }
}

@Composable
private fun CalendarDayCell(
    dayInfo: CalendarDayInfo,
    isSelected: Boolean,
    isBengali: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isCurrentMonth = dayInfo.isCurrentMonth
    val isWeekend = dayInfo.isWeekend
    val isToday = dayInfo.isToday
    val hasHoliday = dayInfo.hasHoliday

    val cellBgColor = when {
        isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        isWeekend && isCurrentMonth -> WeekendGreen.copy(alpha = 0.08f)
        else -> MaterialTheme.colorScheme.surface
    }

    val primaryTextColor = when {
        !isCurrentMonth -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.28f)
        hasHoliday -> HolidayGeneralRed
        isWeekend -> WeekendGreen
        else -> MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = modifier
            .aspectRatio(0.85f)
            .clip(RoundedCornerShape(8.dp))
            .background(cellBgColor)
            .then(
                if (isSelected) {
                    Modifier.border(1.5.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                } else if (isToday) {
                    Modifier.border(1.dp, BdRedAccent, RoundedCornerShape(8.dp))
                } else {
                    Modifier.border(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                }
            )
            .clickable { onClick() }
            .padding(2.dp)
            .testTag("calendar_day_${dayInfo.date}"),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize().padding(vertical = 2.dp)
        ) {
            // Gregorian Day Number (with Today red badge or standard text)
            if (isToday) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(BdRedAccent),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isBengali) BengaliDateFormatter.toBengaliDigits(dayInfo.date.dayOfMonth)
                        else dayInfo.date.dayOfMonth.toString(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            } else {
                Text(
                    text = if (isBengali) BengaliDateFormatter.toBengaliDigits(dayInfo.date.dayOfMonth)
                    else dayInfo.date.dayOfMonth.toString(),
                    fontSize = 12.sp,
                    fontWeight = if (hasHoliday || isWeekend) FontWeight.Bold else FontWeight.Medium,
                    color = primaryTextColor
                )
            }

            // Bangla Day & Hijri Day in micro-text
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (isBengali) BengaliDateFormatter.toBengaliDigits(dayInfo.banglaDate.day)
                    else "${dayInfo.banglaDate.day}",
                    fontSize = 9.sp,
                    color = if (isCurrentMonth) MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = if (isBengali) BengaliDateFormatter.toBengaliDigits(dayInfo.hijriDate.day)
                    else "${dayInfo.hijriDate.day}",
                    fontSize = 8.5.sp,
                    color = if (isCurrentMonth) MaterialTheme.colorScheme.secondary.copy(alpha = 0.75f)
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                )
            }

            // Holiday indicator dot
            if (hasHoliday && isCurrentMonth) {
                val dotColor = when {
                    dayInfo.hasGeneralHoliday -> HolidayGeneralRed
                    dayInfo.hasExecutiveHoliday -> HolidayExecutiveAmber
                    dayInfo.hasObservance -> HolidayObservanceBlue
                    else -> MaterialTheme.colorScheme.tertiary
                }
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(dotColor)
                )
            } else {
                Spacer(modifier = Modifier.size(5.dp))
            }
        }
    }
}

@Composable
private fun LegendDot(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun YearMonthPickerDialog(
    currentYear: Int,
    currentMonth: Int,
    isBengali: Boolean,
    onDismiss: () -> Unit,
    onSelect: (year: Int, month: Int) -> Unit
) {
    var tempYear by remember { mutableStateOf(currentYear) }
    var tempMonth by remember { mutableStateOf(currentMonth) }

    val years = listOf(2024, 2025, 2026, 2027, 2028, 2029, 2030)
    val months = (1..12).toList()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isBengali) "মাস ও বছর নির্বাচন" else "Select Month & Year",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(
                    text = if (isBengali) "বছর:" else "Year:",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    years.take(4).forEach { y ->
                        FilterChipSmall(
                            selected = y == tempYear,
                            text = if (isBengali) BengaliDateFormatter.toBengaliDigits(y) else y.toString(),
                            onClick = { tempYear = y }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    years.drop(4).forEach { y ->
                        FilterChipSmall(
                            selected = y == tempYear,
                            text = if (isBengali) BengaliDateFormatter.toBengaliDigits(y) else y.toString(),
                            onClick = { tempYear = y }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (isBengali) "মাস:" else "Month:",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(6.dp))

                months.chunked(3).forEach { monthRow ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        monthRow.forEach { m ->
                            val mObj = java.time.Month.of(m)
                            val mText = if (isBengali) BengaliDateFormatter.getGregorianMonthBn(mObj)
                            else mObj.name.take(3)
                            Box(modifier = Modifier.weight(1f)) {
                                FilterChipSmall(
                                    selected = m == tempMonth,
                                    text = mText,
                                    onClick = { tempMonth = m }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSelect(tempYear, tempMonth) },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = if (isBengali) "নিশ্চিত করুন" else "Apply")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = if (isBengali) "বাতিল" else "Cancel")
            }
        }
    )
}

@Composable
private fun FilterChipSmall(
    selected: Boolean,
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(
                if (selected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surfaceVariant
            )
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
