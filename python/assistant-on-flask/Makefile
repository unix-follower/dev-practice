setup:
	python -m pip install --upgrade pip
	pip install pipenv

i:
	pipenv install -d
run-dev:
	export APP_LSTM_MODEL_PATH="$(PWD)/app/models/lstm_model.keras" && \
	export APP_GRU_MODEL_PATH="$(PWD)/app/models/gru_model.keras" && \
	export FLASK_APP=app.app && \
	pipenv run python -m flask run --debug -p 8000

run:
	export APP_LSTM_MODEL_PATH="$(PWD)/app/models/lstm_model.keras" && \
	export APP_GRU_MODEL_PATH="$(PWD)/app/models/gru_model.keras" && \
	pipenv run python -m gunicorn \
		--config ./app/config/gunicorn.conf.py app.app:app
