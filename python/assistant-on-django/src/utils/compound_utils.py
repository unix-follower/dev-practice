from typing import Callable

from chemlib import Compound
from src.config.logging import get_logger

logger = get_logger()


def calculate_by(compound_formula: str, compound_supplier: Callable[[Compound], dict]):
    compound = Compound(compound_formula)
    result = compound_supplier(compound)
    logger.debug(result)
    return result
