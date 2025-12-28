package com.mb.voiceassistantkmp.presentation.screen_patientDetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mb.voiceassistantkmp.presentation.utils.LocalVoiceHandler
import org.jetbrains.compose.resources.painterResource
import voiceassistantkmp.composeapp.generated.resources.Res
import voiceassistantkmp.composeapp.generated.resources.cancel
import voiceassistantkmp.composeapp.generated.resources.confirm
import voiceassistantkmp.composeapp.generated.resources.meta_person

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDetailsScreen(
    viewModel: PatientDetailsViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    val overlayAlpha by animateFloatAsState(if (state.isDialogOpen) 0.6f else 0.0f)
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val voiceHandler = LocalVoiceHandler.current

    LaunchedEffect(state.snackBarEvent) {
        state.snackBarEvent?.let { event ->
            snackBarHostState.showSnackbar(
                message = event.message,
                withDismissAction = true
            )
            viewModel.onSnackBarShown()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            snackbarHost = {
                val isError = state.snackBarEvent?.type == SnackBarType.ERROR
                val containerColor = if (isError) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.primary
                }
                SnackbarHost(
                    snackBarHostState,
                    modifier = Modifier.padding(bottom = 104.dp)
                ) { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = containerColor,
                        contentColor = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            },
            topBar = {
                LargeTopAppBar(
                    title = { Text("Patient Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                    scrollBehavior = scrollBehavior
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    PatientDetailsTabEnum.entries.forEachIndexed { index, tab ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = PatientDetailsTabEnum.entries.size
                            ),
                            onClick = { viewModel.onTabSelected(tab) },
                            selected = state.selectedTab == tab,
                            icon = {}
                        ) {
                            Text(tab.title)
                        }
                    }
                }
                Crossfade(
                    targetState = state.selectedTab,
                    modifier = Modifier.fillMaxSize(),
                    animationSpec = tween(300)
                ) { tab ->
                    when (tab) {
                        PatientDetailsTabEnum.Vitals -> VitalsPage(state)
                        PatientDetailsTabEnum.Notes -> NotesPage(state.notes)
                    }
                }
            }
        }

        if (state.isDialogOpen) {
            Surface(
                color = Color.Black.copy(alpha = overlayAlpha),
                modifier = Modifier.fillMaxSize().clickable(enabled = false) {}
            ) {}
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                Modifier
                    .padding(16.dp)
                    .align(Alignment.Center)
            )
        }

        if (state.selectedTab == PatientDetailsTabEnum.Vitals) {
            RecordingUISection(
                state = state,
                onRecordClick = {
                    voiceHandler.recordVoice { response ->
                        if (response == "GRANTED") {
                            viewModel.onRecordButtonClick()
                        }
                    }
                },
                onCancelClick = { viewModel.onDismissRecording() },
                onConfirmClick = { viewModel.onConfirmRecording() }
            )
        }
    }
}

@Composable
fun VitalsPage(
    state: PatientDetailsScreenState,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 160.dp)
        ) {
            items(state.items) { vital ->
                VitalSignsItem(
                    bloodPressure = vital.bloodPressure,
                    bloodSugar = vital.bloodSugar,
                    heartRate = vital.heartBeats,
                    timestamp = vital.formattedTime
                )
            }
        }
    }
}

@Composable
fun BoxScope.RecordingUISection(
    state: PatientDetailsScreenState,
    onRecordClick: () -> Unit,
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .padding(bottom = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AnimatedVisibility(visible = state.isDialogOpen) {
            Surface(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
                    .heightIn(max = 200.dp),
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 8.dp
            ) {
                Box(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
                    Text(text = state.recordedText)
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            AnimatedVisibility(
                visible = state.showActionButtons,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                FloatingActionButton(
                    onClick = onCancelClick,
                    modifier = Modifier.size(56.dp),
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    elevation = FloatingActionButtonDefaults.elevation(4.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.cancel),
                        contentDescription = "Cancel",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            FloatingActionButton(
                onClick = onRecordClick,
                modifier = Modifier.size(72.dp),
                shape = CircleShape,
                containerColor = if (state.isDialogOpen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
                contentColor = if (state.isDialogOpen) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer,
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Record",
                    modifier = Modifier.size(36.dp)
                )
            }

            AnimatedVisibility(
                visible = state.showActionButtons,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                FloatingActionButton(
                    onClick = onConfirmClick,
                    modifier = Modifier.size(56.dp),
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    elevation = FloatingActionButtonDefaults.elevation(4.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.confirm),
                        contentDescription = "Confirm",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotesPage(notesText: String) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 160.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3f / 2f),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Image(
                        painter = painterResource(Res.drawable.meta_person),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
                tonalElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = notesText,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 18.sp,
                            lineHeight = 26.sp,
                            letterSpacing = 0.5.sp
                        ),
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}
