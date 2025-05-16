package com.music.dzr.core.ui

import android.content.Context
import androidx.compose.ui.util.fastJoinToString
import com.music.dzr.core.model.ReleaseType

fun formatContributors(contributors: List<String>): String {
    return contributors.fastJoinToString()
}

fun ReleaseType.toString(context: Context): String {
    return when (this) {
        ReleaseType.ALBUM -> context.getString(R.string.release_type_album)
        ReleaseType.EP -> context.getString(R.string.release_type_ep)
        ReleaseType.SINGLE -> context.getString(R.string.release_type_single)
        ReleaseType.COMPILATION -> context.getString(R.string.release_type_compilation)
        ReleaseType.FEATURED_IN -> context.getString(R.string.release_type_featured_in)
    }
}
