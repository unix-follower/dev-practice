package org.example.assistantonsbservlet.finance;

public final class FinanceCalculator {
    private FinanceCalculator() {
    }

    public static final class BusinessPlanning {
        private BusinessPlanning() {
        }

        /**
         * @return Absence rate = Total days absent / (Number of employees * Number of workdays) * 100
         */
        public static double absencePercentage(int totalDaysAbsent, long employeesNumber, int workdays) {
            return (double) totalDaysAbsent / (employeesNumber * workdays) * 100;
        }

        /**
         * @return additional funds needed = change assets - change liabilities - change retained earnings
         */
        public static double absencePercentage(
            double changeAssets, double changeLiabilities, double changeRetainedEarnings) {
            return changeAssets - changeLiabilities - changeRetainedEarnings;
        }

        /**
         * @return Attrition = (L / (½ * (S+E))) * 100%
         */
        public static double attritionRate(int employeesAtStart, int employeesAtEnd, int employeesLeft) {
            return (employeesLeft / (0.5 * (employeesAtStart + employeesAtEnd))) * 100;
        }
    }
}
