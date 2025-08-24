import json
import logging.config
import os

from src.app import APP_DIR, constants
from src.app.logging.access_log import AccessLogFormatter


def configure_logging():
    with open(f"{APP_DIR}/config/logging.json", encoding=constants.UTF_8) as file:
        config = json.load(file)

        format_mode = os.environ.get("APP_LOG_FORMAT_MODE", "json")
        if format_mode == "text":
            config["handlers"]["default"]["formatter"] = format_mode
            config["formatters"]["access"]["class"] = f"{AccessLogFormatter.__module__}.{AccessLogFormatter.__name__}"

        is_debug_sql = os.environ.get("DEBUG_SQL", "false")
        if is_debug_sql.lower() == "true":
            loggers = config["loggers"]
            info_level = "INFO"
            level = "level"
            loggers["sqlalchemy.engine"][level] = info_level
            loggers["sqlalchemy.pool"][level] = info_level
            loggers["sqlalchemy.dialects"][level] = info_level
            loggers["sqlalchemy.orm"][level] = info_level

        logging.config.dictConfig(config)
