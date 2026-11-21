package com.graf.messenger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Campaign
import androidx.compose.material.icons.rounded.Contacts
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.styropyr0.prismal.components.LocalPrismalBottomTabHighlightedIndex
import com.styropyr0.prismal.components.PrismalGlassBottomTab
import com.styropyr0.prismal.components.PrismalGlassBottomTabs
import com.styropyr0.prismal.PrismalGlassSurface
import com.styropyr0.prismal.shapes.PrismalRoundedRectangle
import com.styropyr0.prismal.sources.prismalGlassLayer
import com.styropyr0.prismal.sources.rememberPrismalGlassLayer

private val Ink = Color(0xFF10131B)
private val Muted = Color(0xFF7D8493)
private val Blue = Color(0xFF1688FF)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.navigationBarColor = android.graphics.Color.TRANSPARENT
        window.decorView.systemUiVisibility =
            android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
            android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        setContent { GrafApp() }
    }
}

private enum class GrafTab(val title: String, val icon: ImageVector) {
    Home("Home", Icons.Rounded.Home),
    Contact("Contact", Icons.Rounded.Contacts),
    Settings("Settings", Icons.Rounded.Settings),
    Search("Search", Icons.Rounded.Search)
}

@Composable
private fun GrafApp() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("graf_prefs", 0) }
    var dark by remember { mutableStateOf(prefs.getBoolean("dark_theme", false)) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var channelOpen by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }

    val backdrop = rememberPrismalGlassLayer()
    val background = if (dark) {
        Brush.linearGradient(listOf(Color(0xFF080D19), Color(0xFF182B43), Color(0xFF111522)))
    } else {
        Brush.linearGradient(listOf(Color(0xFFF6C7C1), Color(0xFF9EBAC7), Color(0xFFE6D5CC)))
    }
    val foreground = if (dark) Color(0xFFF5F7FC) else Ink
    val secondary = if (dark) Color(0xFFB5C0D0) else Muted

    Box(Modifier.fillMaxSize().background(background)) {
        // This is the real Prismal backdrop source. Glass components sample this layer.
        Box(
            Modifier.fillMaxSize().prismalGlassLayer(backdrop)
        ) {
            Column(Modifier.fillMaxSize()) {
                Spacer(Modifier.height(48.dp))
                AnimatedContent(
                    targetState = channelOpen,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "graf-screen"
                ) { inChannel ->
                    if (inChannel) {
                        ChannelScreen(
                            dark = dark,
                            foreground = foreground,
                            secondary = secondary,
                            onBack = { channelOpen = false }
                        )
                    } else {
                        when (GrafTab.entries[selectedTab]) {
                            GrafTab.Home -> HomeScreen(
                                dark, foreground, secondary,
                                onOpenChannel = { channelOpen = true }
                            )
                            GrafTab.Contact -> SimpleScreen(
                                "Contacts", "Your people will appear here.",
                                Icons.Rounded.Contacts, foreground, secondary
                            )
                            GrafTab.Settings -> SettingsScreen(
                                dark = dark,
                                foreground = foreground,
                                secondary = secondary,
                                onThemeChange = {
                                    dark = it
                                    prefs.edit().putBoolean("dark_theme", it).apply()
                                }
                            )
                            GrafTab.Search -> SearchScreen(
                                query = query,
                                onQueryChange = { query = it },
                                dark = dark,
                                foreground = foreground,
                                secondary = secondary,
                                onOpenChannel = { channelOpen = true }
                            )
                        }
                    }
                }
            }
        }

        if (!channelOpen) {
            PrismalGlassBottomTabs(
                selectedTabIndex = { selectedTab },
                onTabSelected = { selectedTab = it },
                backdrop = backdrop,
                tabsCount = 4,
                tintDropletContent = false,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(start = 18.dp, end = 18.dp, bottom = 22.dp)
                    .fillMaxWidth()
            ) {
                GrafTab.entries.forEachIndexed { index, tab ->
                    GrafBottomTab(index, tab, selectedTab) { selectedTab = index }
                }
            }
        }
    }
}

@Composable
private fun RowScope.GrafBottomTab(
    index: Int,
    tab: GrafTab,
    selected: Int,
    onClick: () -> Unit
) {
    val highlighted = LocalPrismalBottomTabHighlightedIndex.current()
    val active = if (highlighted >= 0) highlighted == index else selected == index
    PrismalGlassBottomTab(onClick = onClick) {
        androidx.compose.material3Compat.GrafIcon(
            image = tab.icon,
            contentDescription = tab.title,
            tint = if (active) Color(0xFF087FFF) else Color(0xFF303744),
            size = 22.dp
        )
        androidx.compose.material3Compat.GrafLabel(
            text = tab.title,
            color = if (active) Color(0xFF087FFF) else Color(0xFF303744)
        )
    }
}

@Composable
private fun Header(
    title: String,
    subtitle: String? = null,
    foreground: Color,
    action: (@Composable () -> Unit)? = null
) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, color = foreground, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            if (subtitle != null) Text(subtitle, color = foreground.copy(alpha = .62f), fontSize = 13.sp)
        }
        action?.invoke()
    }
}

@Composable
private fun HomeScreen(
    dark: Boolean,
    foreground: Color,
    secondary: Color,
    onOpenChannel: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Header("Graf", "Your conversations", foreground) {
            GlassIconButton(Icons.Rounded.Tune, dark, onClick = {})
        }
        GlassPanel(dark, Modifier.fillMaxWidth().padding(horizontal = 18.dp)) {
            Row(
                Modifier.fillMaxWidth().clickable(onClick = onOpenChannel).padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ChannelAvatar()
                Spacer(Modifier.width(13.dp))
                Column(Modifier.weight(1f)) {
                    Text("Graf News", color = foreground, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(4.dp))
                    Text("Welcome to Graf. This is our first channel.", color = secondary, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("2:07", color = secondary, fontSize = 11.sp)
                    Spacer(Modifier.height(8.dp))
                    Box(Modifier.size(8.dp).clip(CircleShape).background(Blue))
                }
            }
        }
        Spacer(Modifier.height(22.dp))
        Text(
            "CHANNELS",
            color = secondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )
        GlassPanel(dark, Modifier.fillMaxWidth().padding(horizontal = 18.dp)) {
            Row(
                Modifier.fillMaxWidth().clickable(onClick = onOpenChannel).padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ChannelAvatar()
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("Graf — Official Channel", color = foreground, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                    Text("Tap to open the test channel", color = secondary, fontSize = 12.sp)
                }
                androidx.compose.material3Compat.GrafIcon(Icons.Rounded.MoreHoriz, "More", secondary, 24.dp)
            }
        }
    }
}

@Composable
private fun ChannelScreen(
    dark: Boolean,
    foreground: Color,
    secondary: Color,
    onBack: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                    Text(
                        "Это тестовый канал нашего нового мессенджера. Здесь будут появляться новости и обновления Graf.",
                        color = secondary,
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    )
                    Spacer(Modifier.height(14.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        androidx.compose.material3Compat.GrafIcon(Icons.Rounded.Visibility, "Views", secondary, 15.dp)
                        Spacer(Modifier.width(5.dp))
                        Text("1", color = secondary, fontSize = 12.sp)
                        Spacer(Modifier.weight(1f))
                        Text("14:26", color = secondary, fontSize = 11.sp)
                    }
                }
            }
        }
        GlassPanel(dark, Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 12.dp)) {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Публикации доступны только администраторам", color = secondary, fontSize = 12.sp, modifier = Modifier.weight(1f))
                androidx.compose.material3Compat.GrafIcon(Icons.Rounded.Notifications, "Notifications", Blue, 21.dp)
            }
        }
    }
}

@Composable
private fun SettingsScreen(
    dark: Boolean,
    foreground: Color,
    secondary: Color,
    onThemeChange: (Boolean) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Header("Settings", "Personalize Graf", foreground)
        GlassPanel(dark, Modifier.fillMaxWidth().padding(horizontal = 18.dp)) {
            Row(
                Modifier.fillMaxWidth().clickable { onThemeChange(!dark) }.padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text("Appearance", color = foreground, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Text(if (dark) "Dark theme" else "Light theme", color = secondary, fontSize = 13.sp)
                }
                androidx.compose.material3Compat.GrafLabel(
                    text = if (dark) "DARK" else "LIGHT",
                    color = Blue
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        Text("GRAF MESSENGER · TEST BUILD 0.1.0", color = secondary, fontSize = 11.sp, letterSpacing = 1.sp, modifier = Modifier.padding(24.dp))
    }
}

@Composable
private fun SearchScreen(
    query: String,
    onQueryChange: (String) -> Unit,
    dark: Boolean,
    foreground: Color,
    secondary: Color,
    onOpenChannel: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Header("Search", "Find channels and messages", foreground)
        GlassPanel(dark, Modifier.fillMaxWidth().padding(horizontal = 18.dp)) {
            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                androidx.compose.material3Compat.GrafIcon(Icons.Rounded.Search, "Search", secondary, 21.dp)
                Spacer(Modifier.width(10.dp))
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = androidx.compose.ui.text.TextStyle(color = foreground, fontSize = 16.sp),
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
private fun SimpleScreen(
    title: String,
    message: String,
    icon: ImageVector,
    foreground: Color,
    secondary: Color
) {
    Column(Modifier.fillMaxSize()) {
        Header(title, foreground = foreground)
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                androidx.compose.material3Compat.GrafIcon(icon, null, secondary.copy(alpha = .7f), 42.dp)
                Spacer(Modifier.height(12.dp))
                Text(message, color = secondary, fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun GlassPanel(
    dark: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    // PrismalAGSL's own glass surface, sampling the app's live backdrop.
    val backdrop = rememberPrismalGlassLayer()
    PrismalGlassSurface(
        backdrop = backdrop,
        shape = { PrismalRoundedRectangle(24.dp) },
        tint = if (dark) Color(0xFF182438) else Color(0xFFF7F4F4),
        tintAlpha = if (dark) .24f else .20f,
        blurRadius = 10.dp,
        modifier = modifier
    ) {
        Column(content = content)
    }
}

@Composable
private fun ChannelAvatar(size: androidx.compose.ui.unit.Dp = 52.dp) {
    Box(
        Modifier.size(size).clip(CircleShape).background(
            Brush.linearGradient(listOf(Color(0xFF1688FF), Color(0xFF72D7FF), Color(0xFF9B7BFF)))
        ),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3Compat.GrafIcon(Icons.Rounded.Campaign, "Graf channel", Color.White, size * .52f)
    }
}

@Composable
private fun GlassIconButton(icon: ImageVector, dark: Boolean, onClick: () -> Unit) {
    Box(
        Modifier.size(42.dp).clip(CircleShape)
            .background(if (dark) Color.White.copy(alpha = .12f) else Color.White.copy(alpha = .34f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3Compat.GrafIcon(icon, null, if (dark) Color.White else Ink, 21.dp)
    }
}

