package org.example.assistantonsbservlet.finance;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FinanceCalculatorTest {
    @Nested
    class BusinessPlanning {
        @Test
        void calculateManHours() {
            // given
            final int people = 2;
            final int hoursPerPerson = 16;
            // when
            final double manHours = FinanceCalculator.BusinessPlanning.manHours(people, hoursPerPerson);
            // then
            assertEquals(32, manHours, 0.1);
        }

        @Test
        void calculateNumOfPeopleNeeded() {
            // given
            final int manHours = 35;
            final int hoursPerPerson = 16;
            // when
            final double people = FinanceCalculator.BusinessPlanning.numOfPeopleNeeded(manHours, hoursPerPerson);
            // then
            assertEquals(2, people, 0.1);
        }

        @Test
        void calculateManHoursTotalCostExact() {
            // given
            final int manHours = 35;
            final double hourlyPay = 5.71;
            // when
            final double totalCost = FinanceCalculator.BusinessPlanning.manHoursTotalCostExact(manHours, hourlyPay);
            // then
            assertEquals(199.85, totalCost, 0.01);
        }

        @Test
        void calculateCostPerPerson() {
            // given
            final double hourlyPay = 5.71;
            final int hoursPerPerson = 16;
            // when
            final double cost = FinanceCalculator.BusinessPlanning.costPerPerson(hoursPerPerson, hourlyPay);
            // then
            assertEquals(91.36, cost, 0.01);
        }

        @Test
        void calculateHourlyPay() {
            // given
            final double manHours = 35;
            final double totalCost = 199.85;
            // when
            final double pay = FinanceCalculator.BusinessPlanning.hourlyPay(totalCost, manHours);
            // then
            assertEquals(5.71, pay, 0.01);
        }

        @Test
        void calculateYoutubeGrossRevenuePerDay() {
            // given
            final double rpm = 6.5;
            final int numberOfViews = 13500;
            // when
            final double grossRevenue = FinanceCalculator.BusinessPlanning
                .youtubeGrossRevenuePerDay(rpm, numberOfViews);
            // then
            assertEquals(87.75, grossRevenue, 0.01);
        }

        @Test
        void calculateYoutubeNetRevenuePerDay() {
            // given
            final double rpm = 6.5;
            final int numberOfViews = 13500;
            // when
            final double netRevenue = FinanceCalculator.BusinessPlanning.youtubeNetRevenuePerDay(rpm, numberOfViews);
            // then
            assertEquals(48.26, netRevenue, 0.01);
            assertEquals(337.84, netRevenue * 7, 0.01);
        }
    }

    @Nested
    class EquityInvestment {
        @Test
        void calculateDividendYield() {
            // given
            final double koSharePrice = 66.65;
            final double annualDividendPerShare = 2.04;
            // when
            final double result = FinanceCalculator.EquityInvestment
                .dividendYield(koSharePrice, annualDividendPerShare);
            // then
            assertEquals(3.06, result, 0.01);
        }

        static List<Arguments> calculateDividendsPerPeriodParams() {
            return List.of(
                Arguments.of(FinanceCalculator.Frequency.MONTHLY, 0.17),
                Arguments.of(FinanceCalculator.Frequency.QUARTERLY, 0.51),
                Arguments.of(FinanceCalculator.Frequency.SEMI_ANNUAL, 1.02),
                Arguments.of(FinanceCalculator.Frequency.ANNUAL, 2.04)
            );
        }

        @ParameterizedTest
        @MethodSource("calculateDividendsPerPeriodParams")
        void calculateDividendsPerPeriod(FinanceCalculator.Frequency frequency, double expectedResult) {
            // given
            final double koAnnualDividendPerShare = 2.04;
            // when
            final double result = FinanceCalculator.EquityInvestment
                .dividendsPerPeriod(koAnnualDividendPerShare, frequency);
            // then
            assertEquals(expectedResult, result, 0.01);
        }

        static List<Arguments> calculateDRIPParams() {
            return List.of(
                Arguments.of(FinanceCalculator.Frequency.DAILY, 101.646),
                Arguments.of(FinanceCalculator.Frequency.WEEKLY, 101.6449),
                Arguments.of(FinanceCalculator.Frequency.MONTHLY, 101.6403),
                Arguments.of(FinanceCalculator.Frequency.QUARTERLY, 101.6284),
                Arguments.of(FinanceCalculator.Frequency.SEMI_ANNUAL, 101.6106),
                Arguments.of(FinanceCalculator.Frequency.ANNUAL, 101.5756)
            );
        }

        @ParameterizedTest
        @MethodSource("calculateDRIPParams")
        void calculateDRIP(FinanceCalculator.Frequency compoundFrequency, double expectedResult) {
            // given
            final double moneyInvested = 96.38;
            final double dividendYield = 0.0266;
            final byte numberOfYears = 2;
            // when
            final double result = FinanceCalculator.EquityInvestment.drip(
                moneyInvested, dividendYield, compoundFrequency, numberOfYears);
            // then
            assertEquals(expectedResult, result, 0.0001);
        }
    }
}
