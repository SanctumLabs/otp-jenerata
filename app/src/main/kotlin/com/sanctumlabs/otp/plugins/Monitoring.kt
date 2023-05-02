package com.sanctumlabs.otp.plugins

import com.codahale.metrics.Slf4jReporter
import io.ktor.http.HttpHeaders
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.metrics.dropwizard.DropwizardMetrics
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import java.time.Duration
import java.util.concurrent.TimeUnit

const val MICROMETER_MAX_EXPECTED_VALUE_IN_SECONDS = 20L
const val MICROMETER_SLO1_IN_MS = 100L
const val MICROMETER_SLO2_IN_MIS = 500L
const val DROP_WIZARD_METRIC_PERIOD_IN_SECONDS = 10L

fun Application.configureMonitoringPlugin() {
    val appMicrometerRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

    install(MicrometerMetrics) {
        registry = appMicrometerRegistry
        distributionStatisticConfig = DistributionStatisticConfig.Builder()
            .percentilesHistogram(true)
            .maximumExpectedValue(Duration.ofSeconds(MICROMETER_MAX_EXPECTED_VALUE_IN_SECONDS).toNanos().toDouble())
            .serviceLevelObjectives(
                Duration.ofMillis(MICROMETER_SLO1_IN_MS).toNanos().toDouble(),
                Duration.ofMillis(MICROMETER_SLO2_IN_MIS).toNanos().toDouble()
            )
            .build()
        meterBinders = listOf(
            JvmMemoryMetrics(),
            JvmGcMetrics(),
            ProcessorMetrics()
        )
    }

    install(DropwizardMetrics) {
        Slf4jReporter.forRegistry(registry)
            .outputTo(this@configureMonitoringPlugin.log)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.MILLISECONDS)
            .build()
            .start(DROP_WIZARD_METRIC_PERIOD_IN_SECONDS, TimeUnit.SECONDS)
    }

    install(CallId) {
        header(HttpHeaders.XRequestId)
        verify { callId: String -> callId.isNotEmpty() }
    }

    routing {
        get("/metrics-micrometer") {
            call.respond(appMicrometerRegistry.scrape())
        }
    }
}
