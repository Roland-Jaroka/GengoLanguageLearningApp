package com.example.gengolearning.ui.features.dashboard.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.Red
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R

@Composable
fun SyncCard(modifier: Modifier = Modifier, isSynced: Boolean = false, onSyncedInfo: () -> Unit = {},
             onNotSyncedInfo: () -> Unit = {}, isLoading: Boolean = false) {

    val compositionWifi by rememberLottieComposition( spec =
        LottieCompositionSpec.RawRes(R.raw.wifi)

    )
    val compositionNoInternet by rememberLottieComposition( spec =
         LottieCompositionSpec.RawRes(R.raw.no_internet)
    )

    val compositionLoading by rememberLottieComposition( spec =
        LottieCompositionSpec.RawRes(R.raw.syncdata)
    )

    val progressWifi by animateLottieCompositionAsState(
        compositionWifi
    )
    val progressNoInternet by animateLottieCompositionAsState(
        compositionNoInternet
    )

    val progressLoading by animateLottieCompositionAsState(
        compositionLoading,
        iterations = LottieConstants.IterateForever
    )

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            5.dp
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp)
    ) {
      AnimatedVisibility(
          visible = isLoading
      ) {
          LottieAnimation(
              composition = compositionLoading,
              progress = {
                  progressLoading
              },
              modifier = Modifier
                  .fillMaxWidth()
                  .size(50.dp)
          )
      }

        AnimatedVisibility(
            visible = !isLoading
        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LottieAnimation(
                composition = if (isSynced) compositionWifi else compositionNoInternet,
                progress = {
                    if (isSynced) progressWifi else progressNoInternet
                },
                modifier = Modifier
                    .size(50.dp)
            )
            Text(
                text = if (isSynced) stringResource(R.string.synced_card_text)
                else stringResource(R.string.synced_card_error_text),
                modifier = Modifier
                    .padding(start = 5.dp)
                    .weight(1f),
                color = if (isSynced) Blue else Red
            )

            IconButton(
                onClick = {
                    if (isSynced) {
                        onSyncedInfo()
                    } else {
                        onNotSyncedInfo()
                    }
                },
            ) {
                if (isSynced)
                Image(
                    painter = painterResource(R.drawable.infoicon),
                    contentDescription = "Info",
                    modifier = Modifier
                        .size(20.dp)
                ) else
                    Image(painter = painterResource(R.drawable.restarticon),
                        contentDescription = "Reload",
                        modifier = Modifier
                            .size(20.dp))
            }
        }
          }
    }
}

@Preview
@Composable
private fun Preview() {
    SyncCard(
        isSynced = true,
        isLoading = true
    )
}