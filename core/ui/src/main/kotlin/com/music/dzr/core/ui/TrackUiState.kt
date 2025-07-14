package com.music.dzr.core.ui

import androidx.compose.runtime.Immutable

@Immutable
data class TrackUiState(
    val coverUrl: String?,
    val title: String,
    val isExplicit: Boolean,
    val contributors: List<String>
)