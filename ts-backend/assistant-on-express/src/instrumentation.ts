import * as opentelemetry from "@opentelemetry/sdk-node"
import { getNodeAutoInstrumentations } from "@opentelemetry/auto-instrumentations-node"
import { NodeTracerProvider, BatchSpanProcessor } from "@opentelemetry/sdk-trace-node"
import { CompressionAlgorithm } from "@opentelemetry/otlp-exporter-base"
import { ATTR_SERVICE_NAME, ATTR_SERVICE_VERSION } from "@opentelemetry/semantic-conventions"
import { OTLPTraceExporter } from "@opentelemetry/exporter-trace-otlp-grpc"
import { PrometheusExporter } from "@opentelemetry/exporter-prometheus"
import { defaultResource, resourceFromAttributes } from "@opentelemetry/resources"

const traceExporter = new OTLPTraceExporter({
  timeoutMillis: 15000,
  url: "http://192.168.105.8:4317/v1/traces",
  headers: {},
  compression: CompressionAlgorithm.GZIP,
})

const tracerProvider = new NodeTracerProvider({
  spanProcessors: [new BatchSpanProcessor(traceExporter)],
})

tracerProvider.register()
;["SIGINT", "SIGTERM"].forEach((signal) => {
  process.on(signal, () => tracerProvider.shutdown().catch(console.error))
})

const sdk = new opentelemetry.NodeSDK({
  resource: defaultResource().merge(
    resourceFromAttributes({
      [ATTR_SERVICE_NAME]: "assistant-on-express",
      [ATTR_SERVICE_VERSION]: "1.0",
    }),
  ),
  traceExporter,
  metricReader: new PrometheusExporter({
    port: 9464,
  }),
  instrumentations: [getNodeAutoInstrumentations()],
})
sdk.start()
