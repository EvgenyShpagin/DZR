package coroutine

import com.music.dzr.core.coroutine.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler

/**
 * Test implementation of [DispatcherProvider] for unit and instrumented tests.
 *
 * All dispatchers ([io], [default], [main]) use the same [TestDispatcher][kotlinx.coroutines.test.TestDispatcher],
 * which allows:
 * - Deterministic coroutine execution in tests.
 * - Time control through the shared [TestCoroutineScheduler].
 * - Avoiding multithreading in tests.
 *
 * ## Usage in Unit Tests
 * To ensure the component under test uses the same scheduler as the test,
 * pass the `testScheduler` from the `runTest` scope to the provider.
 *
 * ```kotlin
 * @Test
 * fun `should process data correctly`() = runTest {
 *     val dispatcherProvider = TestDispatcherProvider(this.testScheduler)
 *     val repository = Repository(dispatcherProvider)
 *
 *     repository.loadData()
 *
 *     // Coroutines in the repository now use the test's scheduler
 *     advanceUntilIdle() // Ensure all scheduled coroutines run
 *
 *     // Assert the result
 * }
 * ```
 *
 * ## Injection in Android (Koin) Tests
 * For instrumented tests, replace the production `DispatcherProvider`
 * with this test implementation by providing a Koin module in your test setup.
 *
 * **1. Create a single Koin module for your test:**
 * This module provides all necessary dependencies for the test, including the
 * overridden `DispatcherProvider` and the ViewModel that uses it.
 *
 * ```kotlin
 * // In your test source set
 * val testAppModule = module {
 *     // Provides a single TestCoroutineScheduler instance for the entire test
 *     single { TestCoroutineScheduler() }
 *
 *     // Overrides the production DispatcherProvider with the test version.
 *     single<DispatcherProvider> {
 *         TestDispatcherProvider(get())
 *     }
 *
 *     // Provide other dependencies needed for the test
 *     viewModel { MyViewModel(get()) }
 * }
 * ```
 *
 * **2. Use it in your instrumented test:**
 * The `KoinTestRule` is used to apply the test modules.
 *
 * ```kotlin
 * class MyScreenTest : KoinTest {
 *
 *     @get:Rule
 *     val koinTestRule = KoinTestRule.create {
 *         modules(testAppModule) // Load your single test module
 *     }
 *
 *     // Inject dependencies from the Koin container
 *     private val scheduler: TestCoroutineScheduler by inject()
 *     private val viewModel: MyViewModel by inject()
 *
 *     @Test
 *     fun someViewModelTest() = runTest(scheduler) {
 *         // The ViewModel receives TestDispatcherProvider via Koin
 *         viewModel.loadData()
 *
 *         advanceUntilIdle()
 *
 *         // Assert the UI state
 *     }
 * }
 * ```
 *
 * @param scheduler Scheduler for controlling coroutine execution time in tests.
 *                  Creates a new [TestCoroutineScheduler] by default.
 */
class TestDispatcherProvider(
    scheduler: TestCoroutineScheduler = TestCoroutineScheduler()
) : DispatcherProvider {

    private val testDispatcher = StandardTestDispatcher(scheduler)

    override val io: CoroutineDispatcher get() = testDispatcher
    override val default: CoroutineDispatcher get() = testDispatcher
    override val main: CoroutineDispatcher get() = testDispatcher
}
