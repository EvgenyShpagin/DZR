package com.music.dzr.core.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

/**
 * Application-level scope for long-running coroutines.
 *
 * Used for long-lived operations that should run independently of specific
 * component lifecycles (Activity, Fragment).
 *
 * ## Features:
 * - Uses [SupervisorJob] — failure in one child coroutine doesn't cancel others
 * - Runs on [DispatcherProvider.default] by default for CPU-intensive tasks
 * - Lifetime equals application lifetime
 *
 * ## Usage:
 * ```kotlin
 * class ArticlesRepository(
 *     private val articlesDataSource: ArticlesDataSource,
 *     private val externalScope: ApplicationScope,
 * ) {
 *     // As we want to complete bookmarking the article even if the user moves
 *     // away from the screen, the work is done creating a new coroutine
 *     // from an external scope
 *     suspend fun bookmarkArticle(article: Article) {
 *         externalScope.launch { articlesDataSource.bookmarkArticle(article) }
 *             .join() // Wait for the coroutine to complete
 *     }
 * }
 * ```
 *
 * @param dispatchers Dispatcher provider for coroutine execution
 */
class ApplicationScope(
    dispatchers: DispatcherProvider
) : CoroutineScope by CoroutineScope(
    SupervisorJob() + dispatchers.default
)
