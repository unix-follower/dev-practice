import logging

_logger = None


def create_logger(level: str):
    logging_level = logging.getLevelName(level)
    stream_handler = logging.StreamHandler()
    stream_handler.setLevel(logging_level)

    formatter = logging.Formatter("%(asctime)s %(levelname)s [%(threadName)s] %(filename)s: %(message)s")
    stream_handler.setFormatter(formatter)

    logger = logging.getLogger("main")
    logger.setLevel(logging_level)
    logger.addHandler(stream_handler)

    global _logger
    _logger = logger
    return logger


def get_logger():
    global _logger
    if _logger is None:
        _logger = create_logger("INFO")

    return _logger
