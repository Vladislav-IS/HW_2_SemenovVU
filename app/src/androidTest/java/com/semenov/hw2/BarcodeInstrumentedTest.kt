package com.semenov.hw2

import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.FailureHandler
import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.accessibility.AccessibilityChecks
import androidx.test.espresso.base.DefaultFailureHandler
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matcher
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
@MediumTest
class BarcodeInstrumentedTest {
    @get:Rule
    val composeTestRule = createEmptyComposeRule()

    private var activityScenario: ActivityScenario<MainActivity>? = null
    private var handler: BarcodeFailureHandler? = null
    private var uiDevice: UiDevice? = null

    private val desiredUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
    private var linksFound = 0

    private lateinit var appContext: Context
    private lateinit var mInstrumentation: Instrumentation
    private lateinit var barcodeScanner: BarcodeScannerOptions

    @Before
    fun setUp() {
        mInstrumentation = InstrumentationRegistry.getInstrumentation()
        handler = BarcodeFailureHandler(mInstrumentation)
        Espresso.setFailureHandler(handler)

        uiDevice = UiDevice.getInstance(mInstrumentation)
        uiDevice?.pressHome()

        val nonLocalizedContext = mInstrumentation.targetContext
        val configuration = nonLocalizedContext.resources.configuration
        configuration.setLocale(Locale.UK)
        appContext = nonLocalizedContext.createConfigurationContext(configuration)

        barcodeScanner = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC
            )
            .build()

        val intent = Intent(appContext, MainActivity::class.java)
        activityScenario = ActivityScenario.launch(intent)
    }

    @Test(timeout = MAX_TIMEOUT)
    fun checkQRIsShown() {
        checkImageStep()
    }

    private fun checkImageStep() = runBlocking {
        Log.d("staer", "sdc")
        delay(LOADING_DELAY)
        val qrBitmap = composeTestRule
            .onNodeWithContentDescription("qr-code")
            .captureToImage()
            .asAndroidBitmap()
        val imageFromView = InputImage.fromBitmap(qrBitmap, 0)
        val scanner = BarcodeScanning.getClient()
        val resultFromView = scanner.process(imageFromView)
        delay(THREAD_DELAY)
        resultFromView.result.forEach { barcode ->
            handler?.appendExtraMessage("Barcode of type ${barcode.valueType}")
            when (barcode.valueType) {
                Barcode.TYPE_URL -> {
                    barcode.url?.url?.let { url ->
                        Log.d(TAG, url)
                        validateRecognizedUrl(url)
                    }
                }
            }
        }
        assertEquals("Total number of links: $linksFound.", 1, linksFound)
    }

    private fun validateRecognizedUrl(url: String) {
        assertEquals(desiredUrl, url)
        linksFound++
    }

    companion object {
        private const val LOADING_DELAY: Long = 4_000
        private const val THREAD_DELAY: Long = 8_900
        private const val MAX_TIMEOUT: Long = 20_000
        private const val TAG = "BarcodeImageTest"

        @BeforeClass
        @JvmStatic
        fun enableAccessibilityChecks() {
            AccessibilityChecks.enable()
            IdlingPolicies.setMasterPolicyTimeout(10, TimeUnit.SECONDS)
            IdlingPolicies.setIdlingResourceTimeout(10, TimeUnit.SECONDS)
        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            val mInstrumentation = InstrumentationRegistry.getInstrumentation()
            val uiDevice = UiDevice.getInstance(mInstrumentation)
            uiDevice.pressHome()
        }
    }
}

class BarcodeFailureHandler(instrumentation: Instrumentation) : FailureHandler {
    private var extraMessage = StringBuilder("")
    private var delegate: DefaultFailureHandler = DefaultFailureHandler(instrumentation.targetContext)

    override fun handle(error: Throwable?, viewMatcher: Matcher<View>?) {
        if (error != null) {
            val newError = Throwable(
                "$extraMessage ${error.message}",
                error.cause,
            )
            delegate.handle(newError, viewMatcher)
        }
    }

    fun appendExtraMessage(text: String) {
        extraMessage = extraMessage.append(text)
    }
}
