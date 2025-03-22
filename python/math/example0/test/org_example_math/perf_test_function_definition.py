import cProfile
import timeit
import unittest

# noinspection PyUnresolvedReferences
# pylint: disable=unused-import
from src.org_example_math.function_definition import f_definition as f_def
from src.org_example_math.config.logging_config import logger


class FunctionDefinitionPerformanceTest(unittest.TestCase):
    @staticmethod
    def test_execute_profiler():
        cProfile.runctx("f_def(0)", globals(), locals())

    def test_benchmark(self):
        result = timeit.timeit("f_def(0)", globals=globals())
        logger.info("benchmark result: %d", result)
        self.assertTrue(result < 1)


if __name__ == "__main__":
    unittest.main()
