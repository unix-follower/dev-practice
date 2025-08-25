i:
	uv sync
	uv pip install -r requirements.txt

i-dev:
	uv sync --all-groups

lint:
	uv run ruff check
format:
	uv run ruff format

run-dev:
	export APP_LSTM_MODEL_PATH="$(PWD)/src/app/models/lstm_model.keras" && \
	export APP_GRU_MODEL_PATH="$(PWD)/src/app/models/gru_model.keras" && \
	export FLASK_APP=src.app.app && \
	export OTEL_LOG_LEVEL=debug && \
	export OTEL_PYTHON_LOGGING_AUTO_INSTRUMENTATION_ENABLED=true && \
 	uv run opentelemetry-instrument \
 		--traces_exporter console \
 		--metrics_exporter console \
 		--logs_exporter console \
 		--service_name assistant-on-flask \
		flask run -h 0.0.0.0 -p 5000

run:
	export APP_LSTM_MODEL_PATH="$(PWD)/src/app/models/lstm_model.keras" && \
	export APP_GRU_MODEL_PATH="$(PWD)/src/app/models/gru_model.keras" && \
	export OTEL_LOG_LEVEL=debug && \
	export OTEL_PYTHON_LOGGING_AUTO_INSTRUMENTATION_ENABLED=true && \
 	uv run opentelemetry-instrument \
 		--traces_exporter console \
 		--metrics_exporter console \
 		--logs_exporter console \
 		--service_name assistant-on-flask \
 		gunicorn --config ./src/app/config/gunicorn.conf.py app.app:app
