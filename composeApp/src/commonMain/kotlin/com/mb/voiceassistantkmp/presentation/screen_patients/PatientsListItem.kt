package com.mb.voiceassistantkmp.presentation.screen_patients

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PatientsListItem(
    headlineContent: String,
    supportingContent: String,
    modifier: Modifier
) {
    Card(
        modifier = modifier
            .fillMaxSize(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = headlineContent,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                //color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = supportingContent,
                style = MaterialTheme.typography.bodySmall,
                //color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
