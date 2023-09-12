package com.imcys.bilibilias.ui.tool

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.components.FullScreenScaffold

@Composable
fun Tool() {
    FullScreenScaffold(
        Modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize(),
        topBar = {
            SearchBar()
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            VideoCard()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoCard() {
    Card(onClick = { /*TODO*/ }, Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth()) {
            Column(
                Modifier.weight(1f)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_home_trophy),
                    contentDescription = null,
                    Modifier
                        .height(100.dp)
                        .padding(5.dp)
                )
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    "hhhhhhhhhhhhhhhhhhhhh",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )
                Text(
                    "hhhhhhhhhhhhhhhhhhhhhhhhh",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 2
                )
                Row {
                    Image(
                        painter = painterResource(R.drawable.ic_play_num),
                        contentDescription = "play number",
                        Modifier.size(18.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                    )
                    Text(
                        text = "1000w",
                        Modifier.padding(start = 3.dp),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                    Image(
                        painter = painterResource(R.drawable.ic_danmaku_num),
                        contentDescription = "danmaku number",
                        Modifier
                            .size(18.dp)
                            .padding(start = 10.dp),
                        contentScale = ContentScale.Inside,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                    )
                    Text(
                        text = "1000w",
                        Modifier.padding(start = 3.dp),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ToolItem(
    @DrawableRes imageRes: Int,
    title: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primary
) {
    Card(
        onClick = { /*todo*/ },
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .size(96.dp)
            .padding()
    ) {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = null,
                Modifier
                    .size(35.dp)
                    .padding(top = 10.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )
            Text(
                text = title,
                Modifier
                    .padding(top = 10.dp),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SearchBar(modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxWidth()
            .background(Color(0XFFEDEFF2))
            .clip(RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp)),
    ) {
        Text(
            stringResource(R.string.app_fragment_tool_title),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        var query by remember { mutableStateOf("") }
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            leadingIcon = {
                Image(
                    painter = painterResource(R.drawable.ic_tool_search),
                    contentDescription = "search"
                )
            },
            trailingIcon = {
                if (query.isNotBlank()) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "clear text",
                        modifier = Modifier.clickable { query = "" }
                    )
                }
            },
            placeholder = { Text(text = stringResource(R.string.app_fragment_tool_input_tip)) },
            singleLine = true,
        )
    }
}
