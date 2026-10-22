package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendar.BengaliCalendarConverter
import com.example.calendar.HijriCalendarConverter
import com.example.model.CalendarDayInfo
import com.example.model.Holiday
import com.example.model.HolidayCategory
import com.example.ui.components.DayDetailBottomSheet
import com.example.ui.components.HolidayBadge
import com.example.ui.theme.WeekendGreen
import com.example.util.BengaliDateFormatter
import com.example.viewmodel.HolidaysViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HolidaysScreen(
    holidaysViewModel: HolidaysViewModel,
    isBengali: Boolean,
    modifier: Modifier = Modifier
) {
    val uiState by holidaysViewModel.uiState.collectAsState()
    val context = LocalContext.current
    var selectedDayForSheet by remember { mutableStateOf<CalendarDayInfo?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("holidays_screen")
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Search Bar
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = { holidaysViewModel.setSearchQuery(it) },
            placeholder = {
                Text(
                    text = if (isBengali) "ছুটি বা দিবসের নাম অনুসন্ধান..." else "Search holiday or observance...",
                    fontSize = 13.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            trailingIcon = {
                if (uiState.searchQuery.isNotEmpty()) {
                    IconButton(onClick = { holidaysViewModel.setSearchQuery("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("holiday_search_input"),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Year Selector Chips Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            uiState.availableYears.forEach { year ->
                FilterChip(
                    selected = uiState.selectedYear == year,
                    onClick = { holidaysViewModel.setYear(year) },
                    label = {
                        Text(
                            text = if (isBengali) "${BengaliDateFormatter.toBengaliDigits(year)} সাল" else "Year $year",
                            fontWeight = if (uiState.selectedYear == year) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    modifier = Modifier.testTag("year_chip_$year"),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Category Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                selected = uiState.selectedCategory == null,
                onClick = { holidaysViewModel.setCategory(null) },
                label = { Text(text = if (isBengali) "সকল" else "All") }
            )
            FilterChip(
                selected = uiState.selectedCategory == HolidayCategory.GENERAL,
                onClick = { holidaysViewModel.setCategory(HolidayCategory.GENERAL) },
                label = { Text(text = if (isBengali) "সাধারণ ছুটি" else "General") }
            )
            FilterChip(
                selected = uiState.selectedCategory == HolidayCategory.EXECUTIVE,
                onClick = { holidaysViewModel.setCategory(HolidayCategory.EXECUTIVE) },
                label = { Text(text = if (isBengali) "নির্বাহী আদেশ" else "Executive") }
            )
            FilterChip(
                selected = uiState.selectedCategory == HolidayCategory.OBSERVANCE,
                onClick = { holidaysViewModel.setCategory(HolidayCategory.OBSERVANCE) },
                label = { Text(text = if (isBengali) "জাতীয় দিবস" else "Observance") }
            )
        }

        // Unverified Alert Banner if Year is not verified
        if (!uiState.isYearVerified) {
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("unverified_year_banner")
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Unverified",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isBengali)
                            "${BengaliDateFormatter.toBengaliDigits(uiState.selectedYear)} সালের সরকারি ছুটির প্রজ্ঞাপন এখনও জারি বা যাচাইকৃত হয়নি। উপস্থাপিত তালিকা সম্ভাব্য খসড়া।"
                        else
                            "Official holiday list for ${uiState.selectedYear} not yet verified by government gazette. The list shown is tentative.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Holidays List
        if (uiState.holidays.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isBengali) "কোনো ছুটির তথ্য পাওয়া যায়নি" else "No holidays found matching criteria",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.holidays) { holiday ->
                    HolidayCardItem(
                        holiday = holiday,
                        isBengali = isBengali,
                        onClick = {
                            val parsedDate = try { LocalDate.parse(holiday.startDate) } catch (e: Exception) { null }
                            if (parsedDate != null) {
                                val banglaDate = BengaliCalendarConverter.convert(parsedDate)
                                val hijriDate = HijriCalendarConverter.convert(parsedDate)
                                selectedDayForSheet = CalendarDayInfo(
                                    date = parsedDate,
                                    banglaDate = banglaDate,
                                    hijriDate = hijriDate,
                                    isToday = parsedDate == LocalDate.now(),
                                    isCurrentMonth = true,
                                    holidays = listOf(holiday)
                                )
                            }
                        },
                        onOpenSource = {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(holiday.sourceUrl))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Cannot open browser", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
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

@Composable
private fun HolidayCardItem(
    holiday: Holiday,
    isBengali: Boolean,
    onClick: () -> Unit,
    onOpenSource: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("holiday_card_${holiday.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Name & Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isBengali) holiday.nameBn else holiday.nameEn,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isBengali) holiday.nameEn else holiday.nameBn,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                HolidayBadge(category = holiday.category, isBengali = isBengali)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Date & Day Info
            val parsedDate = try { LocalDate.parse(holiday.startDate) } catch (e: Exception) { null }
            if (parsedDate != null) {
                val weekdayStr = if (isBengali) BengaliDateFormatter.getWeekdayNameBn(parsedDate.dayOfWeek)
                else BengaliDateFormatter.getWeekdayNameEn(parsedDate.dayOfWeek)

                val banglaDate = BengaliCalendarConverter.convert(parsedDate)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "📅 $weekdayStr, ${holiday.startDate}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isBengali) banglaDate.formatBn() else banglaDate.formatEn(),
                        fontSize = 11.5.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(
                text = if (isBengali) holiday.descriptionBn else holiday.descriptionEn,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Verification & Source Link Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (holiday.isVerified) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified",
                            tint = WeekendGreen,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isBengali) "যাচাইকৃত" else "Verified",
                            fontSize = 11.sp,
                            color = WeekendGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Unverified",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isBengali) "অযাচাইকৃত খসড়া" else "Unverified",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    if (holiday.isMoonDependent) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isBengali) "• চাঁদ দেখা সাপেক্ষ" else "• Moon-dependent",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Button(
                    onClick = onOpenSource,
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("open_source_btn_${holiday.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.OpenInBrowser,
                        contentDescription = "Official Source",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isBengali) "গেজেট সূত্র" else "Gazette Source",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}
