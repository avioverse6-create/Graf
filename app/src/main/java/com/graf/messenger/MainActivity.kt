package com.graf.messenger

import android.os.Bundle
import android.content.res.Configuration
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Chat
import kotlinx.coroutines.launch
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Circle

import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.provider.OpenableColumns
import androidx.compose.ui.geometry.Offset
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import java.io.File
import java.io.FileOutputStream
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.CloudUpload
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.FormatSize
import androidx.compose.material.icons.rounded.FormatBold

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.LinearEasing
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.background
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.Campaign
import androidx.compose.material.icons.rounded.Contacts
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.AddPhotoAlternate
import androidx.compose.material.icons.rounded.NotificationsOff
import androidx.compose.material.icons.rounded.VolumeOff
import io.github.neoruaa.glasmio.core.GlasmioTheme
import io.github.neoruaa.glasmio.glass.GlassEmphasis
import io.github.neoruaa.glasmio.glass.GlasmioBackdropHost
import io.github.neoruaa.glasmio.glass.GlassSurface
import io.github.neoruaa.glasmio.ui.GlasmioButton
import io.github.neoruaa.glasmio.ui.GlasmioIconButton
import io.github.neoruaa.glasmio.ui.GlasmioContextMenu
import io.github.neoruaa.glasmio.ui.GlasmioMenu
import io.github.neoruaa.glasmio.ui.GlasmioMenuItem
import com.styropyr0.prismal.PrismalGlass
import com.styropyr0.prismal.components.LocalPrismalBottomTabHighlightedIndex
import com.styropyr0.prismal.components.PrismalGlassBottomTab
import com.styropyr0.prismal.components.PrismalGlassBottomTabs
import com.styropyr0.prismal.components.PrismalGlassButton
import com.styropyr0.prismal.sources.prismalGlassLayer
import com.styropyr0.prismal.sources.rememberPrismalGlassLayer
import com.styropyr0.prismal.shapes.PrismalCapsule
import com.styropyr0.prismal.specular.PrismalSpecular
import com.styropyr0.prismal.specular.PrismalSpecularStyle

private val Ink = Color(0xFF10131B)
private val Muted = Color(0xFF7D8493)
private val Blue = Color(0xFF1688FF)
private val LocalGrafIconScale = staticCompositionLocalOf { 1f }
private val LocalGrafIconBold = staticCompositionLocalOf { false }
private fun grafGlassRim(dark: Boolean) = PrismalSpecular.Default.copy(
    width = 0.85.dp,
    blurRadius = 0.55.dp,
    style = PrismalSpecularStyle.Default(color = if (dark) Color.White.copy(alpha = 0.72f) else Color(0xFF64748B).copy(alpha = 0.38f))
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.navigationBarColor = android.graphics.Color.TRANSPARENT
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            window.navigationBarDividerColor = android.graphics.Color.TRANSPARENT
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            // Keep the gesture-navigation area fully transparent so chat content
            // continues behind the Prismal glass capsule without a dark system scrim.
            window.isNavigationBarContrastEnforced = false
            window.isStatusBarContrastEnforced = false
        }
        window.decorView.systemUiVisibility =
            android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                android.view.View.SYSTEM_UI_FLAG_FULLSCREEN or
                android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        setContent { GrafApp() }
    }
}

private val GrafSettingsCogIcon: ImageVector by lazy {
    ImageVector.Builder(name = "GrafSettingsCog", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 512f, viewportHeight = 512f)
        .apply { addPath(pathData = PathParser().parsePathString("""M456.9 242.2l-26.1-4.2c-3.5-.6-6.1-3.3-6.6-6.8-.5-3.2-1-6.4-1.7-9.5-.7-3.4.9-6.9 3.9-8.6l23.1-12.8c3.6-1.8 5.3-6.1 3.9-9.9l-4-11c-1.4-3.8-5.4-6-9.4-5l-25.9 5c-3.4.7-6.9-1-8.6-4.1-1.5-2.8-3.1-5.6-4.8-8.4-1.8-3-1.6-6.8.7-9.5l17.3-19.9c2.8-3 2.9-7.5.3-10.6l-7.5-9c-2.6-3.1-7.1-3.8-10.5-1.5L378.3 130c-3 1.8-6.8 1.4-9.4-.9-2.4-2.1-4.9-4.2-7.4-6.2-2.7-2.2-3.8-5.9-2.5-9.1l9.4-24.7c1.6-3.7.2-8.1-3.3-10.1l-10.2-5.9c-3.5-2-8-1.1-10.4 2.2l-16.6 20.8c-2 2.5-4.9 3.8-8.5 2.5 0 0-5.6-2.3-9.8-3.7-3.3-1.1-5.6-4.2-5.5-7.7l.4-26.4c.2-4.1-2.6-7.7-6.6-8.4l-11.6-2c-4-.7-7.9 1.7-9.1 5.6l-8.6 25c-1.1 3.3-4.3 5.5-7.8 5.4-1.6 0-3.3-.1-4.9-.1s-3.3 0-4.9.1c-3.5.1-6.6-2.1-7.8-5.4l-8.6-25c-1.2-3.9-5.1-6.3-9.1-5.6l-11.6 2c-4 .7-6.8 4.3-6.6 8.4l.4 26.4c.1 3.5-2.1 6.4-5.5 7.7-2.3.9-7.3 2.8-9.7 3.7-2.8 1-6.1.2-8.8-2.9l-16.5-20.3c-2.4-3.3-6.9-4.2-10.4-2.2l-10.2 5.9c-3.5 2-5 6.4-3.3 10.1l9.4 24.7c1.2 3.3.2 7-2.5 9.1-2.5 2-5 4.1-7.4 6.2-2.6 2.3-6.4 2.7-9.4.9L111 116.3c-3.4-2.2-7.9-1.6-10.5 1.5l-7.5 9c-2.6 3.1-2.5 7.7.3 10.6l17.3 19.9c2.3 2.6 2.6 6.5.7 9.5-1.7 2.7-3.3 5.5-4.8 8.4-1.7 3.1-5.1 4.7-8.6 4.1l-25.9-5c-4-.9-8 1.2-9.4 5l-4 11c-1.4 3.8.3 8.1 3.9 9.9L85.6 213c3.1 1.7 4.6 5.2 3.9 8.6-.6 3.2-1.2 6.3-1.7 9.5-.5 3.5-3.2 6.2-6.6 6.8l-26.1 4.2c-4 .5-7.1 3.9-7.1 7.9v11.7c0 4.1 3 7.5 7.1 7.9l26.1 4.2c3.5.6 6.1 3.3 6.6 6.8.5 3.2 1 6.4 1.7 9.5.7 3.4-.9 6.9-3.9 8.6l-23.1 12.8c-3.6 1.8-5.3 6.1-3.9 9.9l4 11c1.4 3.8 5.4 6 9.4 5l25.9-5c3.4-.7 6.9 1 8.6 4.1 1.5 2.8 3.1 5.6 4.8 8.4 1.8 3 1.6 6.8-.7 9.5l-17.3 19.9c-2.8 3-2.9 7.5-.3 10.6l7.5 9c2.6 3.1 7.1 3.8 10.5 1.5l22.7-13.6c3-1.8 6.8-1.4 9.4.9 2.4 2.1 4.9 4.2 7.4 6.2 2.7 2.2 3.8 5.9 2.5 9.1l-9.4 24.7c-1.6 3.7-.2 8.1 3.3 10.1l10.2 5.9c3.5 2 8 1.1 10.4-2.2l16.8-20.6c2.1-2.6 5.5-3.7 8.2-2.6 3.4 1.4 5.7 2.2 9.9 3.6 3.3 1.1 5.6 4.2 5.5 7.7l-.4 26.4c-.2 4.1 2.6 7.7 6.6 8.4l11.6 2c4 .7 7.9-1.7 9.1-5.6l8.6-25c1.1-3.3 4.3-5.5 7.8-5.4 1.6 0 3.3.1 4.9.1s3.3 0 4.9-.1c3.5-.1 6.6 2.1 7.8 5.4l8.6 25c1.2 3.9 5.1 6.3 9.1 5.6l11.6-2c4-.7 6.8-4.3 6.6-8.4l-.4-26.4c-.1-3.5 2.2-6.6 5.5-7.7 4.2-1.4 7-2.5 9.6-3.5 2.6-.9 5.8-1 8.3 2.1l17 20.9c2.4 3.3 6.9 4.2 10.4 2.2l10.2-5.9c3.5-2 5-6.4 3.3-10.1l-9.4-24.7c-1.2-3.3-.2-7 2.5-9.1 2.5-2 5-4.1 7.4-6.2 2.6-2.3 6.4-2.7 9.4-.9l22.7 13.6c3.4 2.2 7.9 1.6 10.5-1.5l7.5-9c2.6-3.1 2.5-7.7-.3-10.6l-17.3-19.9c-2.3-2.6-2.6-6.5-.7-9.5 1.7-2.7 3.3-5.5 4.8-8.4 1.7-3.1 5.1-4.7 8.6-4.1l25.9 5c4 .9 8-1.2 9.4-5l4-11c1.4-3.8-.3-8.1-3.9-9.9l-23.1-12.8c-3.1-1.7-4.6-5.2-3.9-8.6.6-3.2 1.2-6.3 1.7-9.5.5-3.5 3.2-6.2 6.6-6.8l26.1-4.2c4-.5 7.1-3.9 7.1-7.9v-11.7c-.2-3.8-3.2-7.3-7.3-7.7zM181.8 356.9c-5.2 9-17.4 10.7-25 3.6C129.2 334.2 112 297.1 112 256c0-40.9 17.1-77.9 44.5-104.1 7.5-7.2 19.8-5.5 25 3.5l56 96.6c1.4 2.5 1.4 5.5 0 8l-55.7 96.9zM396 289.7C380.9 353 323.9 400 256 400c-14.1 0-27.8-2-40.6-5.8-9.9-2.9-14.5-14.4-9.3-23.3l55.7-96.9c1.4-2.5 4.1-4 6.9-4h111.7c10.4 0 18 9.6 15.6 19.7zM380.5 242H268.7c-2.9 0-5.5-1.5-6.9-4l-56.1-96.7c-5.2-8.9-.7-20.4 9.2-23.4 13-3.9 26.8-5.9 41.1-5.9 67.9 0 124.9 47 140 110.3 2.4 10.1-5.2 19.7-15.5 19.7z""").toNodes(), fill = SolidColor(Color.White)) }
        .build()
}

private val GrafChatBubblesIcon: ImageVector by lazy {
    ImageVector.Builder(name = "GrafChatBubbles", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 512f, viewportHeight = 512f).apply {
        addPath(PathParser().parsePathString("M431 320.6c0-4.4 1.2-8.6 3.3-12.2.6-1.1 1.4-2.1 2.1-3.1 17.4-26 27.6-57.1 27.6-90.3.3-92.2-77.5-167-173.7-167-83.9 0-153.9 57.1-170.3 132.9-2.4 11.1-3.7 22.4-3.7 34.2 0 92.3 74.8 169.1 171 169.1 15.3 0 35.9-4.6 47.2-7.7 11.3-3.1 22.5-7.2 25.4-8.3 2.9-1.1 6.1-1.7 9.3-1.7 3.6 0 7 .7 10.1 2l56.7 20.1s2.4 1 3.9 1c4.4 0 8-3.5 8-8 0-1-.5-2.7-.5-2.7L431 320.6z").toNodes(), fill = SolidColor(Color.White))
        addPath(PathParser().parsePathString("M318.5 392.5c-3.6 1-8.2 2.1-13.2 3.2-10.5 2.2-23.9 4.5-34 4.5-96.2 0-171-76.8-171-169.1 0-6.6.7-15 1.5-21.4.6-4.3 1.3-8.6 2.3-12.8 1-4.5 2.2-9 3.5-13.4l-8 7.1C66.8 219.2 48 260 48 302.5c0 29.3 8.5 57.5 24.8 82 2.3 3.5 3.6 6.2 3.2 8-.4 1.8-11.9 62-11.9 62-.6 2.9.5 5.8 2.7 7.7 1.5 1.2 3.3 1.8 5.1 1.8 1 0 2-.2 2.9-.6l56.1-22.1c1.8-.7 3.7-1.1 5.7-1.1 0 0 2.4-.2 6.3 1.3 18.9 7.4 39.8 12 60.7 12 46.6 0 90.4-20.1 120.1-55.1 0 0 3.2-4.4 6.9-9.6-3.7 1.3-7.9 2.6-12.1 3.7z").toNodes(), fill = SolidColor(Color.White))
    }.build()
}

private val GrafContactsIcon: ImageVector by lazy {
    ImageVector.Builder(name = "GrafContacts", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        addPath(PathParser().parsePathString("M18.685 19.097A9.723 9.723 0 0 0 21.75 12c0-5.385-4.365-9.75-9.75-9.75S2.25 6.615 2.25 12a9.723 9.723 0 0 0 3.065 7.097A9.716 9.716 0 0 0 12 21.75a9.716 9.716 0 0 0 6.685-2.653Zm-12.54-1.285A7.486 7.486 0 0 1 12 15a7.486 7.486 0 0 1 5.855 2.812A8.224 8.224 0 0 1 12 20.25a8.224 8.224 0 0 1-5.855-2.438ZM15.75 9a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0Z").toNodes(), fill = SolidColor(Color.White))
    }.build()
}

private enum class GrafTab(val title: String, val icon: ImageVector) {
    Chats("Чаты", GrafChatBubblesIcon),
    Contacts("Контакты", GrafContactsIcon),
    Settings("Настройки", Icons.Rounded.Settings),
    Profile("Профиль", Icons.Rounded.Person)
}

@Composable
private fun GrafApp() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("graf_prefs", 0) }
    var dark by remember { mutableStateOf(prefs.getBoolean("dark_theme", true)) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var bottomBarVisible by remember { mutableStateOf(true) }
    var customFontPath by remember { mutableStateOf(prefs.getString("font_path", null)) }
    var textScale by remember { mutableFloatStateOf(prefs.getFloat("text_scale", 1f)) }
    var boldText by remember { mutableStateOf(prefs.getBoolean("bold_text", true)) }
    var iconScale by remember { mutableFloatStateOf(prefs.getFloat("icon_scale", 1f)) }
    var iconBold by remember { mutableStateOf(prefs.getBoolean("icon_bold", false)) }
    val backdrop = rememberPrismalGlassLayer()
    val activity = context as? ComponentActivity

    SideEffect {
        activity?.window?.decorView?.systemUiVisibility =
            android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                android.view.View.SYSTEM_UI_FLAG_FULLSCREEN or
                android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }

    val fontPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) runCatching {
            context.contentResolver.openInputStream(uri)?.use { input ->
                val file = File(context.filesDir, "graf_custom_font.ttf")
                FileOutputStream(file).use { input.copyTo(it) }
                customFontPath = file.absolutePath
                prefs.edit().putString("font_path", file.absolutePath).apply()
            }
        }
    }

    val background = if (dark) Brush.verticalGradient(listOf(Color(0xFF000000), Color(0xFF050505), Color(0xFF000000)))
        else Brush.verticalGradient(listOf(Color(0xFFF5F7FA), Color.White, Color(0xFFF0F3F7)))
    val foreground = if (dark) Color(0xFFF5F7FC) else Color(0xFF171B22)
    val secondary = if (dark) Color(0xFFB5C0D0) else Color(0xFF667085)
    val appFont = remember(customFontPath) {
        runCatching {
            customFontPath?.let { FontFamily(Font(File(it), weight = FontWeight.Normal)) }
        }.getOrNull() ?: FontFamily(Font(R.font.inter_variable, weight = FontWeight.W900))
    }
    val density = LocalDensity.current
    val themedConfiguration = Configuration(LocalConfiguration.current).apply {
        uiMode = (uiMode and Configuration.UI_MODE_NIGHT_MASK.inv()) or
            if (dark) Configuration.UI_MODE_NIGHT_YES else Configuration.UI_MODE_NIGHT_NO
    }

    CompositionLocalProvider(
        LocalConfiguration provides themedConfiguration,
        LocalDensity provides Density(density.density, (density.fontScale * textScale).coerceIn(.75f, 1.20f)),
        LocalTextStyle provides TextStyle(
            fontFamily = appFont,
            fontWeight = if (boldText) FontWeight.W900 else FontWeight.Normal
        ),
        LocalGrafIconScale provides iconScale,
        LocalGrafIconBold provides iconBold
    ) {
        Box(Modifier.fillMaxSize()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .prismalGlassLayer(backdrop)
                    .background(background)
            ) {
                Column(Modifier.fillMaxSize().padding(top = 24.dp)) {
                    AnimatedContent(
                        targetState = selectedTab,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "graf-screen"
                    ) { tab ->
                        when (GrafTab.entries[tab]) {
                            GrafTab.Chats -> ChatsScreen(
                                dark, foreground, secondary, textScale,
                                { textScale = it; prefs.edit().putFloat("text_scale", it).apply() },
                                { bottomBarVisible = it },
                                initialMode = "Chats"
                            )
                            GrafTab.Contacts -> ChatsScreen(
                                dark, foreground, secondary, textScale,
                                { textScale = it; prefs.edit().putFloat("text_scale", it).apply() },
                                { bottomBarVisible = it },
                                initialMode = "Contacts"
                            )
                            GrafTab.Settings -> SettingsScreen(
                                dark, foreground, secondary, textScale, boldText, iconScale, iconBold,
                                onBack = { selectedTab = 0 },
                                onProfileClick = { selectedTab = 3 },
                                onScaleChange = { textScale = it; prefs.edit().putFloat("text_scale", it).apply() },
                                onThemeChange = { dark = it; prefs.edit().putBoolean("dark_theme", it).apply() },
                                onBoldChange = { boldText = it; prefs.edit().putBoolean("bold_text", it).apply() },
                                onIconScaleChange = { iconScale = it; prefs.edit().putFloat("icon_scale", it).apply() },
                                onIconBoldChange = { iconBold = it; prefs.edit().putBoolean("icon_bold", it).apply() },
                                onFontPick = {
                                    fontPicker.launch(arrayOf("font/ttf", "application/x-font-ttf", "application/octet-stream"))
                                },
                                onFontReset = {
                                    customFontPath = null
                                    prefs.edit().remove("font_path").apply()
                                },
                                hasCustomFont = customFontPath != null
                            )
                            GrafTab.Profile -> SimpleScreen(
                                "Профиль",
                                "Профиль пока недоступен",
                                Icons.Rounded.Person,
                                foreground,
                                secondary
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = bottomBarVisible || selectedTab != 0,
                modifier = Modifier.align(Alignment.BottomCenter),
                enter = androidx.compose.animation.slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = androidx.compose.animation.slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                PrismalGlassBottomTabs(
                    selectedTabIndex = { selectedTab },
                    onTabSelected = { selectedTab = it; bottomBarVisible = true },
                    backdrop = backdrop,
                    tabsCount = GrafTab.entries.size,
                    tintDropletContent = false,
                    specular = { grafGlassRim(dark) },
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp).fillMaxWidth()
                ) {
                    GrafTab.entries.forEachIndexed { index, tab ->
                        GrafBottomTab(index, tab, selectedTab, dark) {
                            selectedTab = index
                            bottomBarVisible = true
                        }
                    }
                }
            }
        }
    }
}
@Composable
private fun GrafSettingsCog(active: Boolean, tint: Color) {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(active) {
        if (active) {
            progress.snapTo(0f)
            progress.animateTo(1f, tween(900, easing = LinearEasing))
        } else progress.snapTo(0f)
    }
    val p = progress.value
    // Match every other bottom-tab SVG icon to the user's icon-size setting.
    val iconScale = LocalGrafIconScale.current
    val iconBold = LocalGrafIconBold.current
    val configuredSize = 22.dp * iconScale * if (iconBold) 1.035f else 1f
    val rotation = when {
        p < .70f -> 300f * p / .70f
        p < .90f -> 300f + 60f * (p - .70f) / .20f
        else -> 360f
    }
    val scale = when {
        p < .20f -> 1f - .18f * p / .20f
        p < .70f -> .82f
        p < .90f -> .82f + .22f * (p - .70f) / .20f
        else -> 1.04f - .04f * (p - .90f) / .10f
    }
    Icon(imageVector = GrafSettingsCogIcon, contentDescription = "Settings", tint = tint, modifier = Modifier.size(configuredSize).graphicsLayer {
        rotationZ = rotation
        scaleX = scale
        scaleY = scale
        transformOrigin = androidx.compose.ui.graphics.TransformOrigin.Center
    })
}

@Composable
private fun RowScope.GrafBottomTab(index: Int, tab: GrafTab, selected: Int, dark: Boolean, onClick: () -> Unit) {
    val highlighted = LocalPrismalBottomTabHighlightedIndex.current()
    val active = if (highlighted >= 0) highlighted == index else selected == index
    PrismalGlassBottomTab(onClick = onClick) {
        val tabTint = if (active) Blue else if (dark) Color(0xFFE5ECF5) else Color(0xFF303744)
        if (tab == GrafTab.Settings) GrafSettingsCog(active, tabTint)
        else Box(Modifier.size(28.dp), contentAlignment = Alignment.Center) {
            GrafIcon(tab.icon, tab.title, tabTint, 22.dp)
        }
        Text(tab.title, color = tabTint, fontSize = 11.sp)
    }
}

@Composable
private fun GrafIcon(image: ImageVector, description: String?, tint: Color, size: Dp) {
    val scale = LocalGrafIconScale.current
    val bold = LocalGrafIconBold.current
    Icon(
        image,
        contentDescription = description,
        tint = tint,
        modifier = Modifier.size(size * scale.coerceIn(.75f, 1.30f) * if (bold) 1.035f else 1f)
    )
}

@Composable
private fun Header(title: String, subtitle: String? = null, foreground: Color, action: (@Composable () -> Unit)? = null) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, color = foreground, fontSize = 32.sp, fontWeight = LocalTextStyle.current.fontWeight)
            if (subtitle != null) Text(subtitle, color = foreground.copy(alpha = .62f), fontSize = 13.sp)
        }
        action?.invoke()
    }
}

private val GrafEditIcon: ImageVector by lazy {
    runCatching {
        ImageVector.Builder(
        name = "GrafEdit",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        addPath(
            pathData = PathParser().parsePathString(
                "m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10"
            ).toNodes(),
            fill = null,
            stroke = SolidColor(Color.White),
            strokeLineWidth = 1.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        )
    }.build()
    }.getOrElse { Icons.Rounded.Edit }
}


private fun loadGrafDemoMessages(
    context: android.content.Context,
    fallback: Map<String, List<GrafDemoMessage>>
): Map<String, List<GrafDemoMessage>> {
    val raw = context.getSharedPreferences("graf_demo_chat", 0).getString("messages", null) ?: return fallback
    return runCatching {
        val root = org.json.JSONObject(raw)
        val result = mutableMapOf<String, List<GrafDemoMessage>>()
        val keys = root.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val array = root.optJSONArray(key) ?: continue
            val items = mutableListOf<GrafDemoMessage>()
            for (index in 0 until array.length()) {
                val item = array.optJSONObject(index) ?: continue
                items += GrafDemoMessage(
                    id = item.optLong("id", System.currentTimeMillis() + index),
                    text = item.optString("text", ""),
                    outgoing = item.optBoolean("outgoing", false),
                    time = item.optString("time", ""),
                    replyTo = if (item.isNull("replyTo")) null else item.optString("replyTo"),
                    photoUri = if (item.isNull("photoUri")) null else item.optString("photoUri")
                )
            }
            result[key] = items
        }
        if (result.isEmpty()) fallback else result
    }.getOrDefault(fallback)
}

private fun saveGrafDemoMessages(context: android.content.Context, messages: Map<String, List<GrafDemoMessage>>) {
    runCatching {
        val root = org.json.JSONObject()
        messages.forEach { (key, list) ->
            val array = org.json.JSONArray()
            list.forEach { message ->
                array.put(
                    org.json.JSONObject()
                        .put("id", message.id)
                        .put("text", message.text)
                        .put("outgoing", message.outgoing)
                        .put("time", message.time)
                        .put("replyTo", message.replyTo ?: org.json.JSONObject.NULL)
                        .put("photoUri", message.photoUri ?: org.json.JSONObject.NULL)
                )
            }
            root.put(key, array)
        }
        context.getSharedPreferences("graf_demo_chat", 0).edit().putString("messages", root.toString()).apply()
    }
}

private data class GrafDemoMessage(
    val id: Long,
    val text: String,
    val outgoing: Boolean,
    val time: String,
    val replyTo: String? = null,
    val photoUri: String? = null
)

@Composable
private fun ChatsScreen(
    dark: Boolean,
    foreground: Color,
    secondary: Color,
    textScale: Float,
    onTextScaleChange: (Float) -> Unit,
    onScrollChrome: (Boolean) -> Unit,
    initialMode: String = "Chats",
) {
    data class Chat(val name: String, val handle: String, val preview: String, val time: String, val unread: Int, val online: Boolean)
    val chats = listOf(
        Chat("Алиса Brown", "@alicia", "Отправила тебе фото ✨", "12:42", 2, true),
        Chat("Graf Team", "@graf_team", "Новый дизайн уже готов", "11:18", 1, true),
        Chat("Максим", "@max_dev", "Давай обсудим идею", "Вчера", 0, false),
        Chat("Дизайн / идеи", "@design_room", "Артём: выглядит отлично!", "Пн", 0, true),
        Chat("Никита", "@nikita", "Увидимся вечером?", "Вс", 0, false)
    )
    val contacts = listOf(
        Chat("Алиса Brown", "@alicia", "В сети", "", 0, true),
        Chat("Graf Team", "@graf_team", "Команда Graf", "", 0, true),
        Chat("Максим", "@max_dev", "Был недавно", "", 0, false),
        Chat("Дизайн / идеи", "@design_room", "Групповой чат", "", 0, true),
        Chat("Никита", "@nikita", "Был давно", "", 0, false),
        Chat("Екатерина", "@kate", "В сети", "", 0, true)
    )
    var mode by remember(initialMode) { mutableStateOf(initialMode) }
    var menuExpanded by remember { mutableStateOf(false) }
    var chatMenuExpanded by remember { mutableStateOf(false) }
    var activeChat by remember { mutableStateOf<Chat?>(null) }
    var draft by remember { mutableStateOf("") }
    var replyingTo by remember { mutableStateOf<GrafDemoMessage?>(null) }
    var selectedMessage by remember { mutableStateOf<GrafDemoMessage?>(null) }
    var pendingPhoto by remember { mutableStateOf<Uri?>(null) }
    var emojiPickerOpen by remember { mutableStateOf(false) }
    var reactions by remember { mutableStateOf(mapOf<Long, String>()) }
    val context = LocalContext.current
    val seedMessages = remember {
        mapOf(
            "@alicia" to listOf(
                GrafDemoMessage(1L, "Привет! Как тебе новый Graf? 💙", false, "12:38"),
                GrafDemoMessage(2L, "Очень нравится стеклянный интерфейс!", true, "12:40"),
                GrafDemoMessage(3L, "Отправила тебе фото ✨", false, "12:42")
            ),
            "@graf_team" to listOf(
                GrafDemoMessage(4L, "Собрали новый экран чатов.", false, "11:12"),
                GrafDemoMessage(5L, "Новый дизайн уже готов", false, "11:18")
            ),
            "@max_dev" to listOf(GrafDemoMessage(6L, "Давай обсудим идею", false, "Вчера")),
            "@design_room" to listOf(GrafDemoMessage(7L, "Артём: выглядит отлично!", false, "Пн")),
            "@nikita" to listOf(GrafDemoMessage(8L, "Увидимся вечером?", false, "Вс"))
        )
    }
    var messagesByChat by remember { mutableStateOf(loadGrafDemoMessages(context, seedMessages)) }
    LaunchedEffect(messagesByChat) { saveGrafDemoMessages(context, messagesByChat) }
    val chatListState = rememberLazyListState()
    val messageListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            runCatching {
                context.contentResolver.takePersistableUriPermission(uri, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            pendingPhoto = uri
        }
    }
    LaunchedEffect(activeChat) {
        onScrollChrome(activeChat == null)
        if (activeChat != null) {
            kotlinx.coroutines.yield()
            val count = messagesByChat[activeChat!!.handle].orEmpty().size
            if (count > 0) messageListState.scrollToItem(count - 1)
        }
    }
    LaunchedEffect(chatListState, mode, activeChat) {
        var oldIndex = 0
        var oldOffset = 0
        snapshotFlow { chatListState.firstVisibleItemIndex to chatListState.firstVisibleItemScrollOffset }.collect { (index, offset) ->
            if (activeChat == null && mode == "Chats") {
                if (index > oldIndex || (index == oldIndex && offset > oldOffset)) onScrollChrome(index == 0 && offset < 36)
                if (index < oldIndex || (index == oldIndex && offset < oldOffset)) onScrollChrome(true)
            }
            oldIndex = index
            oldOffset = offset
        }
    }
    LaunchedEffect(activeChat?.handle, activeChat?.let { messagesByChat[it.handle].orEmpty().size }) {
        val count = activeChat?.let { messagesByChat[it.handle].orEmpty().size } ?: 0
        if (count > 0) messageListState.animateScrollToItem(count - 1)
    }
    fun clockNow(): String = android.text.format.DateFormat.format("HH:mm", System.currentTimeMillis()).toString()
    fun sendMessage(chat: Chat) {
        val body = draft.trim()
        val photo = pendingPhoto?.toString()
        if (body.isEmpty() && photo == null) return
        val sent = GrafDemoMessage(
            id = System.currentTimeMillis(),
            text = body,
            outgoing = true,
            time = clockNow(),
            replyTo = replyingTo?.text,
            photoUri = photo
        )
        messagesByChat = messagesByChat + (chat.handle to (messagesByChat[chat.handle].orEmpty() + sent))
        draft = ""
        replyingTo = null
        pendingPhoto = null
        emojiPickerOpen = false
        scope.launch {
            kotlinx.coroutines.delay(900)
            val answer = when (chat.handle) {
                "@alicia" -> listOf("Ахах, да! 😊", "Супер, мне нравится!", "Сейчас посмотрю 💙").random()
                "@graf_team" -> listOf("Принято! Уже проверяем.", "Отлично, спасибо!", "Скоро будет обновление 🚀").random()
                "@max_dev" -> listOf("Давай, я на связи.", "Хорошая идея!", "Согласен 👍").random()
                "@design_room" -> listOf("Спасибо за фидбек ✨", "Классная мысль!", "Обсудим чуть позже.").random()
                else -> listOf("Хорошо 😊", "Договорились!", "Сейчас отвечу подробнее.").random()
            }
            val incoming = GrafDemoMessage(System.currentTimeMillis(), answer, false, clockNow())
            messagesByChat = messagesByChat + (chat.handle to (messagesByChat[chat.handle].orEmpty() + incoming))
        }
    }

    GlasmioTheme(isDark = dark) {
        GlasmioBackdropHost(
            modifier = Modifier.fillMaxSize(),
            sampledContent = {
                Box(Modifier.fillMaxSize()) {
                    if (activeChat != null) {
                        // Telegram Android's MotionBackgroundDrawable default four-colour palette.
                        // Telegram renders these generated gradient wallpapers rather than relying on a fixed bitmap.
                        if (dark) {
                            Box(Modifier.fillMaxSize().background(Brush.linearGradient(listOf(Color(0xFF426D57), Color(0xFFF7E48B), Color(0xFF87A284), Color(0xFFFDF6CA)))))
                            Box(Modifier.fillMaxSize().background(Color(0xFF071018).copy(alpha = .82f)))
                        } else {
                            Box(Modifier.fillMaxSize().background(Color(0xFFE7EDF4)))
                            Box(Modifier.fillMaxSize().background(Color.White.copy(alpha = .70f)))
                        }
                        val chat = activeChat!!
                        val messages = messagesByChat[chat.handle].orEmpty()
                        LazyColumn(
                            state = messageListState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(start = 13.dp, end = 13.dp, top = 82.dp, bottom = 126.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(messages, key = { it.id }) { message ->
                                AnimatedVisibility(
                                    visible = true,
                                    enter = fadeIn(tween(180)) + androidx.compose.animation.slideInVertically(
                                        initialOffsetY = { it / 3 },
                                        animationSpec = spring(dampingRatio = 0.82f, stiffness = 520f)
                                    )
                                ) {
                                val isMine = message.outgoing
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start,
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    Box(Modifier.widthIn(max = 318.dp)) {
                                        Column(
                                            Modifier
                                                .clip(
                                                    if (isMine) androidx.compose.foundation.shape.RoundedCornerShape(20.dp, 20.dp, 6.dp, 20.dp)
                                                    else androidx.compose.foundation.shape.RoundedCornerShape(20.dp, 20.dp, 20.dp, 6.dp)
                                                )
                                                .background(if (dark) { if (isMine) Color(0xFF234F69).copy(alpha = .94f) else Color(0xFF202A35).copy(alpha = .96f) } else { if (isMine) Color(0xFFDCEEFF) else Color.White })
                                                .border(1.dp, if (dark) Color.White.copy(alpha = .12f) else Color(0xFFD9E0E8), androidx.compose.foundation.shape.RoundedCornerShape(20.dp))
                                                .combinedClickable(
                                                    onClick = {},
                                                    onLongClick = { selectedMessage = message }
                                                )
                                                .padding(horizontal = 12.dp, vertical = 9.dp)
                                        ) {
                                            if (message.replyTo != null) {
                                                Column(
                                                    Modifier.fillMaxWidth().padding(bottom = 6.dp)
                                                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                                                        .background(if (dark) Color.White.copy(alpha = .10f) else Color(0xFF172033).copy(alpha = .06f))
                                                        .padding(start = 8.dp, top = 5.dp, bottom = 5.dp, end = 7.dp)
                                                ) {
                                                    Text("Ответ", color = Blue, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                                    Text(message.replyTo, color = foreground.copy(alpha = .78f), fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                                }
                                            }
                                            if (message.photoUri != null) {
                                                coil.compose.AsyncImage(
                                                    model = Uri.parse(message.photoUri),
                                                    contentDescription = "Фото в сообщении",
                                                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                                    modifier = Modifier.fillMaxWidth().heightIn(max = 210.dp).widthIn(min = 160.dp)
                                                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(13.dp))
                                                )
                                            }
                                            if (message.text.isNotEmpty()) {
                                                Text(
                                                    message.text,
                                                    color = foreground,
                                                    fontSize = 14.sp,
                                                    lineHeight = 19.sp
                                                )
                                            }
                                            Row(
                                                Modifier.align(Alignment.End).padding(top = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(message.time, color = (if (dark) Color.White else Color(0xFF344054)).copy(alpha = .62f), fontSize = 10.sp)
                                                if (isMine) {
                                                    Spacer(Modifier.width(4.dp))
                                                    Text("✓✓", color = Color(0xFF9EDBFF), fontSize = 10.sp)
                                                }
                                            }
                                            val reaction = reactions[message.id]
                                            if (reaction != null) {
                                                Text(reaction, fontSize = 13.sp, modifier = Modifier.padding(top = 3.dp))
                                            }
                                        }
                                        GlasmioContextMenu(
                                            visible = selectedMessage?.id == message.id,
                                            itemCount = 4,
                                            menuWidth = 236.dp,
                                            opensAbove = true
                                        ) { menuBackdrop ->
                                            GlasmioMenuItem(
                                                title = "Ответить",
                                                systemName = "arrowshape.turn.up.left",
                                                backdrop = menuBackdrop,
                                                onClick = { replyingTo = message; selectedMessage = null }
                                            )
                                            GlasmioMenuItem(
                                                title = "Копировать",
                                                systemName = "doc.on.doc",
                                                backdrop = menuBackdrop,
                                                onClick = {
                                                    val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                                    clipboard.setPrimaryClip(android.content.ClipData.newPlainText("Graf", message.text))
                                                    selectedMessage = null
                                                }
                                            )
                                            GlasmioMenuItem(
                                                title = "Поставить ❤️",
                                                systemName = "heart",
                                                backdrop = menuBackdrop,
                                                onClick = { reactions = reactions + (message.id to "❤️"); selectedMessage = null }
                                            )
                                            GlasmioMenuItem(
                                                title = "Удалить у меня",
                                                systemName = "trash",
                                                destructive = true,
                                                backdrop = menuBackdrop,
                                                onClick = {
                                                    messagesByChat = messagesByChat + (chat.handle to messagesByChat[chat.handle].orEmpty().filterNot { it.id == message.id })
                                                    selectedMessage = null
                                                }
                                            )
                                        }
                                    }
                                }
                                }
                            }
                        }
                    } else {
                        Box(Modifier.fillMaxSize().background(if (dark) Color(0xFF050608) else Color(0xFFF5F7FA)))
                        if (mode == "Chats") {
                            LazyColumn(
                                state = chatListState,
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(top = 12.dp, bottom = 104.dp),
                                verticalArrangement = Arrangement.spacedBy(1.dp)
                            ) {
                                items(chats, key = { it.handle }) { chat ->
                                    val last = messagesByChat[chat.handle].orEmpty().lastOrNull()
                                    Row(
                                        Modifier.fillMaxWidth().clickable {
                                            activeChat = chat
                                            draft = ""
                                            replyingTo = null
                                            pendingPhoto = null
                                        }.padding(horizontal = 17.dp, vertical = 13.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            Modifier.size(56.dp).clip(CircleShape)
                                                .background(Brush.linearGradient(listOf(Color(0xFF305C77), Color(0xFF263746)))),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(chat.name.take(1).uppercase(), color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                            if (chat.online) Box(
                                                Modifier.align(Alignment.BottomEnd).padding(2.dp).size(12.dp).clip(CircleShape)
                                                    .background(Color(0xFF4DD58A)).border(2.dp, Color(0xFF080B10), CircleShape)
                                            )
                                        }
                                        Spacer(Modifier.width(13.dp))
                                        Column(Modifier.weight(1f)) {
                                            Text(chat.name, color = foreground, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                                            Text(last?.text?.ifBlank { "Фото" } ?: chat.preview, color = secondary, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 4.dp))
                                        }
                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(last?.time ?: chat.time, color = secondary, fontSize = 10.sp)
                                            if (chat.unread > 0) Box(
                                                Modifier.padding(top = 6.dp).clip(CircleShape).background(Blue).padding(horizontal = 7.dp, vertical = 3.dp)
                                            ) { Text(chat.unread.toString(), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                                        }
                                    }
                                    Box(Modifier.fillMaxWidth().padding(start = 86.dp).height(.6.dp).background(Color.White.copy(alpha = .08f)))
                                }
                            }
                        } else {
                            LazyColumn(
                                state = chatListState,
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(top = 12.dp, bottom = 104.dp)
                            ) {
                                items(contacts, key = { it.handle }) { contact ->
                                    Row(
                                        Modifier.fillMaxWidth().clickable {
                                            activeChat = contact
                                            mode = "Chats"
                                            draft = ""
                                        }.padding(horizontal = 18.dp, vertical = 13.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(Modifier.size(50.dp).clip(CircleShape).background(if (dark) Color(0xFF283642) else Color(0xFFE4EAF1)), contentAlignment = Alignment.Center) {
                                            Text(contact.name.take(1), color = foreground, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Spacer(Modifier.width(12.dp))
                                        Column(Modifier.weight(1f)) {
                                            Text(contact.name, color = foreground, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                                            Text(contact.preview, color = secondary, fontSize = 12.sp)
                                        }
                                        Text("Написать", color = Color(0xFF8CCBFF), fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            },
            overlayContent = {
                if (activeChat != null) {
                    val chat = activeChat!!
                    Column(Modifier.fillMaxSize()) {
                        GlassSurface(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp).height(66.dp),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(22.dp)
                        ) {
                            Row(Modifier.fillMaxSize().padding(horizontal = 7.dp), verticalAlignment = Alignment.CenterVertically) {
                                GlasmioIconButton(onClick = {
                                    activeChat = null
                                    selectedMessage = null
                                    replyingTo = null
                                    pendingPhoto = null
                                }, size = 42.dp) { GrafIcon(Icons.Rounded.ArrowBack, "Назад", foreground, 21.dp) }
                                Box(Modifier.size(40.dp).clip(CircleShape).background(Color(0xFF2D5268)), contentAlignment = Alignment.Center) {
                                    Text(chat.name.take(1), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    if (chat.online) Box(Modifier.align(Alignment.BottomEnd).size(10.dp).clip(CircleShape).background(Color(0xFF4DD58A)).border(1.5.dp, Color(0xFF17232D), CircleShape))
                                }
                                Spacer(Modifier.width(10.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(chat.name, color = foreground, fontSize = 15.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                                    Text(if (chat.online) "в сети" else "был(а) недавно", color = if (chat.online) Color(0xFF65D69B) else secondary, fontSize = 11.sp)
                                }
                                GlasmioIconButton(onClick = {}, size = 40.dp) { GrafIcon(Icons.Rounded.Call, "Позвонить", foreground, 19.dp) }
                                GlasmioMenu(
                                    expanded = chatMenuExpanded,
                                    onExpandedChange = { chatMenuExpanded = it },
                                    itemCount = 3,
                                    menuWidth = 230.dp,
                                    anchor = { open ->
                                        GlasmioIconButton(onClick = open, size = 40.dp) { GrafIcon(Icons.Rounded.MoreHoriz, "Ещё", foreground, 21.dp) }
                                    }
                                ) { menuBackdrop, close ->
                                    GlasmioMenuItem(title = "Увеличить текст", systemName = "plus.magnifyingglass", backdrop = menuBackdrop, onClick = { onTextScaleChange((textScale + .05f).coerceAtMost(1.20f)); close() })
                                    GlasmioMenuItem(title = "Уменьшить текст", systemName = "minus.magnifyingglass", backdrop = menuBackdrop, onClick = { onTextScaleChange((textScale - .05f).coerceAtLeast(.8f)); close() })
                                    GlasmioMenuItem(title = "Обычный размер", systemName = "textformat.size", backdrop = menuBackdrop, onClick = { onTextScaleChange(1f); close() })
                                }
                            }
                        }
                        Spacer(Modifier.weight(1f))
                        GlassSurface(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp).imePadding(),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(25.dp)
                        ) {
                            Column(Modifier.fillMaxWidth().padding(horizontal = 9.dp, vertical = 8.dp)) {
                                if (replyingTo != null) {
                                    Row(Modifier.fillMaxWidth().padding(start = 7.dp, bottom = 7.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Column(Modifier.weight(1f)) {
                                            Text("Ответ для сообщения", color = Color(0xFF8CCBFF), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            Text(replyingTo!!.text.ifBlank { "Фото" }, color = secondary, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                        }
                                        GlasmioIconButton(onClick = { replyingTo = null }, size = 30.dp) { GrafIcon(Icons.Rounded.Close, "Отменить ответ", secondary, 16.dp) }
                                    }
                                }
                                if (pendingPhoto != null) {
                                    Row(Modifier.fillMaxWidth().padding(start = 7.dp, bottom = 7.dp), verticalAlignment = Alignment.CenterVertically) {
                                        coil.compose.AsyncImage(model = pendingPhoto, contentDescription = "Выбранное фото", contentScale = androidx.compose.ui.layout.ContentScale.Crop, modifier = Modifier.size(54.dp).clip(androidx.compose.foundation.shape.RoundedCornerShape(10.dp)))
                                        Spacer(Modifier.width(8.dp))
                                        Text("Фото готово к отправке", color = secondary, fontSize = 11.sp, modifier = Modifier.weight(1f))
                                        GlasmioIconButton(onClick = { pendingPhoto = null }, size = 30.dp) { GrafIcon(Icons.Rounded.Close, "Убрать фото", secondary, 16.dp) }
                                    }
                                }
                                if (emojiPickerOpen) {
                                    Row(Modifier.fillMaxWidth().padding(bottom = 7.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                                        listOf("😀", "😂", "🥰", "❤️", "🔥", "👍", "✨", "🙏").forEach { emoji ->
                                            Text(emoji, modifier = Modifier.clickable { draft += emoji }.padding(5.dp), fontSize = 20.sp)
                                        }
                                    }
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    GlasmioIconButton(onClick = { imagePicker.launch(arrayOf("image/*")) }, size = 42.dp) {
                                        GrafIcon(Icons.Rounded.AddPhotoAlternate, "Прикрепить файл", foreground, 21.dp)
                                    }
                                    Spacer(Modifier.width(5.dp))
                                    GlasmioIconButton(onClick = { emojiPickerOpen = !emojiPickerOpen }, size = 38.dp) {
                                        Text("☺", color = foreground, fontSize = 22.sp)
                                    }
                                    Spacer(Modifier.width(4.dp))
                                    Box(
                                        Modifier.weight(1f).clip(androidx.compose.foundation.shape.RoundedCornerShape(22.dp))
                                            .background(if (dark) Color.White.copy(alpha = .085f) else Color(0xFF182230).copy(alpha = .055f)).padding(horizontal = 13.dp, vertical = 11.dp)
                                    ) {
                                        if (draft.isEmpty()) Text("Сообщение", color = secondary, fontSize = 14.sp)
                                        BasicTextField(
                                            value = draft,
                                            onValueChange = { draft = it },
                                            modifier = Modifier.fillMaxWidth().heightIn(min = 22.dp, max = 108.dp),
                                            textStyle = TextStyle(
                                                color = foreground,
                                                fontSize = 14.sp,
                                                fontFamily = LocalTextStyle.current.fontFamily,
                                                fontWeight = LocalTextStyle.current.fontWeight,
                                                lineHeight = 19.sp
                                            ),
                                            maxLines = 5,
                                            cursorBrush = SolidColor(Color(0xFF8CCBFF))
                                        )
                                    }
                                    Spacer(Modifier.width(7.dp))
                                    GlasmioIconButton(
                                        onClick = { sendMessage(chat) },
                                        size = 44.dp,
                                        modifier = Modifier.background(Blue, CircleShape)
                                    ) { GrafIcon(Icons.Rounded.Send, "Отправить", Color.White, 19.dp) }
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}


@Composable
private fun ContactsScreen(dark:Boolean,foreground:Color,secondary:Color) {
 Column(Modifier.fillMaxSize().padding(bottom=88.dp)) {
  Text("Контакты",color=foreground,fontSize=24.sp,fontWeight=FontWeight.W900,modifier=Modifier.padding(22.dp))
  Text("Контакты появятся здесь",color=secondary,fontSize=14.sp,modifier=Modifier.align(Alignment.CenterHorizontally).padding(top=90.dp))
 }
}

@Composable
private fun ChannelScreen(dark: Boolean, foreground: Color, secondary: Color, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            GlassIconButton(Icons.Rounded.ArrowBack, dark, onBack)
            Spacer(Modifier.width(12.dp))
            ChannelAvatar(38.dp)
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text("Graf", color = foreground, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                Text("Channel · 1 subscriber", color = secondary, fontSize = 12.sp)
            }
            GlassIconButton(Icons.Rounded.MoreHoriz, dark, onClick = {})
        }
        Spacer(Modifier.height(12.dp))
        Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
            GlassPanel(dark, Modifier.fillMaxWidth().padding(horizontal = 18.dp)) {
                Column(Modifier.padding(18.dp)) {
                    Text("GRAF", color = Blue, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                    Spacer(Modifier.height(10.dp))
                    Text("Добро пожаловать в Graf!", color = foreground, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text("Это тестовый канал нашего нового мессенджера. Здесь будут появляться новости и обновления Graf.",
                        color = secondary, fontSize = 15.sp, lineHeight = 22.sp)
                    Spacer(Modifier.height(14.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        GrafIcon(Icons.Rounded.Visibility, "Views", secondary, 15.dp)
                        Spacer(Modifier.width(5.dp))
                        Text("1", color = secondary, fontSize = 12.sp)
                        Spacer(Modifier.weight(1f))
                        Text("14:26", color = secondary, fontSize = 11.sp)
                    }
                }
            }
        }
        GlassPanel(dark, Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 12.dp)) {
            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 15.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Публикации доступны только администраторам", color = secondary, fontSize = 12.sp, modifier = Modifier.weight(1f))
                GrafIcon(Icons.Rounded.Notifications, "Notifications", Blue, 21.dp)
            }
        }
    }
}

@Composable
private fun SettingsScreen(dark:Boolean,foreground:Color,secondary:Color,textScale:Float,boldText:Boolean,iconScale:Float,iconBold:Boolean,onBack:()->Unit,onProfileClick:()->Unit,onScaleChange:(Float)->Unit,onThemeChange:(Boolean)->Unit,onBoldChange:(Boolean)->Unit,onIconScaleChange:(Float)->Unit,onIconBoldChange:(Boolean)->Unit,onFontPick:()->Unit,onFontReset:()->Unit,hasCustomFont:Boolean) {
 val fill=if(dark) Color.White.copy(alpha=.10f) else Color(0xFFE9EDF3)
 val stroke=if(dark) Color.White.copy(alpha=.24f) else Color(0xFFD5DCE6)
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal=18.dp).padding(bottom=108.dp)) {
  Row(Modifier.fillMaxWidth().padding(vertical=8.dp),verticalAlignment=Alignment.CenterVertically) {
   GlassIconButton(Icons.Rounded.ArrowBack,dark,onBack)
   Spacer(Modifier.width(14.dp)); Text("Настройки",color=foreground,fontSize=20.sp,fontWeight=FontWeight.W900)
  }
  SettingsGroup("АККАУНТ",foreground,secondary,fill,stroke) {
   SettingsRow(Icons.Rounded.Person,"Мой профиль","Фото и информация профиля",foreground,secondary,onProfileClick)
  }
  Spacer(Modifier.height(14.dp))
  SettingsGroup("ОФОРМЛЕНИЕ",foreground,secondary,fill,stroke) {
   Row(Modifier.fillMaxWidth().clickable { onThemeChange(!dark) }.padding(horizontal=16.dp,vertical=10.dp), verticalAlignment=Alignment.CenterVertically) {
    GrafIcon(if (dark) Icons.Rounded.LightMode else Icons.Rounded.DarkMode, "Тема", foreground, 21.dp)
    Spacer(Modifier.width(13.dp))
    Column(Modifier.weight(1f)) {
     Text("Светлая тема",color=foreground,fontSize=15.sp)
     Text(if (dark) "Выключена" else "Включена",color=secondary,fontSize=12.sp)
    }
    androidx.compose.material.Switch(checked=!dark,onCheckedChange={ onThemeChange(!it) })
   }
  }
  Spacer(Modifier.height(14.dp))
  SettingsGroup("ТЕКСТ И ШРИФТ",foreground,secondary,fill,stroke) {
   Column(Modifier.fillMaxWidth().padding(horizontal=16.dp,vertical=10.dp)) {
    Row(verticalAlignment=Alignment.CenterVertically) { GrafIcon(Icons.Rounded.FormatSize,"Размер",foreground,20.dp); Spacer(Modifier.width(12.dp)); Column(Modifier.weight(1f)) { Text("Размер текста",color=foreground,fontSize=15.sp); Text("${(textScale*100).toInt()}%",color=secondary,fontSize=12.sp) } }
    androidx.compose.material.Slider(value=textScale,onValueChange=onScaleChange,valueRange=.8f..1.20f)
   }
   SettingsRow(Icons.Rounded.FormatBold,"Жирный текст",if(boldText) "Включён" else "Выключен",foreground,secondary) { onBoldChange(!boldText) }
   SettingsRow(Icons.Rounded.CloudUpload,"Загрузить шрифт",if(hasCustomFont) "Свой TTF установлен" else "Выбрать файл .ttf",foreground,secondary,onFontPick)
   if(hasCustomFont) SettingsRow(Icons.Rounded.Close,"Сбросить шрифт","Вернуться к Inter",foreground,secondary,onFontReset)
  }
  Spacer(Modifier.height(14.dp))
  SettingsGroup("РАЗМЕР И СТИЛЬ ИКОНОК",foreground,secondary,fill,stroke) {
   Column(Modifier.fillMaxWidth().padding(horizontal=16.dp,vertical=10.dp)) {
    Row(verticalAlignment=Alignment.CenterVertically) {
     GrafIcon(Icons.Rounded.FormatSize,"Размер иконок",foreground,20.dp)
     Spacer(Modifier.width(12.dp))
     Column(Modifier.weight(1f)) {
      Text("Размер SVG-символов",color=foreground,fontSize=15.sp)
      Text("${(iconScale*100).toInt()}%",color=secondary,fontSize=12.sp)
     }
    }
    androidx.compose.material.Slider(value=iconScale,onValueChange=onIconScaleChange,valueRange=.75f..1.30f)
   }
   SettingsRow(Icons.Rounded.FormatBold,"Жирные символы",if(iconBold) "Включено" else "Выключено",foreground,secondary) { onIconBoldChange(!iconBold) }
  }
  Text("Размер и насыщенность меняют значки интерфейса, включая кнопки и нижнюю панель. Размер текста настраивается отдельно.",color=secondary,fontSize=12.sp,modifier=Modifier.padding(12.dp))
  Text("Поддерживаются файлы TTF. Шрифт хранится на устройстве.",color=secondary,fontSize=12.sp,modifier=Modifier.padding(horizontal=12.dp,vertical=4.dp))
 }
}
@Composable
private fun SettingsGroup(title:String,foreground:Color,secondary:Color,fill:Color,stroke:Color,content:@Composable ColumnScope.()->Unit) {
 Column(Modifier.fillMaxWidth()) { Text(title,color=secondary,fontSize=11.sp,letterSpacing=1.3.sp,modifier=Modifier.padding(start=12.dp,bottom=7.dp))
  Column(Modifier.fillMaxWidth().clip(androidx.compose.foundation.shape.RoundedCornerShape(25.dp)).background(fill).border(1.dp,stroke,androidx.compose.foundation.shape.RoundedCornerShape(25.dp)),content=content) }
}
@Composable
private fun SettingsRow(icon:ImageVector,title:String,subtitle:String,foreground:Color,secondary:Color,onClick:()->Unit) {
 Row(Modifier.fillMaxWidth().heightIn(min=64.dp).clickable(onClick=onClick).padding(horizontal=16.dp,vertical=13.dp),verticalAlignment=Alignment.CenterVertically) {
  GrafIcon(icon,title,foreground,21.dp); Spacer(Modifier.width(13.dp)); Column(Modifier.weight(1f)) { Text(title,color=foreground,fontSize=15.sp,maxLines=2,overflow=TextOverflow.Ellipsis); Text(subtitle,color=secondary,fontSize=12.sp,maxLines=2,overflow=TextOverflow.Ellipsis) }
  GrafIcon(Icons.Rounded.ArrowBack,null,secondary,16.dp)
 }
}
@Composable
private fun SearchScreen(query: String, onQueryChange: (String) -> Unit, dark: Boolean,
                         foreground: Color, secondary: Color, onOpenChannel: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Header("Search", "Find channels and messages", foreground)
        GlassPanel(dark, Modifier.fillMaxWidth().padding(horizontal = 18.dp)) {
            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                GrafIcon(Icons.Rounded.Search, "Search", secondary, 21.dp)
                Spacer(Modifier.width(10.dp))
                BasicTextField(
                    value = query, onValueChange = onQueryChange, singleLine = true,
                    textStyle = TextStyle(color = foreground, fontSize = 16.sp),
                    decorationBox = { inner ->
                        Box {
                            if (query.isEmpty()) Text("Search Graf", color = secondary, fontSize = 16.sp)
                            inner()
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        if ("graf".contains(query.trim(), ignoreCase = true) || query.isBlank()) {
            GlassPanel(dark, Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 14.dp)) {
                Row(Modifier.fillMaxWidth().clickable(onClick = onOpenChannel).padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    ChannelAvatar()
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Graf — Official Channel", color = foreground, fontWeight = FontWeight.SemiBold)
                        Text("Channel", color = secondary, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun SimpleScreen(title: String, message: String, icon: ImageVector, foreground: Color, secondary: Color) {
    Column(Modifier.fillMaxSize()) {
        Header(title, foreground = foreground)
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                GrafIcon(icon, null, secondary.copy(alpha = .7f), 42.dp)
                Spacer(Modifier.height(12.dp))
                Text(message, color = secondary, fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun GlassPanel(dark: Boolean, modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = modifier
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(24.dp))
            .background(if (dark) Color(0xFF253247).copy(alpha = .94f) else Color(0xFFF9F8FA).copy(alpha = .88f)),
        content = content
    )
}

@Composable
private fun ChannelAvatar(size: Dp = 52.dp) {
    Box(Modifier.size(size).clip(CircleShape).background(
        Brush.linearGradient(listOf(Color(0xFF1688FF), Color(0xFF72D7FF), Color(0xFF9B7BFF)))
    ), contentAlignment = Alignment.Center) {
        GrafIcon(Icons.Rounded.Campaign, "Graf channel", Color.White, size * .52f)
    }
}

@Composable
private fun GlassIconButton(icon: ImageVector, dark: Boolean, onClick: () -> Unit) {
    Box(Modifier.size(42.dp).clip(CircleShape)
        .background(if (dark) Color.White.copy(alpha = .12f) else Color.White.copy(alpha = .34f))
        .clickable(onClick = onClick), contentAlignment = Alignment.Center) {
        GrafIcon(icon, null, if (dark) Color.White else Ink, 21.dp)
    }
}

@Composable
private fun ProfileGlassIcon(icon:ImageVector,dark:Boolean,fill:Color,stroke:Brush,backdrop:com.styropyr0.prismal.PrismalBackdrop,onClick:()->Unit) {
 PrismalGlassButton(onClick=onClick,backdrop=backdrop,modifier=Modifier.size(48.dp),height=null,shape={ CircleShape },
  tint=if(dark) Color.White.copy(alpha=.08f) else Color.White.copy(alpha=.22f),contentPadding=PaddingValues(0.dp),horizontalArrangement=Arrangement.Center) {
  GrafIcon(icon,null,if(dark) Color.White else Ink,21.dp)
 }
}
@Composable
private fun ProfileScreen(
    dark: Boolean, foreground: Color, secondary: Color, photo: Uri?,
    onBack: () -> Unit, onOpenSettings: () -> Unit, onPhoto: () -> Unit, onViewPhoto: () -> Unit,
    profileColor: Color, onProfileColorChange: (Color) -> Unit,
    profileName: String, profileUsername: String, profilePhone: String,
    profileAddress: String, profileBirthday: String, profileChannels: String,
    onSaveProfile: (String, String, String, String, String, String) -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var muted by remember { mutableStateOf(false) }
    var editProfileOpen by remember { mutableStateOf(false) }
    var editName by remember(profileName) { mutableStateOf(profileName) }
    var editUsername by remember(profileUsername) { mutableStateOf(profileUsername) }
    var editPhone by remember(profilePhone) { mutableStateOf(profilePhone) }
    var editAddress by remember(profileAddress) { mutableStateOf(profileAddress) }
    var editBirthday by remember(profileBirthday) { mutableStateOf(profileBirthday) }
    var editChannels by remember(profileChannels) { mutableStateOf(profileChannels) }
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("graf_prefs", 0) }
    var trackUri by remember { mutableStateOf(prefs.getString("profile_track_uri", null)?.let(Uri::parse)) }
    var trackTitle by remember { mutableStateOf(prefs.getString("profile_track_title", null) ?: "Добавить песню") }
    var trackPlaying by remember { mutableStateOf(false) }
    var trackProgress by remember { mutableFloatStateOf(0f) }
    val player = remember { MediaPlayer() }
    val audioPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) runCatching {
            context.contentResolver.takePersistableUriPermission(uri, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
            player.reset()
            player.setDataSource(context, uri)
            player.prepare()
            player.start()
            trackUri = uri
            trackPlaying = true
            trackProgress = 0f
            val title = context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) cursor.getString(0) else null
            } ?: "Моя песня"
            trackTitle = title
            prefs.edit().putString("profile_track_uri", uri.toString()).putString("profile_track_title", title).apply()
        }.onFailure { trackPlaying = false }
    }
    DisposableEffect(player) { onDispose { runCatching { player.release() } } }
    LaunchedEffect(Unit) {
        trackUri?.let { savedUri -> runCatching { player.setDataSource(context, savedUri); player.prepare() } }
    }
    LaunchedEffect(trackPlaying, trackUri) {
        while (trackPlaying) {
            val duration = runCatching { player.duration }.getOrDefault(0)
            val position = runCatching { player.currentPosition }.getOrDefault(0)
            trackProgress = if (duration > 0) (position.toFloat() / duration).coerceIn(0f, 1f) else 0f
            if (duration > 0 && position >= duration) trackPlaying = false
            kotlinx.coroutines.delay(100)
        }
    }
    // One shared surface tone for every profile component, independent of profile background theme.
    val glassTone = if (dark) Color(0xFF25272B) else Color(0xFFE7ECF2)
    val page = if (dark) Color.Black else Color(0xFFF5F7FA)
    val text = if (dark) Color(0xFFF5F7F8) else Color(0xFF202529)
    val mutedText = if (dark) Color(0xFFE0E3E5) else Color(0xFF50575C)

    GlasmioTheme(isDark = dark) {
        GlasmioBackdropHost(
            modifier = Modifier.fillMaxSize(),
            sampledContent = { Box(Modifier.fillMaxSize().background(page)) },
            overlayContent = {
                Column(
                    Modifier.fillMaxSize().background(page).verticalScroll(rememberScrollState())
                        .padding(horizontal = 18.dp).padding(top = 8.dp, bottom = 118.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(Modifier.height(10.dp))
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        GlasmioIconButton(onClick = onBack, size = 42.dp) {
                            Icon(Icons.Rounded.ChevronLeft, "Назад", tint = text, modifier = Modifier.size(29.dp))
                        }
                        Spacer(Modifier.weight(1f))
                        GlasmioMenu(
                            expanded = menuExpanded, onExpandedChange = { menuExpanded = it }, itemCount = 2,
                            anchor = { open ->
                                GlasmioIconButton(onClick = open, size = 42.dp) {
                                    Icon(Icons.Rounded.MoreVert, "Меню профиля", tint = text, modifier = Modifier.size(21.dp))
                                }
                            },
                        ) { menuBackdrop, close ->
                            GlasmioMenuItem(title = "Изменить профиль", onClick = { editProfileOpen = true; close() }, systemName = "pencil", backdrop = menuBackdrop, enabled = true)
                            GlasmioMenuItem(title = "Добавить песню", onClick = { audioPicker.launch(arrayOf("audio/*")); close() }, systemName = "music.note", backdrop = menuBackdrop, enabled = true)
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                    Box(
                        Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(Modifier.fillMaxWidth().padding(top = 19.dp, bottom = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                Modifier.size(104.dp).clip(CircleShape).background(glassTone)
                                    .clickable(onClick = onViewPhoto)
                                    .pointerInput(photo) {
                                        var pullDistance = 0f
                                        detectVerticalDragGestures(
                                            onVerticalDrag = { _, amount -> if (amount > 0f) pullDistance += amount else pullDistance = 0f },
                                            onDragEnd = { if (pullDistance > 48.dp.toPx()) onViewPhoto(); pullDistance = 0f },
                                            onDragCancel = { pullDistance = 0f },
                                        )
                                    },
                                contentAlignment = Alignment.Center,
                            ) {
                                if (photo != null) ProfilePhotoImage(photo, Modifier.fillMaxSize().clip(CircleShape))
                                else GrafIcon(Icons.Rounded.Person, "Фото профиля", text, 48.dp)
                            }
                            Spacer(Modifier.height(11.dp))
                            Text(profileName, color = text, fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                            Spacer(Modifier.height(3.dp))
                            Text("@$profileUsername", color = Blue, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
                            Spacer(Modifier.height(4.dp))
                        }
                    }

                    Spacer(Modifier.height(7.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        GlassSurface(
                            modifier = Modifier.weight(1.45f).height(42.dp),
                            shape = CircleShape,
                            onClick = {
                                if (trackUri == null) audioPicker.launch(arrayOf("audio/*"))
                                else runCatching {
                                    if (player.isPlaying) { player.pause(); trackPlaying = false }
                                    else { player.start(); trackPlaying = true }
                                }
                            },
                            contentAlignment = Alignment.CenterStart,
                        ) {
                            Box(Modifier.fillMaxSize().clip(CircleShape)) {
                                Canvas(Modifier.fillMaxSize()) {
                                    if (trackProgress > 0f) {
                                        drawRoundRect(
                                            color = Color.White.copy(alpha = .20f),
                                            topLeft = Offset.Zero,
                                            size = androidx.compose.ui.geometry.Size(size.width * trackProgress, size.height),
                                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(size.height / 2f, size.height / 2f)
                                        )
                                    }
                                }
                                Row(Modifier.fillMaxSize().padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Rounded.MusicNote, null, tint = text, modifier = Modifier.size(16.dp))
                                    Spacer(Modifier.width(7.dp))
                                    Text(trackTitle, color = text, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                }
                            }
                        }
                        GlassSurface(
                            modifier = Modifier.weight(1f).height(42.dp),
                            shape = CircleShape,
                            onClick = { },
                        ) {
                            Row(Modifier.fillMaxSize().padding(horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                                Text("Баланс", color = mutedText, fontSize = 11.sp)
                                Spacer(Modifier.width(5.dp))
                                Text("◆ 0.29", color = text, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                Icon(Icons.Rounded.ArrowBack, null, tint = mutedText, modifier = Modifier.size(14.dp).graphicsLayer { rotationZ = 180f })
                            }
                        }
                    }

                    Spacer(Modifier.height(9.dp))
                    Row(Modifier.fillMaxWidth().padding(horizontal = 18.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.Top) {
                        ProfileGlasmioAction(Icons.Rounded.PhotoCamera, "Фото", text, mutedText, onPhoto)
                        ProfileGlasmioAction(Icons.Rounded.Edit, "Изменить", text, mutedText, { menuExpanded = true })
                        ProfileGlasmioAction(Icons.Rounded.Settings, "Настройки", text, mutedText, onOpenSettings)
                        ProfileGlasmioAction(if (muted) Icons.Rounded.NotificationsOff else Icons.Rounded.Notifications, "Беззвучно", text, mutedText, { muted = !muted })
                    }
                    Spacer(Modifier.height(16.dp))
                    GlassSurface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(25.dp),
                    ) {
                        Column(Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 18.dp)) {
                            ProfileDataRow("Номер", profilePhone, text, mutedText)
                            Spacer(Modifier.height(13.dp))
                            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                Text("Юз", color = mutedText, fontSize = 12.sp, modifier = Modifier.width(66.dp))
                                Text("@$profileUsername", color = Blue, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            }
                            Spacer(Modifier.height(13.dp))
                            ProfileDataRow("Адрес", profileAddress, text, mutedText)
                            if (profileBirthday.isNotBlank()) {
                                Spacer(Modifier.height(12.dp))
                                ProfileDataRow("Дата рождения", profileBirthday, text, mutedText)
                            }
                            if (profileChannels.isNotBlank()) {
                                Spacer(Modifier.height(12.dp))
                                ProfileDataRow("Каналы", profileChannels, text, mutedText)
                            }
                        }
                    }
                    Spacer(Modifier.height(13.dp))
                    Spacer(Modifier.height(15.dp))
                    Text("Истории", color = text, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(8.dp))
                    Text("Историй пока нет", color = mutedText.copy(alpha = .78f), fontSize = 11.sp)
                }
            },
        )
    }
    if (editProfileOpen) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { editProfileOpen = false },
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Column(
                Modifier.fillMaxSize().background(page).padding(horizontal = 22.dp, vertical = 28.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    GlasmioIconButton(onClick = { editProfileOpen = false }, size = 42.dp) {
                        Icon(Icons.Rounded.ChevronLeft, "Назад", tint = text, modifier = Modifier.size(25.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Text("Изменить профиль", color = text, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                }
                Spacer(Modifier.height(8.dp))
                ProfileEditField("Ник", editName, { editName = it }, text, mutedText)
                ProfileEditField("Username", editUsername, { editUsername = it.removePrefix("@") }, text, mutedText, "@")
                ProfileEditField("Телефон", editPhone, { editPhone = it }, text, mutedText)
                ProfileEditField("Адрес", editAddress, { editAddress = it }, text, mutedText)
                ProfileEditField("Дата рождения", editBirthday, { editBirthday = it }, text, mutedText)
                ProfileEditField("Каналы", editChannels, { editChannels = it }, text, mutedText)
                Text("В поле «Каналы» укажи публичные ссылки или названия каналов, которые нужно показать в профиле.",
                    color = mutedText, fontSize = 11.sp)
                Spacer(Modifier.height(8.dp))
                GlasmioButton(
                    onClick = {
                        onSaveProfile(editName.trim(), editUsername.trim().removePrefix("@"), editPhone.trim(),
                            editAddress.trim(), editBirthday.trim(), editChannels.trim())
                        editProfileOpen = false
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                ) {
                    Text("Сохранить изменения", color = text, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
                androidx.compose.material.TextButton(
                    onClick = { editProfileOpen = false },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Отмена", color = mutedText) }
            }
        }
    }
}

@Composable
private fun ProfileEditField(label: String, value: String, onValueChange: (String) -> Unit, foreground: Color, secondary: Color, prefix: String = "") {
    GlassSurface(modifier = Modifier.fillMaxWidth(), shape = androidx.compose.foundation.shape.RoundedCornerShape(15.dp)) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 11.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("$label  ", color = secondary, fontSize = 11.sp)
            if (prefix.isNotEmpty()) Text(prefix, color = Blue, fontSize = 13.sp)
            BasicTextField(
                value = value, onValueChange = onValueChange, singleLine = true,
                textStyle = androidx.compose.ui.text.TextStyle(color = foreground, fontSize = 13.sp),
                cursorBrush = androidx.compose.ui.graphics.SolidColor(Blue),
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun ProfileAddressMap(dark: Boolean, accent: Color) {
    Box(Modifier.fillMaxWidth().height(86.dp).clip(androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
        .background(if (dark) Color(0xFF18212C) else Color(0xFFE0E5EA))) {
        Canvas(Modifier.fillMaxSize()) {
            val road = if (dark) Color.White.copy(alpha = .16f) else Color(0xFF8995A3).copy(alpha = .42f)
            val minor = if (dark) Color.White.copy(alpha = .08f) else Color.White.copy(alpha = .75f)
            for (i in 0..5) {
                val y = size.height * i / 5f
                drawLine(road, androidx.compose.ui.geometry.Offset(0f, y), androidx.compose.ui.geometry.Offset(size.width, y + 16f), strokeWidth = 5f)
            }
            for (i in 0..6) {
                val x = size.width * i / 6f
                drawLine(minor, androidx.compose.ui.geometry.Offset(x, 0f), androidx.compose.ui.geometry.Offset(x - 20f, size.height), strokeWidth = 3f)
            }
            drawCircle(accent.copy(alpha = .22f), radius = 18f, center = androidx.compose.ui.geometry.Offset(size.width * .52f, size.height * .52f))
            drawCircle(accent, radius = 5f, center = androidx.compose.ui.geometry.Offset(size.width * .52f, size.height * .52f))
        }
        Row(Modifier.align(Alignment.BottomStart).padding(7.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.LocationOn, null, tint = accent, modifier = Modifier.size(14.dp))
            Text("Местоположение · 0-27-14", color = if (dark) Color.White.copy(alpha = .82f) else Color(0xFF333B48), fontSize = 9.sp)
        }
    }
}


@Composable
private fun ProfileDataRow(title: String, value: String, foreground: Color, secondary: Color) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(title, color = secondary, fontSize = 12.sp, modifier = Modifier.width(66.dp))
        Text(value, color = foreground, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun ProfileGlasmioAction(icon: ImageVector, label: String, foreground: Color, secondary: Color, onClick: () -> Unit) {
    Column(Modifier.width(68.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        // Use Glasmio's built-in button surface, rim and interaction treatment.
        GlasmioIconButton(onClick = onClick, size = 50.dp) {
            GrafIcon(icon, label, foreground, 21.dp)
        }
        Spacer(Modifier.height(5.dp))
        Text(label, color = secondary, fontSize = 9.sp, textAlign = TextAlign.Center, maxLines = 1)
    }
}

@Composable
private fun ProfilePhotoImage(uri:Uri,modifier:Modifier=Modifier) {
 val context=LocalContext.current
 val bitmap=remember(uri) { runCatching { context.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it)?.asImageBitmap() } }.getOrNull() }
 if(bitmap!=null) Image(bitmap,contentDescription="Profile photo",modifier=modifier,contentScale=androidx.compose.ui.layout.ContentScale.Crop)
}
@Composable
private fun ProfilePhotoViewer(uri:Uri,dark:Boolean,onClose:()->Unit) {
 var muted by remember { mutableStateOf(false) }
 val text = Color.White
 val secondary = Color.White.copy(alpha = .78f)
 GlasmioTheme(isDark = dark) {
  GlasmioBackdropHost(
   modifier = Modifier.fillMaxSize(),
   sampledContent = { ProfilePhotoImage(uri, Modifier.fillMaxSize()) },
   overlayContent = {
    Box(Modifier.fillMaxSize()) {
     GlasmioIconButton(onClick=onClose,size=42.dp,modifier=Modifier.align(Alignment.TopEnd).padding(22.dp)) {
      GrafIcon(Icons.Rounded.Close,"Закрыть",text,22.dp)
     }
     Row(Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(horizontal=18.dp, vertical=34.dp),horizontalArrangement=Arrangement.SpaceEvenly,verticalAlignment=Alignment.Top) {
      ProfileGlasmioAction(Icons.Rounded.PhotoCamera,"Фото",text,secondary,onClose)
      ProfileGlasmioAction(Icons.Rounded.Edit,"Изменить",text,secondary,onClose)
      ProfileGlasmioAction(Icons.Rounded.Settings,"Настройки",text,secondary,onClose)
      ProfileGlasmioAction(if(muted) Icons.Rounded.NotificationsOff else Icons.Rounded.Notifications,"Беззвучно",text,secondary,{muted=!muted})
     }
    }
   }
  )
 }
}
