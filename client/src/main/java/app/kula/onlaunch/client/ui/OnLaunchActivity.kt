package app.kula.onlaunch.client.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.kula.onlaunch.client.OnLaunch
import app.kula.onlaunch.client.data.models.Action
import app.kula.onlaunch.client.data.models.Message

@SuppressLint("CustomSplashScreen")
class OnLaunchActivity : ComponentActivity() {
    companion object {
        const val EXTRA_MESSAGE = "onlaunch_message"
    }

    private lateinit var message: Message

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        actionBar?.hide()

        message = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getParcelable(EXTRA_MESSAGE, Message::class.java)
        } else {
            intent.extras?.getParcelable(EXTRA_MESSAGE)
        } ?: run {
            Log.e("OnLaunchActivity", "Extra $EXTRA_MESSAGE not set")
            finish()
            return@onCreate
        }

        setContent {
            MaterialTheme {
                Message(
                    message = message,
                    dismiss = ::dismiss,
                    openInAppStore = ::openInAppStore,
                )
            }
        }
    }

    private fun dismiss() {
        OnLaunch.markMessageDismissed(messageId = message.id)
        finish()
    }

    private fun openInAppStore() {
        OnLaunch.openAppStore(this)
    }
}

@Composable
private fun Message(
    message: Message,
    dismiss: () -> Unit,
    openInAppStore: () -> Unit,
) {
    // Block back gesture if message is blocking
    BackHandler {
        if (!message.isBlocking) {
            dismiss()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (!message.isBlocking) {
                IconButton(onClick = dismiss) {
                    Image(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = android.R.string.cancel),
                    )
                }
            }
        }

        Text(
            text = message.title,
            style = MaterialTheme.typography.h4,
        )
        Spacer(modifier = Modifier.size(20.dp))
        Text(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(state = rememberScrollState()),
            text = message.body,
            style = MaterialTheme.typography.body1,
        )

        message.actions.forEachIndexed { index, action ->
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                onClick = when (action.actionType) {
                    Action.Type.DISMISS -> dismiss
                    Action.Type.OPEN_APP_IN_APP_STORE -> openInAppStore
                },
            ) {
                Text(
                    text = action.title,
                )
            }
            if (index != message.actions.lastIndex) {
                Spacer(modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    Message(
        message = Message(
            id = 1,
            title = "Freezing the energy consumption",
            body = "What would George do?\n\nWhen the energy prices are heating up it’s time to cool down and reduce the waste of energy and financial health. While that seems hard, start with the easy wins: for example, when did you defrost your freezer the last time? And by the way, for your normal fridge, 4 degree Celsius are cool enough. Each degree less means 5% more energy.",
            isBlocking = false,
            actions = listOf(
                Action(actionType = Action.Type.DISMISS, title = "Thanks George!"),
            )
        ),
        dismiss = {},
        openInAppStore = {},
    )
}
