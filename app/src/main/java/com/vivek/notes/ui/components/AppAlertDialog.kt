package com.vivek.notes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.vivek.notes.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppAlertDialog(
    modifier: Modifier = Modifier,
    showDialog: Boolean = true,
    onDismissClick: () -> Unit,
    onConfirmClick: () -> Unit
) {
    if (showDialog)
        BasicAlertDialog(
            onDismissRequest = { onDismissClick() },
            modifier = modifier,
            properties = DialogProperties
                (dismissOnClickOutside = true)
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = stringResource(R.string.alert), style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.padding(12.dp))
                    Text(text = stringResource(R.string.delete_note_body))
                    Spacer(modifier = Modifier.padding(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = onDismissClick, Modifier
                                .padding(12.dp)
                                .background(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.background
                                )
                                .clip(shape = RoundedCornerShape(8.dp))
                        ) {
                            Text(stringResource(R.string.no))
                        }
                        TextButton(
                            onClick = onConfirmClick, Modifier
                                .padding(12.dp)
                                .background(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.inversePrimary
                                )
                                .clip(shape = RoundedCornerShape(8.dp))
                        ) {
                            Text(stringResource(R.string.yes))
                        }
                    }
                }
            }
        }
}

@Composable
@Preview
fun AppAlertDialogPreview() {
    AppAlertDialog(onConfirmClick = {}, onDismissClick = {})
}