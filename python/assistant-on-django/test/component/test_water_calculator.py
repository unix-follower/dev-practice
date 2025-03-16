import unittest

from src.component import constants, water_calculator


class WaterCalculatorTest(unittest.TestCase):
    def test_calculate_200_molecules(self):
        # given
        milliliters = 200

        # 1 ml = 1 g
        # 200 ml = 200 g
        # Avogadro's number = 6.02e+23
        # H molar mass = 1.008
        # O molar mass = 15.999
        # H₂O molar mass = 1.008 + 1.008 + 15.999 = 18.015
        # molecules = grams / molar mass * Avogadro
        # 200 / 18.015 * 6.02e+23 = 11.101859561476547 * 6.02e+23 = 6.683319456008883e24 ~ 6.7e+24 molecules

        # when
        result = water_calculator.calculate_molecules(milliliters)
        # {'grams': 200, 'molecules': 6.683319456008881e+24, 'moles': 11.101859561476546}

        # then
        self.assertEqual(200, result.get(constants.GRAMS))

        molecules = float(format(result.get(constants.MOLECULES), ".4g"))
        self.assertEqual(0.0, 6.683e+24 - molecules)

        moles = float(format(result.get(constants.MOLES), ".4g"))
        self.assertEqual(11.1, moles)


if __name__ == '__main__':
    unittest.main()
