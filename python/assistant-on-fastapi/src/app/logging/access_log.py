import logging
from logging import Formatter

from pythonjsonlogger.json import JsonFormatter

from src.app.constants import CLIENT_ADDRESS, HTTP, METHOD, PROTOCOL, STATUS_CODE, URL


def _prepare_log_record(record: logging.LogRecord):
    (
        client_addr,
        method,
        full_path,
        http_version,
        status_code,
    ) = record.args
    record.__dict__.update(
        {
            CLIENT_ADDRESS: client_addr,
            PROTOCOL: f"{HTTP}/{http_version}",
            METHOD: method,
            URL: full_path,
            STATUS_CODE: status_code,
        }
    )


class AccessLogFormatter(Formatter):
    def format(self, record):
        _prepare_log_record(record)
        return super().format(record)


class AccessLogJsonFormatter(JsonFormatter):
    def format(self, record: logging.LogRecord):
        _prepare_log_record(record)
        return super().format(record)
