FROM tensorflow/tensorflow:2.15.0

RUN apt update && apt upgrade -y

WORKDIR /opt/app/

COPY Pipfile .
COPY Pipfile.lock .

RUN python -m pip install --upgrade pip
RUN pip install pipenv
RUN pipenv sync

COPY app ./

ENV APP_LSTM_MODEL_PATH=/opt/app/models/lstm_model.keras
ENV APP_GRU_MODEL_PATH=/opt/app/models/gru_model.keras

EXPOSE 8000

CMD pipenv run python -m gunicorn \
    --config ./app/config/gunicorn.conf.py \
    app.app:app
