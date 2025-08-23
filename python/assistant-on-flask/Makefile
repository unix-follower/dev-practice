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
	export FLASK_APP=app.app && \
	uv run flask run --debug -p 8000

run:
	export APP_LSTM_MODEL_PATH="$(PWD)/src/app/models/lstm_model.keras" && \
	export APP_GRU_MODEL_PATH="$(PWD)/src/app/models/gru_model.keras" && \
	uv run gunicorn --config ./src/app/config/gunicorn.conf.py app.app:app
