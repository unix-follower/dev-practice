i:
	uv sync

i-dev:
	uv sync --all-groups

lint:
	uv run ruff check
format:
	uv run ruff format

# test:
# 	pytest tests
# coverage:
# 	pytest --cov=app tests
# coverage-html:
# 	coverage html

run-dev:
	export OTEL_LOG_LEVEL=debug && \
	export OTEL_PYTHON_LOGGING_AUTO_INSTRUMENTATION_ENABLED=true && \
	export APP_LOG_FORMAT_MODE=text && \
	export DEBUG_SQL=true && \
 	uv run opentelemetry-instrument \
		--traces_exporter console \
		--metrics_exporter console \
		--logs_exporter console \
		--service_name assistant-on-fastapi \
		fastapi dev src/app/main.py

run:
	uv run uvicorn src.app.main:app \
	  --host 0.0.0.0 --port 8000 \
	  --log-config src/app/config/logging.json \
	  --workers 4 \
	  --access-log \
	  --timeout-graceful-shutdown 30
