from chemlib import Combustion

from src.config.logging import get_logger
from src.component import constants

logger = get_logger()


def air_volume_combustion_with_propene(compound_volume: float):
    """
    Calculate the volume of air (in liters) to combustion the mix of cyclopropane and propene.
    """

    compound_formula = "C₃H₆"
    reagent = Combustion(compound_formula)
    logger.debug(reagent)

    oxygen_amount = reagent.coefficients["O₂"]
    propene_amount = reagent.coefficients[compound_formula]
    oxygen_fraction_in_compound = oxygen_amount / propene_amount
    oxygen_liters_for_combustion = compound_volume * oxygen_fraction_in_compound
    return oxygen_liters_for_combustion / constants.OXYGEN_FRACTION_IN_AIR
