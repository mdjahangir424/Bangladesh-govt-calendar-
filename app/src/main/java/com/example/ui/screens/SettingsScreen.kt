package com.example.ui.screens

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CreatorCard
import com.example.util.AppConstants
import com.example.util.BengaliDateFormatter
import com.example.viewmodel.SettingsViewModel
import kotlin.math.roundToInt

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    isBengali: Boolean,
    modifier: Modifier = Modifier
) {
    val uiState by settingsViewModel.uiState.collectAsState()
    val context = LocalContext.current

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            settingsViewModel.setNotificationsEnabled(true)
            Toast.makeText(
                context,
                if (isBengali) "বিজ্ঞপ্তি সক্রিয় করা হয়েছে" else "Notifications enabled",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            settingsViewModel.setNotificationsEnabled(false)
            Toast.makeText(
                context,
                if (isBengali) "বিজ্ঞপ্তির অনুমতি প্রত্যাখ্যান করা হয়েছে" else "Notification permission denied",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("settings_screen")
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Screen Section Title: Preferences
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isBengali) "অ্যাপ সেটিংস" else "App Preferences",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Language Setting Card
        Card(
            modifier = Modifier.fillMaxWidth().testTag("language_setting_card"),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Language",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isBengali) "ভাষা নির্বাচন (Language)" else "Select Language",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SelectableOptionPill(
                        selected = uiState.language == "bn",
                        label = "বাংলা (Bengali)",
                        onClick = { settingsViewModel.setLanguage("bn") },
                        modifier = Modifier.weight(1f).testTag("lang_bn_option")
                    )
                    SelectableOptionPill(
                        selected = uiState.language == "en",
                        label = "English",
                        onClick = { settingsViewModel.setLanguage("en") },
                        modifier = Modifier.weight(1f).testTag("lang_en_option")
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Theme Setting Card
        Card(
            modifier = Modifier.fillMaxWidth().testTag("theme_setting_card"),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DarkMode,
                        contentDescription = "Theme",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isBengali) "থিম বা ডিসপ্লে মোড" else "Theme & Appearance",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SelectableOptionPill(
                        selected = uiState.themeMode == "system",
                        label = if (isBengali) "সিস্টেম" else "System",
                        onClick = { settingsViewModel.setTheme("system") },
                        modifier = Modifier.weight(1f).testTag("theme_system_option")
                    )
                    SelectableOptionPill(
                        selected = uiState.themeMode == "light",
                        label = if (isBengali) "লাইট" else "Light",
                        onClick = { settingsViewModel.setTheme("light") },
                        modifier = Modifier.weight(1f).testTag("theme_light_option")
                    )
                    SelectableOptionPill(
                        selected = uiState.themeMode == "dark",
                        label = if (isBengali) "ডার্ক" else "Dark",
                        onClick = { settingsViewModel.setTheme("dark") },
                        modifier = Modifier.weight(1f).testTag("theme_dark_option")
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Hijri Date Adjustment Card
        Card(
            modifier = Modifier.fillMaxWidth().testTag("hijri_adjustment_card"),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.NightsStay,
                        contentDescription = "Hijri",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isBengali) "হিজরি ক্যালেন্ডার সমন্বয় (চাঁদ দেখা)" else "Hijri Calendar Moon Adjustment",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (isBengali)
                        "বাংলাদেশে জাতীয় চাঁদ দেখা কমিটির ঘোষণা অনুযায়ী হিজরি তারিখ -২ থেকে +২ দিন সমন্বয় করতে পারবেন।"
                    else
                        "Adjust Hijri dates by -2 to +2 days to match Bangladesh National Moon Sighting Committee announcements.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                val offsetText = if (uiState.hijriOffset > 0) "+${uiState.hijriOffset}" else "${uiState.hijriOffset}"
                val offsetDisplay = if (isBengali) {
                    if (uiState.hijriOffset == 0) "স্বাভাবিক (০ দিন)"
                    else "$offsetText দিন সমন্বয়"
                } else {
                    if (uiState.hijriOffset == 0) "Standard (0 days)"
                    else "$offsetText days"
                }

                Text(
                    text = if (isBengali) "বর্তমান সমন্বয়: $offsetDisplay" else "Current offset: $offsetDisplay",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Slider(
                    value = uiState.hijriOffset.toFloat(),
                    onValueChange = { settingsViewModel.setHijriOffset(it.roundToInt()) },
                    valueRange = -2f..2f,
                    steps = 3,
                    modifier = Modifier.testTag("hijri_offset_slider")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "-2", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "-1", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "0", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "+1", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "+2", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Holiday Notification Preferences Card
        Card(
            modifier = Modifier.fillMaxWidth().testTag("notifications_card"),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isBengali) "ছুটির দিনের বিজ্ঞপ্তি ও রিমাইন্ডার" else "Holiday Notifications",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Switch(
                        checked = uiState.notificationsEnabled,
                        onCheckedChange = { checked ->
                            if (checked && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                settingsViewModel.setNotificationsEnabled(checked)
                            }
                        },
                        modifier = Modifier.testTag("notifications_toggle")
                    )
                }

                if (uiState.notificationsEnabled) {
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isBengali) "ছুটির দিন সকালে স্মরণ করিয়ে দিন" else "Notify on holiday morning",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Switch(
                            checked = uiState.notifyOnDay,
                            onCheckedChange = { settingsViewModel.setNotifyOnDay(it) },
                            modifier = Modifier.testTag("notify_on_day_toggle")
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isBengali) "ছুটির ১ দিন আগে স্মরণ করিয়ে দিন" else "Notify 1 day before holiday",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Switch(
                            checked = uiState.notifyDayBefore,
                            onCheckedChange = { settingsViewModel.setNotifyDayBefore(it) },
                            modifier = Modifier.testTag("notify_day_before_toggle")
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Reset Settings Button
        OutlinedButton(
            onClick = {
                settingsViewModel.resetPreferences()
                Toast.makeText(
                    context,
                    if (isBengali) "সেটিংস পুনঃস্থাপন করা হয়েছে" else "Preferences reset",
                    Toast.LENGTH_SHORT
                ).show()
            },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().testTag("button_reset_preferences")
        ) {
            Icon(
                imageVector = Icons.Default.RestartAlt,
                contentDescription = "Reset Preferences",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (isBengali) "সেটিংস পুনঃস্থাপন করুন" else "Reset Preferences to Default",
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Permanent Creator and Attribution Section
        CreatorCard(isBengali = isBengali)

        Spacer(modifier = Modifier.height(16.dp))

        // Technical Information & Offline Privacy
        Card(
            modifier = Modifier.fillMaxWidth().testTag("privacy_info_card"),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Privacy",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isBengali) "গোপনীয়তা ও অফলাইন নিরাপত্তা" else "Privacy & Offline Guarantee",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (isBengali)
                        "এই অ্যাপটি শতভাগ অফলাইনে কাজ করে। এটি কোনো ব্যক্তিগত তথ্য, ডিভাইসের অবস্থান বা ব্যবহারকারীর ডেটা সংগ্রহ করে না। কোনো লগইন বা ইন্টারনেটের প্রয়োজন নেই।"
                    else
                        "This app operates 100% offline without collecting any personal data, device location, or usage analytics. No account, sign-in, or internet connection is required.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = if (isBengali)
                        "সংস্করণ: ১.০ • ক্যালেন্ডার মান: বাংলা একাডেমি ২০১৯ সংস্করণ • হিজরি: উম্ম আল-কুরা"
                    else
                        "Version: 1.0 • Bangla Standard: Bangla Academy 2019 Revision • Hijri: Umm al-Qura standard",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun SelectableOptionPill(
    selected: Boolean,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (selected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
