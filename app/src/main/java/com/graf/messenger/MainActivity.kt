package com.graf.messenger

import android.os.Bundle
import android.graphics.BitmapFactory
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
import androidx.compose.material.LocalTextStyle
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
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
 val context=LocalContext.current
 val prefs=remember { context.getSharedPreferences("graf_prefs",0) }
 var dark by remember { mutableStateOf(prefs.getBoolean("dark_theme",false)) }
 var selectedTab by remember { mutableIntStateOf(0) }
 var settingsOpen by remember { mutableStateOf(false) }
 var channelOpen by remember { mutableStateOf(false) }
 var photoViewer by remember { mutableStateOf(false) }
 var query by remember { mutableStateOf("") }
 var profilePhoto by remember { mutableStateOf(prefs.getString("profile_photo",null)?.let(Uri::parse)) }
 var customFontPath by remember { mutableStateOf(prefs.getString("font_path",null)) }
 var textScale by remember { mutableFloatStateOf(prefs.getFloat("text_scale",1f)) }
 var boldText by remember { mutableStateOf(prefs.getBoolean("bold_text",true)) }
 val backdrop=rememberPrismalGlassLayer()
 val photoPicker=rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
  if(uri!=null) { runCatching { context.contentResolver.takePersistableUriPermission(uri,android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION) }; profilePhoto=uri; prefs.edit().putString("profile_photo",uri.toString()).apply() }
 }
 val fontPicker=rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
  if(uri!=null) runCatching { context.contentResolver.openInputStream(uri)?.use { input -> val file=File(context.filesDir,"graf_custom_font.ttf"); FileOutputStream(file).use { input.copyTo(it) }; customFontPath=file.absolutePath; prefs.edit().putString("font_path",file.absolutePath).apply() } }
 }
 val background=if(dark) Brush.linearGradient(listOf(Color(0xFF080D19),Color(0xFF182B43),Color(0xFF111522))) else Brush.linearGradient(listOf(Color(0xFFF4D8D0),Color(0xFFA9C4D1),Color(0xFFE9DDD5)))
 val foreground=if(dark) Color(0xFFF5F7FC) else Color(0xFF171B24)
 val secondary=if(dark) Color(0xFFB5C0D0) else Color(0xFF596273)
 val appFont=remember(customFontPath) { runCatching { customFontPath?.let { FontFamily(Font(File(it),weight=FontWeight.Normal)) } }.getOrNull() ?: FontFamily(Font(R.font.inter_variable,weight=FontWeight.W900)) }
 val density=LocalDensity.current
 CompositionLocalProvider(LocalDensity provides Density(density.density,(density.fontScale*textScale).coerceIn(.75f,1.5f)),
  LocalTextStyle provides TextStyle(fontFamily=appFont,fontWeight=if(boldText) FontWeight.W900 else FontWeight.Normal)) {
  Box(Modifier.fillMaxSize()) {
   Box(Modifier.fillMaxSize().prismalGlassLayer(backdrop).background(background)) {
    Column(Modifier.fillMaxSize()) {
     Spacer(Modifier.height(48.dp))
     AnimatedContent(targetState=Triple(channelOpen,selectedTab,settingsOpen),transitionSpec={ fadeIn() togetherWith fadeOut() },label="graf-screen") { state ->
      when {
       state.first -> ChannelScreen(dark,foreground,secondary) { channelOpen=false }
       state.second==2 && state.third -> SettingsScreen(dark,foreground,secondary,textScale,boldText,
        onBack={settingsOpen=false},onThemeChange={dark=it;prefs.edit().putBoolean("dark_theme",it).apply()},
        onScaleChange={textScale=it;prefs.edit().putFloat("text_scale",it).apply()},
        onBoldChange={boldText=it;prefs.edit().putBoolean("bold_text",it).apply()},
        onFontPick={fontPicker.launch(arrayOf("font/ttf","application/x-font-ttf","application/octet-stream"))},
        onFontReset={customFontPath=null;prefs.edit().remove("font_path").apply()},hasCustomFont=customFontPath!=null)
       else -> when(GrafTab.entries[state.second]) {
        GrafTab.Home -> HomeScreen(dark,foreground,secondary) { channelOpen=true }
        GrafTab.Contact -> SimpleScreen("Contacts","Your people will appear here.",Icons.Rounded.Contacts,foreground,secondary)
        GrafTab.Settings -> ProfileScreen(dark,foreground,secondary,profilePhoto,
         onBack={selectedTab=0},onMore={settingsOpen=true},onPhoto={photoPicker.launch(arrayOf("image/*"))},
         onViewPhoto={if(profilePhoto!=null) photoViewer=true})
        GrafTab.Search -> SearchScreen(query,{query=it},dark,foreground,secondary) { channelOpen=true }
       }
      }
     }
    }
   }
   if(!channelOpen && !settingsOpen) {
    PrismalGlassBottomTabs(selectedTabIndex={selectedTab},onTabSelected={selectedTab=it},backdrop=backdrop,tabsCount=GrafTab.entries.size,tintDropletContent=false,
     modifier=Modifier.align(Alignment.BottomCenter).navigationBarsPadding().padding(horizontal=20.dp,vertical=8.dp).fillMaxWidth().clip(androidx.compose.foundation.shape.RoundedCornerShape(40.dp)).background(if(dark) Color.White.copy(alpha=.07f) else Color.White.copy(alpha=.58f)).border(1.dp,if(dark) Color.White.copy(alpha=.20f) else Color.White.copy(alpha=.78f),androidx.compose.foundation.shape.RoundedCornerShape(40.dp))) {
      GrafTab.entries.forEachIndexed { index,tab -> GrafBottomTab(index,tab,selectedTab,dark) { selectedTab=index } }
    }
   }
   if(photoViewer && profilePhoto!=null) ProfilePhotoViewer(profilePhoto!!,dark) { photoViewer=false }
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
            Text(title, color = foreground, fontSize = 32.sp, fontWeight = LocalTextStyle.current.fontWeight)
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
                    Text("Graf News", color = foreground, fontSize = 17.sp, fontWeight = LocalTextStyle.current.fontWeight)
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
        Text("CHANNELS", color = secondary, fontSize = 11.sp, fontWeight = LocalTextStyle.current.fontWeight, letterSpacing = 1.5.sp,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp))
        GlassPanel(dark, Modifier.fillMaxWidth().padding(horizontal = 18.dp)) {
            Row(Modifier.fillMaxWidth().clickable(onClick = onOpenChannel).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                ChannelAvatar()
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("Graf — Official Channel", color = foreground, fontWeight = LocalTextStyle.current.fontWeight, fontSize = 15.sp)
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
private fun SettingsScreen(dark:Boolean,foreground:Color,secondary:Color,textScale:Float,boldText:Boolean,onBack:()->Unit,onThemeChange:(Boolean)->Unit,onScaleChange:(Float)->Unit,onBoldChange:(Boolean)->Unit,onFontPick:()->Unit,onFontReset:()->Unit,hasCustomFont:Boolean) {
 val fill=if(dark) Color.White.copy(alpha=.10f) else Color.White.copy(alpha=.48f)
 val stroke=if(dark) Color.White.copy(alpha=.24f) else Color.White.copy(alpha=.78f)
 Column(Modifier.fillMaxSize().padding(horizontal=18.dp)) {
  Row(Modifier.fillMaxWidth().padding(vertical=8.dp),verticalAlignment=Alignment.CenterVertically) {
   ProfileGlassIcon(Icons.Rounded.ArrowBack,dark,fill,Brush.linearGradient(listOf(stroke,stroke.copy(alpha=.3f))),onBack)
   Spacer(Modifier.width(14.dp)); Text("Настройки",color=foreground,fontSize=25.sp,fontWeight=FontWeight.W900)
  }
  SettingsGroup("ВНЕШНИЙ ВИД",foreground,secondary,fill,stroke) {
   SettingsRow(Icons.Rounded.LightMode,"Светлая тема","Белое оформление",foreground,secondary) { onThemeChange(false) }
   SettingsRow(Icons.Rounded.DarkMode,"Тёмная тема","Тёмное оформление",foreground,secondary) { onThemeChange(true) }
  }
  Spacer(Modifier.height(14.dp))
  SettingsGroup("ТЕКСТ И ШРИФТ",foreground,secondary,fill,stroke) {
   Column(Modifier.fillMaxWidth().padding(horizontal=16.dp,vertical=10.dp)) {
    Row(verticalAlignment=Alignment.CenterVertically) { GrafIcon(Icons.Rounded.FormatSize,"Размер",foreground,20.dp); Spacer(Modifier.width(12.dp)); Column(Modifier.weight(1f)) { Text("Размер символов",color=foreground,fontSize=15.sp); Text("${(textScale*100).toInt()}%",color=secondary,fontSize=12.sp) } }
    androidx.compose.material.Slider(value=textScale,onValueChange=onScaleChange,valueRange=.8f..1.35f)
   }
   SettingsRow(Icons.Rounded.FormatBold,"Жирный текст",if(boldText) "Включён" else "Выключен",foreground,secondary) { onBoldChange(!boldText) }
   SettingsRow(Icons.Rounded.CloudUpload,"Загрузить шрифт",if(hasCustomFont) "Свой TTF установлен" else "Выбрать файл .ttf",foreground,secondary,onFontPick)
   if(hasCustomFont) SettingsRow(Icons.Rounded.Close,"Сбросить шрифт","Вернуться к Inter",foreground,secondary,onFontReset)
  }
  Text("Поддерживаются файлы TTF. Шрифт хранится на устройстве.",color=secondary,fontSize=12.sp,modifier=Modifier.padding(12.dp))
 }
}
@Composable
private fun SettingsGroup(title:String,foreground:Color,secondary:Color,fill:Color,stroke:Color,content:@Composable ColumnScope.()->Unit) {
 Column(Modifier.fillMaxWidth()) { Text(title,color=secondary,fontSize=11.sp,letterSpacing=1.3.sp,modifier=Modifier.padding(start=12.dp,bottom=7.dp))
  Column(Modifier.fillMaxWidth().clip(androidx.compose.foundation.shape.RoundedCornerShape(25.dp)).background(fill).border(1.dp,stroke,androidx.compose.foundation.shape.RoundedCornerShape(25.dp)),content=content) }
}
@Composable
private fun SettingsRow(icon:ImageVector,title:String,subtitle:String,foreground:Color,secondary:Color,onClick:()->Unit) {
 Row(Modifier.fillMaxWidth().clickable(onClick=onClick).padding(horizontal=16.dp,vertical=13.dp),verticalAlignment=Alignment.CenterVertically) {
  GrafIcon(icon,title,foreground,21.dp); Spacer(Modifier.width(13.dp)); Column(Modifier.weight(1f)) { Text(title,color=foreground,fontSize=15.sp); Text(subtitle,color=secondary,fontSize=12.sp) }
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
private fun ProfileGlassIcon(icon:ImageVector,dark:Boolean,fill:Color,stroke:Brush,onClick:()->Unit) {
 Box(Modifier.size(50.dp).clip(CircleShape).background(fill).border(1.dp,stroke,CircleShape).clickable(onClick=onClick),contentAlignment=Alignment.Center) {
  GrafIcon(icon,null,if(dark) Color.White else Ink,22.dp)
 }
}
@Composable
private fun ProfileScreen(dark:Boolean,foreground:Color,secondary:Color,photo:Uri?,onBack:()->Unit,onMore:()->Unit,onPhoto:()->Unit,onViewPhoto:()->Unit) {
 val fill=if(dark) Color.White.copy(alpha=.12f) else Color.White.copy(alpha=.30f)
 val stroke=Brush.linearGradient(listOf(Color.White.copy(alpha=.86f),Color.White.copy(alpha=.20f),Color.White.copy(alpha=.65f)))
 Box(Modifier.fillMaxSize().offset(y=(-48).dp)) {
  Column(Modifier.fillMaxSize()) {
   Spacer(Modifier.height(184.dp))
   Column(Modifier.fillMaxWidth(),horizontalAlignment=Alignment.CenterHorizontally) {
    Text("Alicia Brown 💎",color=foreground,fontSize=25.sp,fontWeight=FontWeight.W900)
    Spacer(Modifier.height(4.dp)); Text("last seen just now",color=secondary,fontSize=15.sp,fontWeight=FontWeight.Normal)
    Spacer(Modifier.height(18.dp))
    Row(Modifier.clip(CircleShape).background(fill).border(1.dp,stroke,CircleShape).padding(horizontal=15.dp,vertical=6.dp),verticalAlignment=Alignment.CenterVertically) {
     GrafIcon(Icons.Rounded.MusicNote,"Music",foreground,15.dp); Spacer(Modifier.width(7.dp)); Text("Stayin' Alive - Bee Gees",color=foreground,fontSize=12.sp,fontWeight=FontWeight.Normal)
    }
   }
   Spacer(Modifier.height(26.dp))
   Row(Modifier.fillMaxWidth().padding(horizontal=24.dp),horizontalArrangement=Arrangement.SpaceEvenly) {
    ProfileAction(Icons.Rounded.Call,"call",dark,fill,stroke,secondary) {}
    ProfileAction(Icons.Rounded.Notifications,"mute",dark,fill,stroke,secondary) {}
    ProfileAction(Icons.Rounded.Search,"search",dark,fill,stroke,secondary) {}
    ProfileAction(Icons.Rounded.MoreHoriz,"more",dark,fill,stroke,secondary,onMore)
   }
  }
  Row(Modifier.fillMaxWidth().padding(horizontal=16.dp,vertical=16.dp),verticalAlignment=Alignment.CenterVertically) {
   ProfileGlassIcon(Icons.Rounded.ArrowBack,dark,fill,stroke,onBack); Spacer(Modifier.weight(1f))
   Row(Modifier.height(50.dp).clip(CircleShape).background(fill).border(1.dp,stroke,CircleShape).clickable { }.padding(horizontal=18.dp),verticalAlignment=Alignment.CenterVertically) {
    Text("Edit",color=foreground,fontSize=16.sp,fontWeight=FontWeight.Normal)
   }
  }
  Box(Modifier.align(Alignment.TopCenter).padding(top=29.dp).size(138.dp).clip(CircleShape)
   .background(Brush.linearGradient(listOf(Color(0xFF69C9D0),Color(0xFF477EB5),Color(0xFFC8B5A0)))).border(2.dp,stroke,CircleShape)
   .combinedClickable(onClick=onViewPhoto,onLongClick=onPhoto),contentAlignment=Alignment.Center) {
    if(photo!=null) ProfilePhotoImage(photo,Modifier.fillMaxSize()) else GrafIcon(Icons.Rounded.Person,"Profile photo",Color.White,70.dp)
  }
 }
}
@Composable
private fun ProfileAction(icon:ImageVector,label:String,dark:Boolean,fill:Color,stroke:Brush,secondary:Color,onClick:()->Unit) {
 Column(Modifier.width(72.dp),horizontalAlignment=Alignment.CenterHorizontally) {
  Box(Modifier.size(64.dp).clip(CircleShape).background(fill).border(1.dp,stroke,CircleShape).clickable(onClick=onClick),contentAlignment=Alignment.Center) { GrafIcon(icon,label,Color.White,27.dp) }
  Spacer(Modifier.height(7.dp)); Text(label,color=secondary,fontSize=12.sp,fontWeight=FontWeight.Normal,textAlign=TextAlign.Center)
 }
}
@Composable
private fun ProfilePhotoImage(uri:Uri,modifier:Modifier=Modifier) {
 val context=LocalContext.current
 val bitmap=remember(uri) { runCatching { context.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it)?.asImageBitmap() } }.getOrNull() }
 if(bitmap!=null) Image(bitmap,contentDescription="Profile photo",modifier=modifier)
}
@Composable
private fun ProfilePhotoViewer(uri:Uri,dark:Boolean,onClose:()->Unit) {
 var drag by remember { mutableFloatStateOf(0f) }
 Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha=.97f)).pointerInput(Unit) {
  detectVerticalDragGestures(onVerticalDrag={_,amount->drag+=amount},onDragEnd={if(drag>90f) onClose();drag=0f})
 },contentAlignment=Alignment.Center) {
  ProfilePhotoImage(uri,Modifier.fillMaxWidth().aspectRatio(1f))
  Box(Modifier.align(Alignment.TopEnd).padding(22.dp).size(44.dp).clip(CircleShape).background(Color.White.copy(alpha=.18f)).clickable(onClick=onClose),contentAlignment=Alignment.Center) { GrafIcon(Icons.Rounded.Close,"Close",Color.White,22.dp) }
 }
}
