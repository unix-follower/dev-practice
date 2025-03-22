from chemlib import Compound

from src.config.logging import get_logger

logger = get_logger()


def calculate_molecules(milliliters: float):
    """
    Calculate the number of molecules in water
    """

    water = Compound("H₂O")
    result = water.get_amounts(grams=milliliters)
    logger.debug(result)
    return result
