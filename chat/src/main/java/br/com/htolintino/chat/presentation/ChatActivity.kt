package br.com.htolintino.chat.presentation

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.htolintino.chat.R
import br.com.htolintino.chat.domain.Message
import br.com.htolintino.mycomposetutorial.ui.theme.MyComposeTutorialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

class ChatActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyComposeTutorialTheme {
                MainLayout()
            }
        }
    }
}

@Composable
internal fun MainLayout(viewModel: ChatViewModel = viewModel()) {
    Column(
        modifier = Modifier.padding(PaddingValues(bottom = 80.dp))
    ) {
        Conversation(viewModel)
    }

    Column {
        Spacer(modifier = Modifier.weight(1f))
        var textFieldValue by remember { mutableStateOf(String()) }
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = textFieldValue,
                label = {
                    Text(text = stringResource(id = R.string.type_your_message_hint))
                },
                onValueChange = { value -> textFieldValue = value },
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            )
            Button(
                onClick = {
                    viewModel.onSendMessageClick(textFieldValue)
                    textFieldValue = String()
                },
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .padding(8.dp)
            ) {
                Text(text = stringResource(id = R.string.send))
            }
        }
    }
}

@Composable
internal fun Conversation(viewModel: ChatViewModel) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val messages = viewModel.messages.observeAsState()

    messages.value?.let { list ->
        LazyColumn(state = listState) {
            items(list) { message -> MessageCard(message = message) }
        }
        coroutineScope.launch { listState.animateScrollToItem(list.size) }
    }
}

@Composable
fun MessageCard(message: Message) {
    Row(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.avatar),
            contentDescription = String(),
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        var isExpanded by remember { mutableStateOf(false) }
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
        )

        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(
                text = message.author,
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.subtitle2
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                elevation = 1.dp,
                color = surfaceColor,
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
            ) {
                Text(
                    text = message.body,
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewMessageCard() {
    MyComposeTutorialTheme {
        MainLayout()
    }
}