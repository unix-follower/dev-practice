from src.utils import compound_utils


def calculate_atoms_in_butane(butane_moles: float = 1):
    """
    Calculate the number of nitrogen atoms in butane.
    """
    calc_by_moles_fn = lambda butane: butane.get_amounts(moles=butane_moles)
    return compound_utils.calculate_by("C₄H₁₀", calc_by_moles_fn)
