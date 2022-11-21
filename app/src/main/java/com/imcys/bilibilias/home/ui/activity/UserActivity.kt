package com.imcys.bilibilias.home.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.imcys.bilibilias.R
import com.imcys.bilibilias.home.ui.activity.ui.theme.BILIBILIASTheme
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX.statusBarOnly

class UserActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val msgMutableList = mutableListOf<Message>()
        msgMutableList.add(Message("xxx",
            "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"))

        msgMutableList.add(Message("xxx", "xxxx"))
        msgMutableList.add(Message("xxx", "xxxx"))


        setContent {
            BILIBILIASTheme {
                // A surface container using the 'background' color from the theme
                Surface(Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {

                    Scaffold(
                        topBar = {
                            TopAppBar(title = {
                                Text(text = "d11")
                            })
                        }

                    ) {

                        Column {
                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)) {
                                Conversation(msgMutableList)
                            }

                        }


                    }
                }
            }
        }

    }


}

data class Message(val author: String, val body: String)


@Composable
fun Conversation(messages: List<Message>) {
    LazyColumn {
        messages.map { item { MessageCard(it) } }
    }
}


@Composable
fun MessageCard(msg: Message) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "测速",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))
        var isExpanded by remember { mutableStateOf(false) }



        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {

            Text(text = msg.author,
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.subtitle1
            )
            Spacer(modifier = Modifier.height(4.dp))


            Surface(shape = MaterialTheme.shapes.medium, elevation = 1.dp)
            {
                Text(
                    text = msg.body,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                )

            }

        }
    }


}


@Preview
@Composable
fun PreviewMessageCard() {
    BILIBILIASTheme {
        Surface() {
            MessageCard(
                msg = Message("Android", "Jetpack Compose")
            )
        }
    }

}


