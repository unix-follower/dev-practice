import unittest

from src.component import constants, hydrogen_calculator


class HydrogenCalculatorTest(unittest.TestCase):
    def test_calculate_atoms_in_10moles_of_butane(self):
        # given
        butane_moles = 10

        # C₄H₁₀
        # C molar mass = 12.011
        # H molar mass = 1.008
        # molar mass = 12.011 * 4 + 1.008 * 10 = 48.044 + 10.08 = 58.124 g/mol
        # moles = mass in grams / molar mass = 1 / 58.124 = 0.017204597068337
        # molecules = moles * Avogadro = 10 * 6.02e+23 = 6.02e+24

        # when
        result = hydrogen_calculator.calculate_atoms_in_butane(butane_moles)
        # {'grams': 581.2400000000002, 'molecules': 6.02e+24, 'moles': 10}

        # then
        self.assertAlmostEqual(581.24, result.get(constants.GRAMS), places=2)

        molecules = float(format(result.get(constants.MOLECULES), ".4g"))
        self.assertEqual(0.0, 6.02e+24 - molecules)

        self.assertEqual(butane_moles, result.get(constants.MOLES))


if __name__ == '__main__':
    unittest.main()
