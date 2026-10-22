package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.HolidayRepository
import com.example.data.PreferencesRepository
import com.example.ui.screens.CalendarScreen
import com.example.ui.screens.HolidaysScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.BdCalendarTheme
import com.example.ui.theme.BdGreenPrimary
import com.example.ui.theme.BdRedAccent
import com.example.util.AppConstants
import com.example.viewmodel.CalendarViewModel
import com.example.viewmodel.CalendarViewModelFactory
import com.example.viewmodel.HolidaysViewModel
import com.example.viewmodel.HolidaysViewModelFactory
import com.example.viewmodel.SettingsViewModel
import com.example.viewmodel.SettingsViewModelFactory

enum class AppScreen(val route: String) {
    HOME("home"),
    CALENDAR("calendar"),
    HOLIDAYS("holidays"),
    SETTINGS("settings")
}

class MainActivity : ComponentActivity() {

    private lateinit var holidayRepository: HolidayRepository
    private lateinit var preferencesRepository: PreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        holidayRepository = HolidayRepository(applicationContext)
        preferencesRepository = PreferencesRepository(applicationContext)

        setContent {
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModelFactory(preferencesRepository)
            )
            val settingsState by settingsViewModel.uiState.collectAsState()

            val isDarkTheme = when (settingsState.themeMode) {
                "dark" -> true
                "light" -> false
                else -> isSystemInDarkTheme()
            }

            val calendarViewModel: CalendarViewModel = viewModel(
                factory = CalendarViewModelFactory(holidayRepository, preferencesRepository)
            )
            val holidaysViewModel: HolidaysViewModel = viewModel(
                factory = HolidaysViewModelFactory(holidayRepository)
            )

            val isBengali = settingsState.language == "bn"

            BdCalendarTheme(darkTheme = isDarkTheme) {
                CalendarAppContent(
                    isBengali = isBengali,
                    calendarViewModel = calendarViewModel,
                    holidaysViewModel = holidaysViewModel,
                    settingsViewModel = settingsViewModel
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarAppContent(
    isBengali: Boolean,
    calendarViewModel: CalendarViewModel,
    holidaysViewModel: HolidaysViewModel,
    settingsViewModel: SettingsViewModel
) {
    var currentScreen by remember { mutableStateOf(AppScreen.HOME) }

    // Intercept back button to navigate to Home before exiting
    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        currentScreen = AppScreen.HOME
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        // Flag emblem dot
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(BdGreenPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(BdRedAccent)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isBengali) AppConstants.APP_NAME_BN else AppConstants.APP_NAME_EN,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    // Quick language toggle in header
                    IconButton(
                        onClick = {
                            val newLang = if (isBengali) "en" else "bn"
                            settingsViewModel.setLanguage(newLang)
                        },
                        modifier = Modifier.testTag("button_language_quick_toggle")
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isBengali) "EN" else "বাং",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("bottom_nav_bar")
            ) {
                NavigationBarItem(
                    selected = currentScreen == AppScreen.HOME,
                    onClick = { currentScreen = AppScreen.HOME },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Today,
                            contentDescription = "Home"
                        )
                    },
                    label = {
                        Text(
                            text = if (isBengali) "আজ" else "Home",
                            fontSize = 11.sp,
                            fontWeight = if (currentScreen == AppScreen.HOME) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    modifier = Modifier.testTag("nav_home"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )

                NavigationBarItem(
                    selected = currentScreen == AppScreen.CALENDAR,
                    onClick = { currentScreen = AppScreen.CALENDAR },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Calendar"
                        )
                    },
                    label = {
                        Text(
                            text = if (isBengali) "ক্যালেন্ডার" else "Calendar",
                            fontSize = 11.sp,
                            fontWeight = if (currentScreen == AppScreen.CALENDAR) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    modifier = Modifier.testTag("nav_calendar"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )

                NavigationBarItem(
                    selected = currentScreen == AppScreen.HOLIDAYS,
                    onClick = { currentScreen = AppScreen.HOLIDAYS },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Celebration,
                            contentDescription = "Holidays"
                        )
                    },
                    label = {
                        Text(
                            text = if (isBengali) "ছুটির তালিকা" else "Holidays",
                            fontSize = 11.sp,
                            fontWeight = if (currentScreen == AppScreen.HOLIDAYS) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    modifier = Modifier.testTag("nav_holidays"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )

                NavigationBarItem(
                    selected = currentScreen == AppScreen.SETTINGS,
                    onClick = { currentScreen = AppScreen.SETTINGS },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    },
                    label = {
                        Text(
                            text = if (isBengali) "সেটিংস" else "Settings",
                            fontSize = 11.sp,
                            fontWeight = if (currentScreen == AppScreen.SETTINGS) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    modifier = Modifier.testTag("nav_settings"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.HOME -> HomeScreen(
                    calendarViewModel = calendarViewModel,
                    isBengali = isBengali,
                    onNavigateToCalendar = { currentScreen = AppScreen.CALENDAR },
                    onNavigateToHolidays = { currentScreen = AppScreen.HOLIDAYS }
                )
                AppScreen.CALENDAR -> CalendarScreen(
                    calendarViewModel = calendarViewModel,
                    isBengali = isBengali
                )
                AppScreen.HOLIDAYS -> HolidaysScreen(
                    holidaysViewModel = holidaysViewModel,
                    isBengali = isBengali
                )
                AppScreen.SETTINGS -> SettingsScreen(
                    settingsViewModel = settingsViewModel,
                    isBengali = isBengali
                )
            }
        }
    }
}
