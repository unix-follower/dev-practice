package org.example.assistantonsbservlet.finance;

public final class FinanceCalculator {
    private FinanceCalculator() {
    }

    public static final class BusinessPlanning {
        private BusinessPlanning() {
        }

        /**
         * @return man-hours = people * hours
         */
        public static double manHours(int people, float hoursPerPerson) {
            return people * hoursPerPerson;
        }

        /**
         * @return people = man-hours / hours
         */
        public static int numOfPeopleNeeded(int manHours, int hoursPerPerson) {
            return Math.floorDiv(manHours, hoursPerPerson);
        }

        /**
         * No rounding.
         * @return total cost = man-hours * hourly pay
         */
        public static double manHoursTotalCostExact(int manHours, double hourlyPay) {
            return manHours * hourlyPay;
        }

        public static double costPerPerson(int hoursPerPerson, double hourlyPay) {
            return hoursPerPerson * hourlyPay;
        }

        public static double hourlyPay(double totalCost, double manHours) {
            return totalCost / manHours;
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

        /**
         * RPM stands for revenue per 1000 impressions (RPM).
         * @return Estimated gross earnings = RPM * number of views / 1000
         */
        public static double youtubeGrossRevenuePerDay(double rpm, long numberOfViews) {
            return rpm * numberOfViews / 1000;
        }

        /**
         * YouTube takes 45% of the money made on videos on its platform.
         * @return Estimated net earnings = estimated gross earnings × (55 / 100)
         */
        public static double youtubeNetRevenuePerDay(double rpm, long numberOfViews) {
            final double grossRevenue = youtubeGrossRevenuePerDay(rpm, numberOfViews);
            return grossRevenue * 0.55;
        }
    }

    public enum Frequency {
        ANNUAL(1),
        SEMI_ANNUAL(2),
        QUARTERLY(4),
        MONTHLY(12),
        WEEKLY(52),
        DAILY(365);

        private final int number;

        Frequency(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }
    }

    public static final class EquityInvestment {
        private EquityInvestment() {
        }

        /**
         * @return dividend yield % = annual dividends / share price * 100
         */
        public static double dividendYield(double sharePrice, double annualDividendPerShare) {
            return annualDividendPerShare / sharePrice * 100;
        }

        public static double dividendsPerPeriod(double dividendPerShare, Frequency frequency) {
            switch (frequency) {
                case ANNUAL,
                     SEMI_ANNUAL,
                     QUARTERLY,
                     MONTHLY -> {
                    return dividendPerShare / frequency.getNumber();
                }
                case null, default -> throw new IllegalArgumentException();
            }
        }

        /**
         * @return FV=P(1 + r/m)^(mt)
         */
        public static double drip(double moneyInvested, double dividendYield, Frequency compoundFrequency,
                                  byte numberOfYears) {
            final double frequency = compoundFrequency.getNumber();
            return moneyInvested * Math.pow((1 + dividendYield / frequency), frequency * numberOfYears);
        }
    }
}
