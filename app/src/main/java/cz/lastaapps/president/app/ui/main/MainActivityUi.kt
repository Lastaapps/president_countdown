/*
 *   Copyright 2021, Petr Laštovička as Lasta apps, All rights reserved
 *
 *     This file is part of President Countdown.
 *
 *     This app is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This app is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this app.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package cz.lastaapps.president.app.ui.main

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cz.lastaapps.president.about.ui.About
import cz.lastaapps.president.app.ui.uimode.UIModeState
import cz.lastaapps.president.app.ui.uimode.UIModeStorage
import cz.lastaapps.president.app.ui.uimode.UIModeSwitch
import cz.lastaapps.president.clock.Clock
import cz.lastaapps.president.clock.WebMessages
import cz.lastaapps.president.notifications.ui.settings.NotificationSettings
import cz.lastaapps.president.privacypolicy.PrivacyPolicy
import cz.lastaapps.president.wallpaper.settings.ui.WallpaperSettings
import cz.lastaapps.president.whatsnew.WhatsNewProperties
import cz.lastaapps.president.whatsnew.ui.WhatsNewDialog
import cz.lastaapps.ui.common.extencions.rememberMutableSaveable
import cz.lastaapps.ui.common.extencions.viewModelKt
import cz.lastaapps.ui.common.layouts.ExpandableBottomLayout
import cz.lastaapps.ui.common.themes.MainTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import cz.lastaapps.president.navigation.NavigationConstants as N

private val padding = 16.dp
private const val UI_MODE_STORAGE_NAME = "MAIN_UI_MODE"

/**
 * UI for the Main activity
 * */
@Composable
internal fun MainActivityRoot(modifier: Modifier = Modifier) {

    //manages ui mode
    val context = LocalContext.current
    val uiModeStorage = remember(context) { UIModeStorage(context, UI_MODE_STORAGE_NAME) }
    val uiMode by uiModeStorage.getThemeFlow().collectAsState()

    MainTheme(UIModeState.isLight(uiMode)) {
        Surface(color = MaterialTheme.colors.background) {

            //shows privacy policy dialog
            if (checkPrivacyPolicy()) {

                //shows whats new
                CheckWhatsNew()

                //navigation between parts of the app
                val navController = rememberNavController()

                NavHost(navController, startDestination = N.id.home) {
                    composable(N.id.home) {
                        MainScaffold(
                            navController,
                            uiModeStorage,
                            modifier
                        )
                    }
                    composable(N.id.wallpaperSettings) { WallpaperSettings(modifier) }
                    composable(N.id.notificationSettings) { NotificationSettings(modifier) }
                    composable(N.id.about) { About(modifier) }
                }
            }
        }
    }
}

/**
 * @return If there is everything fine with the privacy agreement*/
@Composable
private fun checkPrivacyPolicy(): Boolean {

    val context = LocalContext.current
    var shown by remember { mutableStateOf(false) }

    //shown until necessary by the module
    if (!shown) {
        shown = PrivacyPolicy.shouldAgree(context)
    }

    PrivacyPolicy.Dialog(
        shown = shown,
        stateChanged = { shown = it }
    )

    return !shown
}

@Composable
private fun CheckWhatsNew() {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var shown by remember { mutableStateOf(false) }

    //loads the properties
    LaunchedEffect(key1 = "") {
        shown = WhatsNewProperties.getInstance(context, scope).shouldAutoShow()
    }

    WhatsNewDialog(
        shown = shown,
        visibilityChanged = { shown = it }
    )
}

/**
 * The main content - clock, navigation
 * Set's up the SnackBar
 * */
@Composable
private fun MainScaffold(
    navController: NavController,
    uiModeStorage: UIModeStorage,
    modifier: Modifier = Modifier
) {

    val scaffoldState = rememberScaffoldState()
    val viewModel = mainViewModel()

    //deletes SnackBar message on config/orientation change
    remember(LocalContext.current.resources) {
        viewModel.showSnackbar(null)
        null
    }

    LaunchedEffect("") {
        //wait's until SnackBar message has been deleted after a configuration change
        delay(100)
        viewModel.snackbarMessage.collectLatest {
            it?.let {
                scaffoldState.snackbarHostState.showSnackbar(it)
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            // reuse default SnackBarHost to have default animation and timing handling
            SnackbarHost(it) { data ->
                // custom SnackBar with the custom border
                Snackbar(
                    modifier = Modifier,
                    snackbarData = data
                )
            }
        },
        content = { innerPadding ->

            Content(
                navController,
                uiModeStorage,
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            )
        },
        modifier = modifier,
    )
}

/**
 * The main content - clock, navigation
 * */
@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun Content(
    navController: NavController,
    uiModeStorage: UIModeStorage,
    modifier: Modifier = Modifier
) {
    val viewModel = mainViewModel()

    ConstraintLayout(
        modifier = Modifier
            .padding(padding)
            .then(modifier)
    ) {

        val (clock, messages, overview, themeMode) = createRefs()

        var expanded by rememberMutableSaveable { mutableStateOf(false) }

        val guideClock = createGuidelineFromTop(0.3f)

        //Clock
        ClockStateHolder(
            modifier = Modifier
                .constrainAs(clock) {
                    if (!expanded) {
                        top.linkTo(guideClock)
                    } else {
                        top.linkTo(parent.top, margin = 24.dp)
                    }
                    centerHorizontallyTo(parent)
                },
        )

        //Web message
        AnimatedVisibility(
            visible = !expanded,
            enter = slideInVertically({ it * 3 / 2 }) + fadeIn(0f),
            exit = slideOutVertically({ it * 3 / 2 * -1 }) + fadeOut(0f),
            modifier = Modifier.constrainAs(messages) {
                top.linkTo(clock.bottom, margin = 24.dp)
                centerHorizontallyTo(clock)
            }
        ) {
            WebMessages()
        }

        val guideOverview = createGuidelineFromTop(0.3f)

        val isOpened by viewModel.isOpened.collectAsState()

        ExpandableBottomLayout(
            expanded = expanded,
            onExpanded = {
                expanded = it
                if (it)
                    viewModel.markOpened()
            },
            switchBlinking = !isOpened,
            modifier = Modifier
                .constrainAs(overview) {
                    bottom.linkTo(parent.bottom)
                    top.linkTo(guideOverview)
                    centerHorizontallyTo(parent)

                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
        ) {
            Card(
                backgroundColor = MaterialTheme.colors.primaryVariant,
                modifier = Modifier.fillMaxWidth(),
            ) {

                val scrollPosition = rememberScrollState(0)

                Overview(
                    navController,
                    modifier = Modifier
                        .padding(padding)
                        .verticalScroll(scrollPosition),
                    verticalArrangement = Arrangement.Bottom,
                )
            }
        }

        //Theme
        UIMode(
            expanded = expanded,
            uiModeStorage = uiModeStorage,
            modifier = Modifier.constrainAs(themeMode) {
                val themePadding = 0.dp

                top.linkTo(parent.top, themePadding)
                end.linkTo(parent.end, themePadding)
            }
        )
    }
}

@Composable
private fun ClockStateHolder(modifier: Modifier = Modifier) {

    val viewModel = mainViewModel()
    val clockState by remember { viewModel.clockState }.collectAsState()

    Clock(state = clockState, modifier = modifier)
}

@ExperimentalAnimationApi
@Composable
private fun UIMode(expanded: Boolean, uiModeStorage: UIModeStorage, modifier: Modifier = Modifier) {

    val uiMode by uiModeStorage.getThemeFlow().collectAsState()

    AnimatedVisibility(
        visible = expanded,
        enter = slideInVertically({ it * -2 }),
        exit = slideOutHorizontally({ it * 2 }),
        modifier = modifier
    ) {
        UIModeSwitch(uiMode, { uiModeStorage.setTheme(it) })
    }

}

@Composable
internal fun mainViewModel(
    key: String? = null,
    factory: ViewModelProvider.Factory? = null
): MainViewModel = viewModelKt(MainViewModel::class, key, factory)
