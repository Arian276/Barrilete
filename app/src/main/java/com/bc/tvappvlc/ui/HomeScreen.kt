package com.bc.tvappvlc.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.gson.Gson
    import com.google.gson.reflect.TypeToken
import com.bc.tvappvlc.R
import com.bc.tvappvlc.model.Channel
import com.bc.tvappvlc.ui.theme.BadgeBg
import com.bc.tvappvlc.ui.theme.OnBadge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen() {
    val context = LocalContext.current
    var channels by remember { mutableStateOf<List<Channel>>(emptyList()) }

    LaunchedEffect(Unit) {
        channels = loadChannels(context)
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Barrilete Cósmico") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(UiTokens.spaceMd),
            verticalArrangement = Arrangement.spacedBy(UiTokens.spaceMd),
            horizontalArrangement = Arrangement.spacedBy(UiTokens.spaceMd),
            modifier = Modifier.padding(paddingValues)
        ) {
            items(channels) { channel ->
                ChannelCard(channel = channel) {
                    // Navegar al reproductor
                    val intent = Intent(context, com.bc.tvappvlc.ui.PlayerActivity::class.java)
                    intent.putExtra("url", channel.url)
                    intent.putExtra("title", channel.name)
                    context.startActivity(intent)
                }
            }
        }
    }
}

private suspend fun loadChannels(context: Context): List<Channel> {
    return withContext(Dispatchers.IO) {
        val assetManager = context.assets
        val input = assetManager.open("channels.json")
        val reader = InputStreamReader(input)
        val type = object : TypeToken<List<Channel>>() {}.type
        Gson().fromJson<List<Channel>>(reader, type)
    }
}