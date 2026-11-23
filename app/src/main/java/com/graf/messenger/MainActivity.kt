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
private fun grafGlassRim() = PrismalSpecular.Default.copy(
    width = 1.dp,
    blurRadius = 0.6.dp,
    style = PrismalSpecularStyle.Default(color = Color.White.copy(alpha = 0.78f))
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

private enum class GrafTab(val title: String, val icon: ImageVector) {
    Home("Posts", Icons.Rounded.Home),
    Contact("Contacts", Icons.Rounded.Contacts),
    Search("Search", Icons.Rounded.Search),
    Settings("Settings", Icons.Rounded.Settings)
}

@Composable
private fun GrafApp() {
 val context=LocalContext.current
 val prefs=remember { context.getSharedPreferences("graf_prefs",0) }
 val dark = true
 var selectedTab by remember { mutableIntStateOf(0) }
 var bottomBarVisible by remember { mutableStateOf(true) }
 var settingsOpen by remember { mutableStateOf(false) }
 var profileName by remember { mutableStateOf(prefs.getString("profile_name","Alicia Brown") ?: "Alicia Brown") }
 var profileUsername by remember { mutableStateOf(prefs.getString("profile_username","username") ?: "username") }
 var profilePhone by remember { mutableStateOf(prefs.getString("profile_phone","+1488 1234 8922") ?: "+1488 1234 8922") }
 var profileAddress by remember { mutableStateOf(prefs.getString("profile_address","0-27-14") ?: "0-27-14") }
 var profileBirthday by remember { mutableStateOf(prefs.getString("profile_birthday","") ?: "") }
 var profileChannels by remember { mutableStateOf(prefs.getString("profile_channels","") ?: "") }
 var channelOpen by remember { mutableStateOf(false) }
 val activity = context as? ComponentActivity
 SideEffect {
  // Draw the app background behind system bars, but keep all interactive content
  // below the status bar on every screen. Never hide the status bar on the profile.
  activity?.window?.decorView?.systemUiVisibility =
   android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
    android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
    android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
    android.view.View.SYSTEM_UI_FLAG_FULLSCREEN or
    android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
    android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
 }

 var photoViewer by remember { mutableStateOf(false) }
 var query by remember { mutableStateOf("") }
 var profilePhoto by remember { mutableStateOf(prefs.getString("profile_photo",null)?.let(Uri::parse)) }
 var profileColor by remember { mutableIntStateOf(prefs.getInt("profile_color",0xFF8994A5.toInt())) }
 var customFontPath by remember { mutableStateOf(prefs.getString("font_path",null)) }
 var textScale by remember { mutableFloatStateOf(prefs.getFloat("text_scale",1f)) }
 var boldText by remember { mutableStateOf(prefs.getBoolean("bold_text",true)) }
 var iconScale by remember { mutableFloatStateOf(prefs.getFloat("icon_scale",1f)) }
 var iconBold by remember { mutableStateOf(prefs.getBoolean("icon_bold",false)) }
 val backdrop=rememberPrismalGlassLayer()
 val photoPicker=rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
  if(uri!=null) { runCatching { context.contentResolver.takePersistableUriPermission(uri,android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION) }; profilePhoto=uri; prefs.edit().putString("profile_photo",uri.toString()).apply() }
 }
 val fontPicker=rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
  if(uri!=null) runCatching { context.contentResolver.openInputStream(uri)?.use { input -> val file=File(context.filesDir,"graf_custom_font.ttf"); FileOutputStream(file).use { input.copyTo(it) }; customFontPath=file.absolutePath; prefs.edit().putString("font_path",file.absolutePath).apply() } }
 }
 val background=if(dark) Brush.verticalGradient(listOf(Color(0xFF000000),Color(0xFF050505),Color(0xFF000000))) else Brush.verticalGradient(listOf(Color(0xFFF7F7F8),Color(0xFFE9EAED)))
 val foreground=if(dark) Color(0xFFF5F7FC) else Color(0xFF171B24)
 val secondary=if(dark) Color(0xFFB5C0D0) else Color(0xFF596273)
 val appFont=remember(customFontPath) { runCatching { customFontPath?.let { FontFamily(Font(File(it),weight=FontWeight.Normal)) } }.getOrNull() ?: FontFamily(Font(R.font.inter_variable,weight=FontWeight.W900)) }
 val density=LocalDensity.current
 val themedConfiguration = Configuration(LocalConfiguration.current).apply {
  uiMode = (uiMode and Configuration.UI_MODE_NIGHT_MASK.inv()) or
   (if (dark) Configuration.UI_MODE_NIGHT_YES else Configuration.UI_MODE_NIGHT_NO)
 }
 CompositionLocalProvider(LocalConfiguration provides themedConfiguration,
  LocalDensity provides Density(density.density,(density.fontScale*textScale).coerceIn(.75f,1.5f)),
  LocalTextStyle provides TextStyle(fontFamily=appFont,fontWeight=if(boldText) FontWeight.W900 else FontWeight.Normal),
  LocalGrafIconScale provides iconScale,
  LocalGrafIconBold provides iconBold) {
  Box(Modifier.fillMaxSize()) {
   Box(Modifier.fillMaxSize().prismalGlassLayer(backdrop).background(background)) {
    Column(Modifier.fillMaxSize().padding(top = 24.dp)) {
     AnimatedContent(targetState=Pair(channelOpen,selectedTab),transitionSpec={ fadeIn() togetherWith fadeOut() },label="graf-screen") { state ->
      when {
       state.first -> ChannelScreen(dark,foreground,secondary) { channelOpen=false }
       state.second==3 && settingsOpen -> SettingsScreen(dark,foreground,secondary,textScale,boldText,iconScale,iconBold,
        onBack={settingsOpen=false},onProfileClick={settingsOpen=false},
        onScaleChange={textScale=it;prefs.edit().putFloat("text_scale",it).apply()},
        onBoldChange={boldText=it;prefs.edit().putBoolean("bold_text",it).apply()},
        onIconScaleChange={iconScale=it;prefs.edit().putFloat("icon_scale",it).apply()},
        onIconBoldChange={iconBold=it;prefs.edit().putBoolean("icon_bold",it).apply()},
        onFontPick={fontPicker.launch(arrayOf("font/ttf","application/x-font-ttf","application/octet-stream"))},
        onFontReset={customFontPath=null;prefs.edit().remove("font_path").apply()},hasCustomFont=customFontPath!=null)
       else -> when(GrafTab.entries[state.second]) {
        GrafTab.Home -> PostsScreen(dark,foreground,secondary,{ bottomBarVisible=it })
        GrafTab.Contact -> ContactsScreen(dark,foreground,secondary)
        GrafTab.Search -> SearchScreen(query,{query=it},dark,foreground,secondary) { channelOpen=false }
        GrafTab.Settings -> ProfileScreen(dark,foreground,secondary,profilePhoto,
         onBack={selectedTab=0},onOpenSettings={settingsOpen=true},onPhoto={photoPicker.launch(arrayOf("image/*"))},
         onViewPhoto={if(profilePhoto!=null) photoViewer=true else photoPicker.launch(arrayOf("image/*"))},
         profileColor=Color(profileColor),onProfileColorChange={profileColor=it.toArgb();prefs.edit().putInt("profile_color",profileColor).apply()},
         profileName=profileName,profileUsername=profileUsername,profilePhone=profilePhone,
         profileAddress=profileAddress,profileBirthday=profileBirthday,profileChannels=profileChannels,
         onSaveProfile={name,username,phone,address,birthday,channels ->
          profileName=name;profileUsername=username;profilePhone=phone;profileAddress=address;profileBirthday=birthday;profileChannels=channels
          prefs.edit().putString("profile_name",name).putString("profile_username",username).putString("profile_phone",phone)
           .putString("profile_address",address).putString("profile_birthday",birthday).putString("profile_channels",channels).apply()})
       }
      }
     }
    }
   }
   if(!channelOpen) {
    AnimatedVisibility(visible=bottomBarVisible || selectedTab!=0 || settingsOpen,modifier=Modifier.align(Alignment.BottomCenter),
     enter=androidx.compose.animation.slideInVertically(initialOffsetY={it})+fadeIn(),
     exit=androidx.compose.animation.slideOutVertically(targetOffsetY={it})+fadeOut()) {
     PrismalGlassBottomTabs(selectedTabIndex={selectedTab},onTabSelected={selectedTab=it;settingsOpen=false;bottomBarVisible=true},backdrop=backdrop,tabsCount=GrafTab.entries.size,tintDropletContent=false,
      modifier=Modifier.padding(horizontal=20.dp,vertical=10.dp).fillMaxWidth()) {
      GrafTab.entries.forEachIndexed { index,tab -> GrafBottomTab(index,tab,selectedTab,dark) { selectedTab=index;settingsOpen=false;bottomBarVisible=true } }
     }
    }
   }
   if(photoViewer && profilePhoto!=null) ProfilePhotoViewer(profilePhoto!!,dark) { photoViewer=false }
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
        else GrafIcon(tab.icon, tab.title, tabTint, 22.dp)
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
        modifier = Modifier.size(size * scale * if (bold) 1.035f else 1f)
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

@Composable
private fun PostsScreen(dark:Boolean, foreground:Color, secondary:Color, onScrollChrome:(Boolean)->Unit) {
 data class Post(val name:String,val handle:String,val time:String,val body:String,val likes:Int,val replies:Int,val photo:String?=null)
 data class Chat(val name:String,val handle:String,val preview:String,val time:String,val unread:Int)
 val posts=listOf(
  Post("Graf","@graf_app","2 мин","Добро пожаловать в Graf — место для коротких мыслей, красивых кадров и общения.",128,24,"https://images.unsplash.com/photo-1470252649378-9c29740c9fa8?w=1200&auto=format&fit=crop&q=85"),
  Post("Алиса","@alicia","18 мин","Утро, любимый кофе и немного времени для себя ☕",42,8,"https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=1200&auto=format&fit=crop&q=85"),
  Post("Технологии","@tech_daily","34 мин","Новые идеи становятся реальностью, когда над ними работают каждый день.",76,13),
  Post("Музыка дня","@music_now","1 ч","Какой трек у вас сегодня на повторе? 🎧",31,19,"https://images.unsplash.com/photo-1511379938547-c1f69419868d?w=1200&auto=format&fit=crop&q=85"),
  Post("Путешествия","@go_explore","2 ч","Небольшая прогулка по незнакомому городу иногда запоминается сильнее большого путешествия.",93,6,"https://images.unsplash.com/photo-1470770841072-f978cf4d019e?w=1200&auto=format&fit=crop&q=85"),
  Post("Дизайн","@pixel_notes","3 ч","Хороший интерфейс не мешает — он помогает сосредоточиться на главном.",58,11),
  Post("Кино","@frame_by_frame","4 ч","Вечер для хорошего фильма 🍿",27,4,"https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=1200&auto=format&fit=crop&q=85"),
  Post("Наука","@curious_mind","5 ч","Любопытство — отличный повод задать ещё один вопрос.",64,9),
  Post("Спорт","@daily_motion","6 ч","Главное — регулярность и удовольствие от процесса.",49,7,"https://images.unsplash.com/photo-1530549387789-4c1017266635?w=1200&auto=format&fit=crop&q=85")
 )
 val chats=listOf(Chat("Алиса Brown","@alicia","Отправила тебе фото ✨","12:42",2),Chat("Graf Team","@graf_team","Новый дизайн уже готов","11:18",1),Chat("Максим","@max_dev","Давай обсудим идею","Вчера",0),Chat("Дизайн / идеи","@design_room","Артём: выглядит отлично!","Пн",0))
 var mode by remember { mutableStateOf("Grafs") }; var menu by remember { mutableStateOf(false) }
 var activePost by remember { mutableStateOf<Post?>(null) }; var comment by remember { mutableStateOf("") }
 var attachment by remember { mutableStateOf<Uri?>(null) }; var comments by remember { mutableStateOf(mapOf<String,List<String>>()) }
 var likes by remember { mutableStateOf(setOf<String>()) }; var activeChat by remember { mutableStateOf<Chat?>(null) }
 var draft by remember { mutableStateOf("") }; var messages by remember { mutableStateOf(mapOf<String,List<String>>()) }
 val listState= rememberLazyListState(); val context=LocalContext.current
 val picker=rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri -> if(uri!=null){ runCatching { context.contentResolver.takePersistableUriPermission(uri,android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION) }; attachment=uri } }
 LaunchedEffect(listState,mode,activeChat) {
  var oldIndex=0; var oldOffset=0
  kotlinx.coroutines.flow.snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }.collect { (index,offset) ->
   if(index>oldIndex || (index==oldIndex && offset>oldOffset)) onScrollChrome(index==0 && offset<36)
   if(index<oldIndex || (index==oldIndex && offset<oldOffset)) onScrollChrome(true)
   oldIndex=index; oldOffset=offset
  }
 }
 GlasmioTheme(isDark=dark) {
  GlasmioBackdropHost(modifier=Modifier.fillMaxSize(),sampledContent={
   Box(Modifier.fillMaxSize().background(Color(0xFF050608))) {
    when {
     activeChat!=null -> {
      val chat=activeChat!!; val msgs=messages[chat.handle].orEmpty()
      Column(Modifier.fillMaxSize()) {
       LazyColumn(Modifier.weight(1f).fillMaxWidth(),contentPadding=PaddingValues(top=70.dp,start=16.dp,end=16.dp,bottom=16.dp),verticalArrangement=Arrangement.spacedBy(9.dp)) {
        items(msgs) { msg -> Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.End) {
         Box(Modifier.widthIn(max=290.dp).clip(androidx.compose.foundation.shape.RoundedCornerShape(18.dp,18.dp,5.dp,18.dp)).background(Color(0xFF183D62)).padding(12.dp)){ Text(msg,color=Color.White,fontSize=14.sp) }
        } }
        if(msgs.isEmpty()) item { Text("Начните общение — отправьте первое сообщение.",color=secondary,fontSize=13.sp,modifier=Modifier.fillMaxWidth().padding(top=45.dp),textAlign=TextAlign.Center) }
       }
       Row(Modifier.fillMaxWidth().padding(12.dp),verticalAlignment=Alignment.CenterVertically) {
        Box(Modifier.weight(1f).clip(androidx.compose.foundation.shape.RoundedCornerShape(22.dp)).background(Color(0xFF202329)).padding(13.dp)) {
         if(draft.isEmpty()) Text("Сообщение…",color=secondary,fontSize=14.sp)
         BasicTextField(value=draft,onValueChange={draft=it},textStyle=TextStyle(color=foreground,fontSize=14.sp),modifier=Modifier.fillMaxWidth())
        }
        Spacer(Modifier.width(8.dp))
        GlasmioIconButton(onClick={ if(draft.isNotBlank()){ messages=messages+(chat.handle to (messages[chat.handle].orEmpty()+draft.trim()));draft="" } },size=44.dp){ GrafIcon(Icons.Rounded.Send,"Отправить",foreground,19.dp) }
       }
      }
     }
     mode=="Grafs" -> LazyColumn(state=listState,modifier=Modifier.fillMaxSize(),contentPadding=PaddingValues(start=16.dp,end=16.dp,top=66.dp,bottom=116.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
      items(posts) { post ->
       val key=post.handle+post.time; val liked=key in likes; val postComments=comments[key].orEmpty()
       Column(Modifier.fillMaxWidth().clip(androidx.compose.foundation.shape.RoundedCornerShape(23.dp)).background(Color(0xFF17191D)).border(1.dp,Color.White.copy(alpha=.14f),androidx.compose.foundation.shape.RoundedCornerShape(23.dp)).padding(15.dp)) {
        Row(verticalAlignment=Alignment.CenterVertically) {
         Box(Modifier.size(40.dp).clip(CircleShape).background(Color(0xFF30343B)),contentAlignment=Alignment.Center){ Text(post.name.take(1),color=foreground,fontWeight=FontWeight.Bold) }
         Spacer(Modifier.width(10.dp)); Column(Modifier.weight(1f)){ Text(post.name,color=foreground,fontSize=14.sp,fontWeight=FontWeight.Bold);Text(post.handle,color=secondary,fontSize=11.sp) }
         Text(post.time,color=secondary,fontSize=11.sp); GrafIcon(Icons.Rounded.MoreHoriz,"Ещё",secondary,20.dp)
        }
        Spacer(Modifier.height(12.dp)); Text(post.body,color=foreground,fontSize=14.sp,lineHeight=20.sp)
        if(post.photo!=null){ Spacer(Modifier.height(12.dp));coil.compose.AsyncImage(model=post.photo,contentDescription="Фото публикации",contentScale=androidx.compose.ui.layout.ContentScale.Crop,modifier=Modifier.fillMaxWidth().height(205.dp).clip(androidx.compose.foundation.shape.RoundedCornerShape(17.dp))) }
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically) {
         Row(Modifier.clickable { likes=if(liked) likes-key else likes+key },verticalAlignment=Alignment.CenterVertically){ GrafIcon(if(liked) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,"Нравится",if(liked) Color(0xFFFF5577) else secondary,20.dp);Spacer(Modifier.width(6.dp));Text((post.likes+if(liked)1 else 0).toString(),color=secondary,fontSize=12.sp) }
         Spacer(Modifier.width(18.dp))
         Row(Modifier.clickable { activePost=post;comment="";attachment=null },verticalAlignment=Alignment.CenterVertically){ GrafIcon(Icons.Rounded.ChatBubbleOutline,"Комментарии",secondary,19.dp);Spacer(Modifier.width(6.dp));Text((post.replies+postComments.size).toString(),color=secondary,fontSize=12.sp) }
         Spacer(Modifier.weight(1f));GrafIcon(Icons.Rounded.Send,"Поделиться",secondary,19.dp)
        }
        if(postComments.isNotEmpty()){ Spacer(Modifier.height(7.dp));Text("Последний комментарий: ${postComments.last()}",color=secondary,fontSize=11.sp,maxLines=2,overflow=TextOverflow.Ellipsis) }
       }
      }
     }
     else -> LazyColumn(state=listState,modifier=Modifier.fillMaxSize(),contentPadding=PaddingValues(top=68.dp,bottom=100.dp)) {
      items(chats) { chat ->
       Row(Modifier.fillMaxWidth().clickable { activeChat=chat;draft="" }.padding(horizontal=18.dp,vertical=14.dp),verticalAlignment=Alignment.CenterVertically) {
        Box(Modifier.size(52.dp).clip(CircleShape).background(Color(0xFF242A33)),contentAlignment=Alignment.Center){ Text(chat.name.take(1),color=foreground,fontSize=19.sp,fontWeight=FontWeight.Bold) }
        Spacer(Modifier.width(12.dp));Column(Modifier.weight(1f)){Text(chat.name,color=foreground,fontSize=15.sp,fontWeight=FontWeight.SemiBold);Text(chat.preview,color=secondary,fontSize=12.sp,maxLines=1)}
        Column(horizontalAlignment=Alignment.End){Text(chat.time,color=secondary,fontSize=10.sp);if(chat.unread>0) Text(chat.unread.toString(),color=Blue,fontSize=11.sp) }
       }
       Box(Modifier.fillMaxWidth().padding(start=82.dp).height(.5.dp).background(Color.White.copy(alpha=.09f)))
      }
     }
    }
   }
  },overlayContent={
   Column(Modifier.fillMaxSize()) {
    GlassSurface(modifier=Modifier.fillMaxWidth().padding(horizontal=12.dp).height(56.dp),shape=androidx.compose.foundation.shape.RoundedCornerShape(20.dp)) {
     Box(Modifier.fillMaxSize(),contentAlignment=Alignment.Center) {
      if(activeChat!=null) GlasmioIconButton(onClick={activeChat=null},size=36.dp,modifier=Modifier.align(Alignment.CenterStart).padding(start=8.dp)){ GrafIcon(Icons.Rounded.ArrowBack,"Назад",foreground,20.dp) }
      GlasmioMenu(expanded=menu,onExpandedChange={menu=it},itemCount=2,centerHorizontally=true,forceBelowAnchor=true,menuWidth=224.dp,
       anchor={open->Text("GRAF",color=foreground,fontFamily=FontFamily.Serif,fontWeight=FontWeight.Bold,fontSize=25.sp,letterSpacing=4.sp,modifier=Modifier.clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp)).clickable(onClick=open).padding(horizontal=18.dp,vertical=7.dp))}
      ){ backdrop,close ->
       GlasmioMenuItem(title="Grafs",subtitle="Лента публикаций",onClick={mode="Grafs";activeChat=null;menu=false;close()},systemName="square.grid.2x2",backdrop=backdrop,enabled=true)
       GlasmioMenuItem(title="Chats",subtitle="Личные сообщения",onClick={mode="Chats";activeChat=null;menu=false;close()},systemName="bubble.left.and.bubble.right",backdrop=backdrop,enabled=true)
      }
     }
    }
    Spacer(Modifier.weight(1f))
   }
   if(activePost!=null) Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha=.65f)),contentAlignment=Alignment.Center) {
    GlassSurface(modifier=Modifier.fillMaxWidth().padding(horizontal=16.dp),shape=androidx.compose.foundation.shape.RoundedCornerShape(26.dp)) {
     Column(Modifier.fillMaxWidth().padding(16.dp)) {
      Row(verticalAlignment=Alignment.CenterVertically){Text("Комментарии",color=foreground,fontSize=19.sp,fontWeight=FontWeight.Bold,modifier=Modifier.weight(1f));GlasmioIconButton(onClick={activePost=null;attachment=null},size=36.dp){GrafIcon(Icons.Rounded.Close,"Закрыть",foreground,18.dp)}}
      Spacer(Modifier.height(9.dp));Text(activePost!!.body,color=secondary,fontSize=13.sp,maxLines=2,overflow=TextOverflow.Ellipsis)
      val key=activePost!!.handle+activePost!!.time;val list=comments[key].orEmpty()
      Column(Modifier.heightIn(max=180.dp).verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(7.dp)){
       if(list.isEmpty()) Text("Пока нет комментариев. Будь первым!",color=secondary,fontSize=12.sp,modifier=Modifier.padding(vertical=12.dp))
       list.forEach { msg -> Box(Modifier.fillMaxWidth().clip(androidx.compose.foundation.shape.RoundedCornerShape(14.dp)).background(Color.White.copy(alpha=.07f)).padding(11.dp)){Text(msg,color=foreground,fontSize=13.sp)} }
      }
      if(attachment!=null){Spacer(Modifier.height(8.dp));coil.compose.AsyncImage(model=attachment,contentDescription="Фото",modifier=Modifier.size(68.dp).clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp)),contentScale=androidx.compose.ui.layout.ContentScale.Crop)}
      Spacer(Modifier.height(9.dp))
      Row(horizontalArrangement=Arrangement.spacedBy(5.dp)){listOf("❤️","😂","🔥","✨","👍","🥰").forEach{ e->Text(e,Modifier.clip(androidx.compose.foundation.shape.RoundedCornerShape(10.dp)).background(Color.White.copy(alpha=.07f)).clickable{comment+=e}.padding(7.dp),fontSize=17.sp)}}
      Spacer(Modifier.height(9.dp))
      Row(verticalAlignment=Alignment.CenterVertically){
       GlasmioIconButton(onClick={picker.launch(arrayOf("image/*"))},size=38.dp){GrafIcon(Icons.Rounded.AddPhotoAlternate,"Добавить фото",secondary,19.dp)}
       Spacer(Modifier.width(7.dp))
       Box(Modifier.weight(1f).clip(androidx.compose.foundation.shape.RoundedCornerShape(17.dp)).background(Color.White.copy(alpha=.08f)).padding(horizontal=11.dp,vertical=12.dp)){
        if(comment.isEmpty())Text("Написать комментарий…",color=secondary,fontSize=12.sp)
        BasicTextField(value=comment,onValueChange={comment=it},textStyle=TextStyle(color=foreground,fontSize=13.sp),modifier=Modifier.fillMaxWidth())
       }
       Spacer(Modifier.width(7.dp))
       GlasmioIconButton(onClick={
        val msg=buildString{append(comment.trim());if(attachment!=null){if(isNotEmpty())append(" ");append("📷 Фото")}}
        if(msg.isNotBlank()){comments=comments+(key to (comments[key].orEmpty()+msg));comment="";attachment=null}
       },size=40.dp){GrafIcon(Icons.Rounded.Send,"Отправить",foreground,18.dp)}
      }
     }
    }
   }
  })
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
private fun SettingsScreen(dark:Boolean,foreground:Color,secondary:Color,textScale:Float,boldText:Boolean,iconScale:Float,iconBold:Boolean,onBack:()->Unit,onProfileClick:()->Unit,onScaleChange:(Float)->Unit,onBoldChange:(Boolean)->Unit,onIconScaleChange:(Float)->Unit,onIconBoldChange:(Boolean)->Unit,onFontPick:()->Unit,onFontReset:()->Unit,hasCustomFont:Boolean) {
 val fill=if(dark) Color.White.copy(alpha=.10f) else Color.White.copy(alpha=.48f)
 val stroke=if(dark) Color.White.copy(alpha=.24f) else Color.White.copy(alpha=.78f)
 Column(Modifier.fillMaxSize().padding(horizontal=18.dp)) {
  Row(Modifier.fillMaxWidth().padding(vertical=8.dp),verticalAlignment=Alignment.CenterVertically) {
   GlassIconButton(Icons.Rounded.ArrowBack,dark,onBack)
   Spacer(Modifier.width(14.dp)); Text("Настройки",color=foreground,fontSize=20.sp,fontWeight=FontWeight.W900)
  }
  SettingsGroup("АККАУНТ",foreground,secondary,fill,stroke) {
   SettingsRow(Icons.Rounded.Person,"Мой профиль","Фото и информация профиля",foreground,secondary,onProfileClick)
  }
  Spacer(Modifier.height(14.dp))
  SettingsGroup("ТЕКСТ И ШРИФТ",foreground,secondary,fill,stroke) {
   Column(Modifier.fillMaxWidth().padding(horizontal=16.dp,vertical=10.dp)) {
    Row(verticalAlignment=Alignment.CenterVertically) { GrafIcon(Icons.Rounded.FormatSize,"Размер",foreground,20.dp); Spacer(Modifier.width(12.dp)); Column(Modifier.weight(1f)) { Text("Размер текста",color=foreground,fontSize=15.sp); Text("${(textScale*100).toInt()}%",color=secondary,fontSize=12.sp) } }
    androidx.compose.material.Slider(value=textScale,onValueChange=onScaleChange,valueRange=.8f..1.35f)
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
    androidx.compose.material.Slider(value=iconScale,onValueChange=onIconScaleChange,valueRange=.75f..1.55f)
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
    val glassTone = Color(0xFF25272B)
    val page = Color.Black
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
