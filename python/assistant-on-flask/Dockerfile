FROM python:3.12.11-slim

WORKDIR /opt/app/

RUN python -m pip install --upgrade pip

COPY pyproject.toml requirements.txt ./

RUN pip install uv
RUN uv sync
RUN uv pip install -r requirements.txt

COPY src ./src

ENV APP_LSTM_MODEL_PATH=/opt/app/models/lstm_model.keras
ENV APP_GRU_MODEL_PATH=/opt/app/models/gru_model.keras

EXPOSE 8000

CMD ["uv", "run", "gunicorn", "--config", "./src/app/config/gunicorn.conf.py", "src.app.app:app"]
