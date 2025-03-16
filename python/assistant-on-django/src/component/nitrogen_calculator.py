import chemlib
from chemlib import Compound

from src.component import constants
from src.config.logging import get_logger

logger = get_logger()


def calculate_mass(molecules: float = 1):
    """
    Calculate the mass (in grams) of nitrogen molecules.
    """

    nitrogen = Compound(constants.NITROGEN_SYMBOL)
    result = nitrogen.get_amounts(molecules=molecules)
    logger.debug(result)
    return result


def calculate_compound_atoms(compound_formula: str, total_grams: float, nitrogen_fraction: float):
    """
    Calculate the number of nitrogen atoms in grams of compound
    that contains a certain percent of non-nitrogen impurity.
    :param compound_formula: compound with nitrogen
    :param total_grams: mass of compound
    :param nitrogen_fraction: percent of nitrogen in compound
    """

    compound = Compound(compound_formula)
    grams = total_grams * nitrogen_fraction

    amounts = compound.get_amounts(grams=grams)
    one_mole = amounts.get(constants.MOLES)
    two_mole = one_mole + one_mole
    result = two_mole * chemlib.AVOGADROS_NUMBER

    amounts[constants.TOTAL_FRACTION_ATOMS] = result
    logger.debug(amounts)

    return amounts


def calculate_ammonium_compound_atoms(total_grams: float, nitrogen_fraction: float):
    """
    Calculate the number of nitrogen atoms in grams of ammonium
    that contains a certain percent of non-nitrogen impurity.
    :param total_grams: mass of ammonium carbonate
    :param nitrogen_fraction: percent of nitrogen in compound
    """
    return calculate_compound_atoms("(NH₄)₂CO₃", total_grams, nitrogen_fraction)


def calculate_urea_compound_atoms(total_grams: float, nitrogen_fraction: float):
    """
    Calculate the number of nitrogen atoms in grams of carbamide
    that contains a certain percent of non-nitrogen impurity.
    :param total_grams: mass of carbamide
    :param nitrogen_fraction: percent of nitrogen in compound
    """
    return calculate_compound_atoms("(NH₂)₂CO", total_grams, nitrogen_fraction)
