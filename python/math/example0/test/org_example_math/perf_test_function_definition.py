import cProfile
import timeit
import unittest

# noinspection PyUnresolvedReferences
import src.org_example_mathfunction_definition as f_def
from src.org_example_math.config.logging_config import logger


class FunctionDefinitionPerformanceTest(unittest.TestCase):
    @staticmethod
    def test_execute_profiler():
        cProfile.runctx("f_def.example1(0)", globals(), locals())

    def test_benchmark(self):
        result = timeit.timeit("f_def.example1(0)", globals=globals())
        logger.info("benchmark result: {}".format(result))
        self.assertTrue(result < 1)


if __name__ == "__main__":
    unittest.main()
