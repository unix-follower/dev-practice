import logging
import textwrap

from src.org_example_math.config.logging_config import logger

def _log_slope_calculations(**kwargs):
    if logger.isEnabledFor(logging.INFO):
        x1 = kwargs["x1"]
        x2 = kwargs["x2"]
        numerator = f"f({x2}) - f({x1})"
        numerator_length = len(numerator)

        minus_position = numerator_length // 2
        denominator_x2 = str(x2)
        leading_whitespaces = " " * (minus_position - len(denominator_x2) - 1)
        denominator_str = leading_whitespaces + denominator_x2 + " - " + str(x1)

        line1 = "-" * numerator_length

        x1_square = kwargs["x1_square"]
        x2_square = kwargs["x2_square"]
        numerator_squares_str = f"{x2_square} - {x1_square}"
        numerator_squares_str_length = len(numerator_squares_str)
        denominator_subtraction_result = kwargs["denominator_subtraction_result"]
        denominator_subtraction_result_str = str(denominator_subtraction_result)
        denominator_subtraction_result_str_length = len(denominator_subtraction_result_str)
        if numerator_squares_str_length > denominator_subtraction_result_str_length:
            line2 = "-" * numerator_squares_str_length
        else:
            line2 = "-" * denominator_subtraction_result_str_length
        denominator_subtraction_position = (numerator_length + numerator_squares_str_length) - (
            numerator_squares_str_length // 2
        )
        denominator_str_length = len(denominator_str)
        denominator_subtraction_leading_whitespaces = " " * (
            denominator_subtraction_position - denominator_str_length - 1
        )

        denominator_subtraction_str = (
            denominator_subtraction_leading_whitespaces + denominator_subtraction_result_str
        )
        if (len(line2) - len(denominator_subtraction_str)) < 0:
            if denominator_subtraction_result_str_length <= 2:
                denominator_subtraction_leading_whitespaces = " " * (
                    numerator_length - denominator_str_length + 2
                )
            elif denominator_subtraction_result_str_length == 3:
                denominator_subtraction_leading_whitespaces = " " * (
                    numerator_length - denominator_str_length + 3
                )
            else:
                denominator_subtraction_leading_whitespaces = " " * (
                    numerator_length - denominator_str_length
                )

            denominator_subtraction_str = (
                denominator_subtraction_leading_whitespaces + denominator_subtraction_result_str
            )

        result = kwargs["result"]
        msg = textwrap.dedent(
            f"""
            {numerator}   {numerator_squares_str}
            {line1} = {line2} = {result}
            {denominator_str}   {denominator_subtraction_str}
            """
        )
        logger.info(msg)

def slope_angle(x1, x2):
    """
         BC   BD - CD   f(x₂) - f(x₁)
    AB = -- = ------- = -------------
         AC     ED        (x₂ - x₁)
    :return: (f(x₂) - f(x₁)) / (x₂ - x₁)
    """

    def f(x):
        return pow(x, 2)

    x2_square = f(x2)
    x1_square = f(x1)
    denominator_subtraction_result = x2 - x1
    result = (x2_square - x1_square) / denominator_subtraction_result

    _log_slope_calculations(
        x1=x1,
        x2=x2,
        x1_square=x1_square,
        x2_square=x2_square,
        denominator_subtraction_result=denominator_subtraction_result,
        result=result,
    )

    return result
