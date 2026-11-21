package com.graf.messenger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Campaign
import androidx.compose.material.icons.rounded.Contacts
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Search
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.styropyr0.prismal.PrismalGlass
import com.styropyr0.prismal.components.LocalPrismalBottomTabHighlightedIndex
import com.styropyr0.prismal.components.PrismalGlassBottomTab
import com.styropyr0.prismal.components.PrismalGlassBottomTabs
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
    Settings("Profile", Icons.Rounded.Person),
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

    Box(Modifier.fillMaxSize()) {
      Box(Modifier.fillMaxSize().prismalGlassLayer(backdrop).background(background)) {
        Column(Modifier.fillMaxSize()) {
                Spacer(Modifier.height(48.dp))
                AnimatedContent(
                    targetState = channelOpen,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "graf-screen"
                ) { inChannel ->
                    if (inChannel) {
                        ChannelScreen(dark, foreground, secondary) { channelOpen = false }
                    } else {
                        when (GrafTab.entries[selectedTab]) {
                            GrafTab.Home -> HomeScreen(dark, foreground, secondary) { channelOpen = true }
                            GrafTab.Contact -> SimpleScreen("Contacts", "Your people will appear here.", Icons.Rounded.Contacts, foreground, secondary)
                            GrafTab.Settings -> ProfileScreen(dark, foreground, secondary) { selectedTab = 0 }
                            GrafTab.Search -> SearchScreen(query, { query = it }, dark, foreground, secondary) {
                                channelOpen = true
                            }
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
                tabsCount = GrafTab.entries.size,
                tintDropletContent = false,
                // Keep Prismal's tuned defaults for the complete capsule and its liquid lens.
                modifier = Modifier.align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                GrafTab.entries.forEachIndexed { index, tab ->
                    GrafBottomTab(index, tab, selectedTab, dark) { selectedTab = index }
                }
            }
        }
    }
}

@Composable
private fun RowScope.GrafBottomTab(index: Int, tab: GrafTab, selected: Int, dark: Boolean, onClick: () -> Unit) {
    val highlighted = LocalPrismalBottomTabHighlightedIndex.current()
    val active = if (highlighted >= 0) highlighted == index else selected == index
    PrismalGlassBottomTab(onClick = onClick) {
        GrafIcon(tab.icon, tab.title, if (active) Blue else if (dark) Color(0xFFE5ECF5) else Color(0xFF303744), 22.dp)
        Text(tab.title, color = if (active) Blue else if (dark) Color(0xFFE5ECF5) else Color(0xFF303744), fontSize = 11.sp)
    }
}

@Composable
private fun GrafIcon(image: ImageVector, description: String?, tint: Color, size: Dp) {
    Icon(image, contentDescription = description, tint = tint, modifier = Modifier.size(size))
}

@Composable
private fun Header(title: String, subtitle: String? = null, foreground: Color, action: (@Composable () -> Unit)? = null) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, color = foreground, fontSize = 32.sp, fontWeight = FontWeight.Black)
            if (subtitle != null) Text(subtitle, color = foreground.copy(alpha = .62f), fontSize = 13.sp)
        }
        action?.invoke()
    }
}

@Composable
private fun HomeScreen(dark: Boolean, foreground: Color, secondary: Color, onOpenChannel: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Header("Graf", "Your conversations", foreground) {
            GlassIconButton(Icons.Rounded.Tune, dark, onClick = {})
        }
        GlassPanel(dark, Modifier.fillMaxWidth().padding(horizontal = 18.dp)) {
            Row(Modifier.fillMaxWidth().clickable(onClick = onOpenChannel).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                ChannelAvatar()
                Spacer(Modifier.width(13.dp))
                Column(Modifier.weight(1f)) {
                    Text("Graf News", color = foreground, fontSize = 17.sp, fontWeight = FontWeight.Black)
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
        Text("CHANNELS", color = secondary, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp))
        GlassPanel(dark, Modifier.fillMaxWidth().padding(horizontal = 18.dp)) {
            Row(Modifier.fillMaxWidth().clickable(onClick = onOpenChannel).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                ChannelAvatar()
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("Graf — Official Channel", color = foreground, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                    Text("Tap to open the test channel", color = secondary, fontSize = 12.sp)
                }
                GrafIcon(Icons.Rounded.MoreHoriz, "More", secondary, 24.dp)
            }
        }
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
private fun SettingsScreen(dark: Boolean, foreground: Color, secondary: Color, onThemeChange: (Boolean) -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Header("Settings", "Personalize Graf", foreground)
        Row(Modifier.fillMaxWidth().padding(horizontal = 18.dp).clip(CircleShape)
            .background(if (dark) Color.White.copy(alpha = .10f) else Color.White.copy(alpha = .28f))
            .clickable { onThemeChange(!dark) }.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Appearance", color = foreground, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text(if (dark) "Dark theme" else "Light theme", color = secondary, fontSize = 13.sp)
            }
            Text(if (dark) "DARK" else "LIGHT", color = Blue, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(12.dp))
        Text("GRAF MESSENGER · TEST BUILD 0.1.0", color = secondary, fontSize = 11.sp, letterSpacing = 1.sp,
            modifier = Modifier.padding(24.dp))
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
private fun ProfileScreen(dark: Boolean, foreground: Color, secondary: Color, onBack: () -> Unit) {
    val glassFill = if (dark) Color(0xFFBBD7F5).copy(alpha = .10f) else Color.White.copy(alpha = .24f)
    val glassStroke = Brush.linearGradient(
        if (dark) listOf(Color.White.copy(alpha = .72f), Color(0xFF8DBDFF).copy(alpha = .18f), Color.White.copy(alpha = .48f))
        else listOf(Color.White.copy(alpha = .96f), Color(0xFF6F9FC8).copy(alpha = .24f), Color.White.copy(alpha = .72f))
    )
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            ProfileGlassIcon(Icons.Rounded.ArrowBack, dark, glassFill, glassStroke, onBack)
            Spacer(Modifier.weight(1f))
            Row(
                Modifier.height(44.dp).clip(CircleShape).background(glassFill)
                    .border(1.dp, glassStroke, CircleShape).clickable { }.padding(horizontal = 17.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                GrafIcon(Icons.Rounded.Edit, "Edit profile", Blue, 17.dp)
                Spacer(Modifier.width(8.dp))
                Text("Изм.", color = foreground, fontSize = 14.sp, fontWeight = FontWeight.Black)
            }
        }
        Spacer(Modifier.height(38.dp))
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                Modifier.size(132.dp).clip(CircleShape)
                    .background(Brush.linearGradient(listOf(Color(0xFF66C8FF), Color(0xFF3678F6), Color(0xFF9878F5))))
                    .border(2.dp, glassStroke, CircleShape),
                contentAlignment = Alignment.Center
            ) { GrafIcon(Icons.Rounded.Person, "Profile photo", Color.White, 68.dp) }
            Spacer(Modifier.height(20.dp))
            Text("Ваш ник", color = foreground, fontSize = 25.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.height(6.dp))
            Text("@graf_user", color = secondary, fontSize = 14.sp, fontWeight = FontWeight.Black)
        }
        Spacer(Modifier.height(42.dp))
        Row(Modifier.fillMaxWidth().padding(horizontal = 24.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.Top) {
            ProfileAction(Icons.Rounded.PhotoCamera, "Изменить фото", dark, glassFill, glassStroke, secondary)
            ProfileAction(Icons.Rounded.Edit, "Изменить профиль", dark, glassFill, glassStroke, secondary)
            ProfileAction(Icons.Rounded.Settings, "Настройки", dark, glassFill, glassStroke, secondary)
        }
    }
}

@Composable
private fun ProfileGlassIcon(icon: ImageVector, dark: Boolean, fill: Color, stroke: Brush, onClick: () -> Unit) {
    Box(
        Modifier.size(44.dp).clip(CircleShape).background(fill).border(1.dp, stroke, CircleShape).clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) { GrafIcon(icon, "Back", if (dark) Color.White else Ink, 20.dp) }
}

@Composable
private fun ProfileAction(icon: ImageVector, label: String, dark: Boolean, fill: Color, stroke: Brush, secondary: Color) {
    Column(Modifier.width(96.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier.size(62.dp).clip(CircleShape).background(fill).border(1.dp, stroke, CircleShape).clickable { },
            contentAlignment = Alignment.Center
        ) { GrafIcon(icon, label, if (dark) Color.White else Ink, 24.dp) }
        Spacer(Modifier.height(10.dp))
        Text(label, color = secondary, fontSize = 11.sp, fontWeight = FontWeight.Black,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center, lineHeight = 14.sp, maxLines = 2)
    }
}
