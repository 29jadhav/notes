package com.vivek.notes.features.home_feature.presentation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import androidx.hilt.navigation.compose.hiltViewModel
import com.vivek.notes.R
import com.vivek.notes.features.home_feature.event.HomeAction
import com.vivek.notes.features.home_feature.event.HomeEvent
import kotlinx.coroutines.flow.collectLatest

private const val DEFAULT_NOTE_ID = -1

@Composable
fun HomeScreen(
    onAddNoteClick: (Int) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val fabContentDescription = stringResource(R.string.add_new_note)
    LaunchedEffect(key1 = true) {
        homeViewModel.event.collectLatest {
            when (it) {
                is HomeEvent.OnItemClick -> {
                    onAddNoteClick(it.id)
                }
            }
        }

    }

    Scaffold(topBar = {
        Row(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp)) {
            Text(
                text = stringResource(R.string.notes_title),
                style = MaterialTheme.typography.displayMedium,
                color =
                MaterialTheme.colorScheme.primary
            )
        }
    }, floatingActionButton = {
        FloatingActionButton(modifier = Modifier.semantics {
            contentDescription = fabContentDescription
        }, onClick = { onAddNoteClick(DEFAULT_NOTE_ID) }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }) {
        Box(
            modifier = Modifier
                .padding(it)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            LazyColumn {
                items(homeViewModel.notesList.size) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary, shape =
                                RoundedCornerShape(12.dp)
                            )
                            .clip(shape = RoundedCornerShape(12.dp))
                            .clickable {
                                homeViewModel.action(HomeAction.OnNoteItemClick(it))
                            }
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Text(
                            text = homeViewModel.notesList[it].title,
                            style = MaterialTheme.typography
                                .titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = homeViewModel.notesList[it].description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    //HomeScreen()
}

@Preview(
    name = "Dark mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun HomeScreenPreviewDarkMode() {
    //HomeScreen()
}