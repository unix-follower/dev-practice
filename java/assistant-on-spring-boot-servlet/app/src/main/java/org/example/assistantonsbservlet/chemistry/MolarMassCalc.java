package org.example.assistantonsbservlet.chemistry;

import org.example.assistantonsbservlet.api.ErrorCode;
import org.example.assistantonsbservlet.api.model.resp.CalculatorResponse;
import org.example.assistantonsbservlet.exception.ChemistryApiException;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.config.Elements;
import org.openscience.cdk.config.Isotopes;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.IntUnaryOperator;

public final class MolarMassCalc implements MolarMassCalculator {
    private String strategy;
    private String inputType;

    @Override
    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    @Override
    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    @Override
    public CalculatorResponse calculate(String input) {
        double molarMass;
        final var execStrategy = strategy != null ? strategy : "default";
        if (execStrategy.equals("custom")) {
            if (Constants.SMILES_IN_TYPE.equals(inputType)) {
                throw new UnsupportedOperationException();
            }
            molarMass = customCalculateFromFormula(input);
        } else {
            if (Constants.FORMULA_IN_TYPE.equals(inputType)) {
                molarMass = calculateFromFormula(input);
            } else {
                molarMass = calculateFromSmiles(input);
            }
        }

        return new CalculatorResponse(molarMass);
    }

    private double calculateFromSmiles(String smiles) {
        final var builder = DefaultChemObjectBuilder.getInstance();
        try {
            final var atomContainer = new SmilesParser(builder).parseSmiles(smiles);
            final var molecularFormula = MolecularFormulaManipulator.getMolecularFormula(atomContainer);
            return MolecularFormulaManipulator.getMass(molecularFormula);
        } catch (CDKException e) {
            throw new ChemistryApiException(e, ErrorCode.INVALID_INPUT);
        }
    }

    private double calculateFromFormula(String formula) {
        final var builder = DefaultChemObjectBuilder.getInstance();
        final var molecularFormula = MolecularFormulaManipulator.getMolecularFormula(formula, builder);
        return MolecularFormulaManipulator.getMass(molecularFormula);
    }

    private double customCalculateFromFormula(String formula) {
        final var stack = new ArrayDeque<Map<String, Integer>>();
        stack.push(new HashMap<>()); // Base level for the formula

        final BiFunction<Character, Integer, Integer> parseElementSymbol = (c, index) -> {
            final var element = new StringBuilder();
            element.append(c);

            int count = index;

            if (count + 1 < formula.length() && Character.isLowerCase(formula.charAt(count + 1))) {
                element.append(formula.charAt(count + 1));
                count++;
            }
            count++;

            // Parse quantity (subscript)
            int quantity = 0;
            while (count < formula.length() && Character.isDigit(formula.charAt(count))) {
                quantity = quantity * 10 + (formula.charAt(count) - '0');
                count++;
            }
            quantity = (quantity == 0) ? 1 : quantity;

            // Add element to the current level in the stack
            final var currentLevel = stack.peek();
            if (currentLevel != null) {
                currentLevel.put(element.toString(), currentLevel.getOrDefault(element.toString(), 0) + quantity);
            }

            return count;
        };

        final IntUnaryOperator endGroup = index -> {
            int count = index;
            count++;
            // Parse multiplier after closing parenthesis
            int multiplier = 0;
            while (count < formula.length() && Character.isDigit(formula.charAt(count))) {
                multiplier = multiplier * 10 + (formula.charAt(count) - '0');
                count++;
            }
            multiplier = (multiplier == 0) ? 1 : multiplier;

            // Apply multiplier to the group and merge into the previous level
            final var group = stack.pop();
            final var currentLevel = stack.peek();
            if (stack.isEmpty()) {
                throw new ChemistryApiException(ErrorCode.INVALID_INPUT);
            }
            for (final var entry : group.entrySet()) {
                currentLevel.put(entry.getKey(),
                    currentLevel.getOrDefault(entry.getKey(), 0) + entry.getValue() * multiplier);
            }
            return count;
        };

        int strIndex = 0;
        while (strIndex < formula.length()) {
            char c = formula.charAt(strIndex);

            if (Character.isUpperCase(c)) {
                strIndex = parseElementSymbol.apply(c, strIndex);

            } else if (c == '(') {
                // Start a new group
                stack.push(new HashMap<>());
                strIndex++;
            } else if (c == ')') {
                strIndex = endGroup.applyAsInt(strIndex);
            } else {
                strIndex++;
            }
        }

        if (stack.isEmpty()) {
            throw new ChemistryApiException(ErrorCode.INVALID_INPUT);
        }

        double molarMass = 0;

        for (final var entry : stack.peek().entrySet()) {
            final String element = entry.getKey();
            final int quantity = entry.getValue();
            final double atomicWeight = getAtomicWeight(element);
            molarMass += atomicWeight * quantity;
        }
        return molarMass;
    }

    private static double getAtomicWeight(String element) {
        try {
            final var isotopeFactory = Isotopes.getInstance();
            return isotopeFactory.getNaturalMass(Elements.ofString(element).number());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
