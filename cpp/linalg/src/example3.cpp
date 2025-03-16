#include "constants.hpp"

#include <boost/numeric/ublas/matrix.hpp>
#include <boost/numeric/ublas/lu.hpp>
#include <boost/numeric/ublas/io.hpp>

#include <armadillo>
#include <iostream>
#include <exception>
#include <sstream>
#include <cassert>

void solve_with_armadillo()
{
  std::cout << "Solve with Armadillo" << std::endl;

  arma::mat coefficient_matrix = {
      {1.0, 1.0, 0.0},
      {2.0, -1.0, 3.0},
      {1.0, -2.0, -1.0}};

  arma::vec constant_vector = {0.0, 3.0, 3.0};

  arma::vec result_vector = arma::solve(coefficient_matrix, constant_vector);

  const double x = result_vector(FIRST_COLUMN);
  const double y = result_vector(SECOND_COLUMN);
  const double z = result_vector(THIRD_COLUMN);
  std::cout << "x = " << x << std::endl;
  std::cout << "y = " << y << std::endl;
  std::cout << "z = " << z << std::endl;

  assert(static_cast<int>(round(x)) == 1);
  assert(static_cast<int>(round(y)) == -1);
  assert(static_cast<int>(round(z)) == 0);
}

namespace ublas = boost::numeric::ublas;

template <typename T>
bool lu_solve(ublas::matrix<T> &A, ublas::vector<T> &b, ublas::vector<T> &x)
{
  ublas::permutation_matrix<std::size_t> pm(A.size1());
  bool is_singular_matrix = ublas::lu_factorize(A, pm);
  if (is_singular_matrix)
  {
    return false;
  }
  x.assign(b);
  ublas::lu_substitute(A, pm, x);
  return true;
}

void solve_with_boost()
{
  std::cout << "Solve with Boost" << std::endl;

  ublas::matrix<double> coefficient_matrix(3, 3);
  coefficient_matrix(FIRST_ROW, FIRST_COLUMN) = 1;
  coefficient_matrix(FIRST_ROW, SECOND_COLUMN) = 1;
  coefficient_matrix(FIRST_ROW, THIRD_COLUMN) = 0;
  coefficient_matrix(SECOND_ROW, FIRST_COLUMN) = 2;
  coefficient_matrix(SECOND_ROW, SECOND_COLUMN) = -1;
  coefficient_matrix(SECOND_ROW, THIRD_COLUMN) = 3;
  coefficient_matrix(THIRD_ROW, FIRST_COLUMN) = 1;
  coefficient_matrix(THIRD_ROW, SECOND_COLUMN) = -2;
  coefficient_matrix(THIRD_ROW, THIRD_COLUMN) = -1;

  ublas::vector<double> constant_vector(3);
  constant_vector(FIRST_COLUMN) = 0;
  constant_vector(SECOND_COLUMN) = 3;
  constant_vector(THIRD_COLUMN) = 3;

  ublas::vector<double> result_vector(3);

  bool can_solve = lu_solve(coefficient_matrix, constant_vector, result_vector);
  if (!can_solve)
  {
    throw std::runtime_error("The matrix is singular and cannot be solved.");
  }

  const double x = result_vector(FIRST_COLUMN);
  const double y = result_vector(SECOND_COLUMN);
  const double z = result_vector(THIRD_COLUMN);
  std::cout << "x = " << x << std::endl;
  std::cout << "y = " << y << std::endl;
  std::cout << "z = " << z << std::endl;

  assert(static_cast<int>(round(x)) == 1);
  assert(static_cast<int>(round(y)) == -1);
  assert(static_cast<int>(round(z)) == 0);
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
