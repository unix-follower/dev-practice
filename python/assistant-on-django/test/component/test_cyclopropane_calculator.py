import unittest

from src.component import cyclopropane_calculator


class CyclopropaneCalculatorTest(unittest.TestCase):
    def test_calculate_volume_of_oxygen_for_combustion_of_6_liters_of_cyclopropane_and_propene(self):
        # given
        compound_volume = 6

        # O₂ fraction in compound = 9/2 = 4.5
        # O₂ > C₃H₆
        # O₂ liters for combustion = compound_volume * oxygen fraction = 6 * 4.5 = 27 liters
        # O₂ volume in the air ~ 21% = 0.21
        # O₂ liters for combustion / O₂ fraction in the air
        # 27 / 0.21 = 128.57142857142858 ~ 128.57 liters

        # when
        result = cyclopropane_calculator.air_volume_combustion_with_propene(compound_volume)

        # then
        self.assertAlmostEqual(128.57, result, places=2)


if __name__ == '__main__':
    unittest.main()
