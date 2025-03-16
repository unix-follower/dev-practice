.PHONY: test

install:
	pip install -r requirements.txt

test:
	python -m unittest -v --locals

coverage:
	coverage run --source=src -m unittest -v --locals
	coverage report -m
	coverage html

pylint:
	pylint ./src

pylint-generate-rcfile:
	pylint --generate-rcfile > .pylintrc

flake8:
	flake8 .
