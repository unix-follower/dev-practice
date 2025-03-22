import unittest

from src.component import constants, nitrogen_calculator


class NitrogenCalculatorTest(unittest.TestCase):
    def test_calculate_mass_of_2_molecules_of_N(self):
        # given
        n_molecules = 2

        # N molar mass = 14.007
        # N moles = N molecules / Avogadro = 2 / 6.02e+23 = 3.322259136212625e-24
        # N molecules = 14.007 * 3.322259136212625e-24 = 4.65348837209302e-23 ~ 4.65e-23 grams

        # when
        result = nitrogen_calculator.calculate_mass(n_molecules)
        # {'grams': 4.6534883720930234e-23, 'molecules': 2, 'moles': 3.322259136212625e-24}

        # then
        grams = float(format(result.get(constants.GRAMS), ".4g"))
        self.assertEqual(0.0, 4.653e-23 - grams)

        self.assertEqual(2, result.get(constants.MOLECULES))

        moles = float(format(result.get(constants.MOLES), ".4g"))
        self.assertEqual(0.0, 3.322e-24 - moles)

    def test_calculate_ammonium_compound_atoms(self):
        # given
        total_grams = 100
        nitrogen_fraction = 0.9  # 90%

        # (NH₄)₂CO₃
        # N molar mass = 14.007
        # H molar mass = 1.008
        # C molar mass = 12.011
        # O molar mass = 15.999
        # N grams = total grams * nitrogen fraction = 100 * 0.9 = 90.0
        # molar mass = 14.007 * 2 + 1.008 * 8 + 12.011 + 15.999 * 3 = 28.014 + 8.064 + 12.011 + 47.997 = 96.086
        # moles = mass in grams / molar mass = 90 / 96.086 = 0.9366609079366401
        # molecules = moles * Avogadro = 90 / 96.086 * 6.02e+23 = 0.9366609079366401 * 6.02e+23 = 5.638698665778574e+23

        # 2 moles = 0.9366609079366401 + 0.9366609079366401 = 1.8733218158732803
        # N atoms = 2 moles * Avogadro = 1.8733218158732803 * 6.02e+23 = 1.1277397331557148e+24

        # when
        result = nitrogen_calculator.calculate_ammonium_compound_atoms(total_grams, nitrogen_fraction)
        # {
        # 'grams': 90.0,
        # 'molecules': 5.638698665778574e+23,
        # 'moles': 0.9366609079366401,
        # 'totalFractionAtoms': 1.1277397331557148e+24
        # }

        # then
        self.assertEqual(90, result.get(constants.GRAMS))

        molecules = float(format(result.get(constants.MOLECULES), ".4g"))
        self.assertEqual(0.0, 5.639e+23 - molecules)

        moles = result.get(constants.MOLES)
        self.assertAlmostEqual(0.936, moles, places=2)

        total_fraction_atoms = float(format(result.get(constants.TOTAL_FRACTION_ATOMS), ".4g"))
        self.assertEqual(0.0, 1.128e+24 - total_fraction_atoms, total_fraction_atoms)

    def test_calculate_urea_compound_atoms(self):
        # given
        total_grams = 20
        nitrogen_fraction = 0.9  # 90%

        # (NH₂)₂CO grams = total grams * nitrogen fraction = 20 * 0.9 = 18
        # N molar mass = 14.007
        # H molar mass = 1.008
        # C molar mass = 12.011
        # O molar mass = 15.999
        # molar mass = 14.007 * 2 + 1.008 * 4 + 12.011 + 15.999 = 28.014 + 4.032 + 12.011 + 15.999 = 60.056
        # moles = mass in grams / molar mass = 18 / 60.056 = 0.29972026108964966
        # molecules = moles * Avogadro = 0.29972026108964966 * 6.02e+23 = 1.804315971759691e+23

        # 2 moles = 0.29972026108964966 + 0.29972026108964966 = 0.5994405221792993
        # N atoms = 2 moles * Avogadro = 0.5994405221792993 * 6.02e+23 = 3.608631943519382e+23

        # when
        result = nitrogen_calculator.calculate_urea_compound_atoms(total_grams, nitrogen_fraction)
        # {
        # 'grams': 18.0,
        # 'molecules': 1.804315971759691e+23,
        # 'moles': 0.29972026108964966,
        # 'totalFractionAtoms': 3.608631943519382e+23
        # }

        # then
        self.assertEqual(18, result.get(constants.GRAMS))

        molecules = float(format(result.get(constants.MOLECULES), ".4g"))
        self.assertEqual(0.0, 1.804e+23 - molecules)

        moles = result.get(constants.MOLES)
        self.assertAlmostEqual(0.299, moles, places=2)

        total_fraction_atoms = float(format(result.get(constants.TOTAL_FRACTION_ATOMS), ".4g"))
        self.assertEqual(0.0, 3.609e+23 - total_fraction_atoms, total_fraction_atoms)


if __name__ == '__main__':
    unittest.main()
