package com.mb.voiceassistantkmp.presentation.screen_patientDetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mb.voiceassistantkmp.presentation.utils.LocalVoiceHandler
import org.jetbrains.compose.resources.painterResource
import voiceassistantkmp.composeapp.generated.resources.Res
import voiceassistantkmp.composeapp.generated.resources.cancel
import voiceassistantkmp.composeapp.generated.resources.confirm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDetailsScreen(
    viewModel: PatientDetailsViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    val blurRadius by animateDpAsState(if (state.isDialogOpen) 10.dp else 0.dp)
    val overlayAlpha by animateFloatAsState(if (state.isDialogOpen) 0.6f else 0.0f)
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val voiceHandler = LocalVoiceHandler.current

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .blur(blurRadius),
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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = innerPadding.calculateTopPadding() + 8.dp,
                    bottom = 160.dp
                )
            ) {
                items(
                    state.items
                ) { vital ->
                    Box(modifier = Modifier.animateItem()) {
                        VitalSignsItem(
                            bloodPressure = vital.bloodPressure,
                            bloodSugar = vital.bloodSugar,
                            heartRate = vital.heartBeats
                        )
                    }
                }
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                Modifier
                    .padding(16.dp)
                    .align(Alignment.Center)
            )
        }

        if (state.isDialogOpen) {
            Surface(
                color = Color.Black.copy(alpha = overlayAlpha),
                modifier = Modifier.fillMaxSize().clickable(enabled = false) {}
            ) {}
        }

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
            .padding(bottom = 32.dp),
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
//                color = MaterialTheme.colorScheme.secondaryContainer,
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
//                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
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
//                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
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
