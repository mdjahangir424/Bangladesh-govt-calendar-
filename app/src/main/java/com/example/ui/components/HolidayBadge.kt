package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.HolidayCategory
import com.example.ui.theme.HolidayExecutiveAmber
import com.example.ui.theme.HolidayGeneralRed
import com.example.ui.theme.HolidayObservanceBlue
import com.example.ui.theme.WeekendGreen

@Composable
fun HolidayBadge(
    category: HolidayCategory,
    isBengali: Boolean,
    modifier: Modifier = Modifier
) {
    val (text, bgColor, textColor) = when (category) {
        HolidayCategory.GENERAL -> Triple(
            if (isBengali) "সাধারণ ছুটি" else "General Holiday",
            HolidayGeneralRed.copy(alpha = 0.12f),
            HolidayGeneralRed
        )
        HolidayCategory.EXECUTIVE -> Triple(
            if (isBengali) "নির্বাহী আদেশ" else "Executive Order",
            HolidayExecutiveAmber.copy(alpha = 0.14f),
            HolidayExecutiveAmber
        )
        HolidayCategory.OBSERVANCE -> Triple(
            if (isBengali) "জাতীয় দিবস" else "Observance",
            HolidayObservanceBlue.copy(alpha = 0.12f),
            HolidayObservanceBlue
        )
        HolidayCategory.OPTIONAL -> Triple(
            if (isBengali) "ঐচ্ছিক ছুটি" else "Optional Holiday",
            Color(0xFF7C3AED).copy(alpha = 0.12f),
            Color(0xFF7C3AED)
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 3.dp)
            .testTag("holiday_badge_${category.name.lowercase()}")
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun WeekendBadge(
    isBengali: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(WeekendGreen.copy(alpha = 0.12f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
            .testTag("weekend_badge")
    ) {
        Text(
            text = if (isBengali) "সাপ্তাহিক ছুটি" else "Weekend",
            color = WeekendGreen,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
