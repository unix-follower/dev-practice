#include "constants.hpp"

#include <boost/numeric/ublas/matrix.hpp>
#include <boost/numeric/ublas/lu.hpp>

#include <armadillo>
#include <iostream>
#include <sstream>
#include <exception>
#include <cassert>

void solve_with_armadillo()
{
    std::cout << "Solve with Armadillo" << std::endl;

    arma::mat coefficient_matrix = {{3, 2}, {-1, 1}};
    arma::vec constant_vector = {7, 6};

    arma::vec result_vector = arma::solve(coefficient_matrix, constant_vector);

    const double x1 = result_vector(FIRST_COLUMN);
    const double x2 = result_vector(SECOND_COLUMN);
    std::cout << "x1 = " << x1 << std::endl;
    std::cout << "x2 = " << x2 << std::endl;

    assert(x1 == -1);
    assert(x2 == 5);
}

void solve_with_boost()
{
    std::cout << "Solve with Boost" << std::endl;

    using namespace boost::numeric::ublas;

    matrix<double> coefficient_matrix(2, 2);
    coefficient_matrix(FIRST_ROW, FIRST_COLUMN) = 3;
    coefficient_matrix(FIRST_ROW, SECOND_COLUMN) = 2;
    coefficient_matrix(SECOND_ROW, FIRST_COLUMN) = -1;
    coefficient_matrix(SECOND_ROW, SECOND_COLUMN) = 1;

    vector<double> constant_vector(2);
    constant_vector(FIRST_COLUMN) = 7;
    constant_vector(SECOND_COLUMN) = 6;

    permutation_matrix<std::size_t> perm_matrix(coefficient_matrix.size1());
    int lu_factorize_result = lu_factorize(coefficient_matrix, perm_matrix);
    if (lu_factorize_result != 0)
    {
        throw std::runtime_error("LU factorization failed");
    }

    vector<double> result_vector(constant_vector);
    lu_substitute(coefficient_matrix, perm_matrix, result_vector);

    const double x1 = result_vector(FIRST_COLUMN);
    const double x2 = result_vector(SECOND_COLUMN);
    std::cout << "x1 = " << x1 << std::endl;
    std::cout << "x2 = " << x2 << std::endl;

    assert(x1 == -1);
    assert(x2 == 5);
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
