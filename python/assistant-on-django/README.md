### Install dependencies
```shell
pipenv install
```
### Start application
```shell
python manage.py runserver
```
### Test API
```shell
port=8000
```
```shell
curl -v http://localhost:${port}/api/v1/water/calculate-molecules?milliliters=100
```
```shell
curl -v http://localhost:${port}/api/v1/nitrogen/calculate-mass?molecules=1
```
