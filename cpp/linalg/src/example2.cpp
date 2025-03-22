#include "constants.hpp"

#include <boost/numeric/ublas/matrix.hpp>
#include <boost/numeric/ublas/lu.hpp>
#include <boost/numeric/ublas/io.hpp>

#include <armadillo>
#include <iostream>
#include <sstream>
#include <cassert>

void solve_with_armadillo()
{
    std::cout << "Solve with Armadillo" << std::endl;

    arma::mat coefficient_matrix = {
        {3.0, 0.0, 0.0},
        {1.0, 5.0, -2.0},
        {1.0 / 3.0, 2.0, 0.0}};

    arma::vec constant_vector = {9.0, 2.0, 3.0};

    arma::vec result_vector = arma::solve(coefficient_matrix, constant_vector);

    const double x1 = result_vector(FIRST_COLUMN);
    const double x2 = result_vector(SECOND_COLUMN);
    const double x3 = result_vector(THIRD_COLUMN);
    std::cout << "x1 = " << x1 << std::endl;
    std::cout << "x2 = " << x2 << std::endl;
    std::cout << "x3 = " << x3 << std::endl;

    assert(static_cast<int>(round(x1)) == 3);
    assert(static_cast<int>(round(x2)) == 1);
    assert(static_cast<int>(round(x3)) == 3);
}

void solve_with_boost()
{
    std::cout << "Solve with Boost" << std::endl;

    namespace ublas = boost::numeric::ublas;

    ublas::matrix<double> coefficient_matrix(3, 3);
    coefficient_matrix(FIRST_ROW, FIRST_COLUMN) = 3;
    coefficient_matrix(FIRST_ROW, SECOND_COLUMN) = 0;
    coefficient_matrix(FIRST_ROW, THIRD_COLUMN) = 0;
    coefficient_matrix(SECOND_ROW, FIRST_COLUMN) = 1;
    coefficient_matrix(SECOND_ROW, SECOND_COLUMN) = 5;
    coefficient_matrix(SECOND_ROW, THIRD_COLUMN) = -2;
    coefficient_matrix(THIRD_ROW, FIRST_COLUMN) = 1 / 3.0;
    coefficient_matrix(THIRD_ROW, SECOND_COLUMN) = 2;
    coefficient_matrix(THIRD_ROW, THIRD_COLUMN) = 0;

    ublas::vector<double> constant_vector(3);
    constant_vector(FIRST_COLUMN) = 9;
    constant_vector(SECOND_COLUMN) = 2;
    constant_vector(THIRD_COLUMN) = 3;

    ublas::permutation_matrix<std::size_t> pm(coefficient_matrix.size1());
    ublas::lu_factorize(coefficient_matrix, pm);
    ublas::lu_substitute(coefficient_matrix, pm, constant_vector);

    const double x1 = constant_vector(FIRST_COLUMN);
    const double x2 = constant_vector(SECOND_COLUMN);
    const double x3 = constant_vector(THIRD_COLUMN);
    std::cout << "x1 = " << x1 << std::endl;
    std::cout << "x2 = " << x2 << std::endl;
    std::cout << "x3 = " << x3 << std::endl;

    assert(static_cast<int>(round(x1)) == 3);
    assert(static_cast<int>(round(x2)) == 1);
    assert(static_cast<int>(round(x3)) == 3);
}

int main(int argc, char *argv[])
{
    int demo_number = 0;
    if (argc >= 2)
    {
        std::string number_as_str = argv[SECOND_INDEX];
        std::istringstream(number_as_str) >> demo_number;
    }

    switch (demo_number)
    {
    case 1:
        solve_with_boost();
        break;
    case 2:
        solve_with_armadillo();
        break;

    default:
        solve_with_boost();
        solve_with_armadillo();
        break;
    }

    return 0;
}
