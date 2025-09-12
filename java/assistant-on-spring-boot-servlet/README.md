#### Define variables
```shell
export SERVER_URL=http://localhost:8080
```
## API calls
#### Get Actuator endpoints
```shell
curl -v $SERVER_URL/actuator | jq
```
#### Get Prometheus metrics
```shell
curl -v $SERVER_URL/actuator/prometheus
```
## Math
### Triangle calculator
#### Solve for sides
```shell
curl -v $SERVER_URL/api/v1/math/calculator/right-triangle \
  --header 'Content-Type: application/json' \
  --data '{"cathetusA": 3, "cathetusB": 4}' | jq
```
#### Solve for leg a and hypotenuse
```shell
curl -v $SERVER_URL/api/v1/math/calculator/right-triangle \
  --header 'Content-Type: application/json' \
  --data '{"cathetusA": 3, "hypotenuse": 5}' | jq
```
#### Solve for leg b and hypotenuse
```shell
curl -v $SERVER_URL/api/v1/math/calculator/right-triangle \
  --header 'Content-Type: application/json' \
  --data '{"cathetusB": 4, "hypotenuse": 5}' | jq
```
#### Try to solve for invalid hypotenuse
```shell
curl -v $SERVER_URL/api/v1/math/calculator/right-triangle \
  --header 'Content-Type: application/json' \
  --data '{"cathetusB": 6, "hypotenuse": 5}' | jq
```
### Hypotenuse calculator
#### Solve for side and opposite angle
```shell
curl -v $SERVER_URL/api/v1/math/calculator/hypotenuse \
  --header 'Content-Type: application/json' \
  --data '{"solveFor": "side_and_opposite_angle", "cathetusA": 3, "angleAlpha": 60, "alphaAngleUnit": "degrees"}' | jq
```
#### Solve for side and adjacent angle
```shell
curl -v $SERVER_URL/api/v1/math/calculator/hypotenuse \
  --header 'Content-Type: application/json' \
  --data '{"solveFor": "side_and_adjacent_angle", "cathetusA": 3, "angleBeta": 30, "betaAngleUnit": "degrees"}' | jq
```
#### Solve for area and one side
```shell
curl -v $SERVER_URL/api/v1/math/calculator/hypotenuse \
  --header 'Content-Type: application/json' \
  --data '{"solveFor": "area_and_side", "cathetusA": 3, "area": 3.464}' | jq
```
## Chemistry
### PubChem
#### Compound
##### Get all graphs
```shell
curl -v "$SERVER_URL/api/v1/chemistry/compound/graph?page=1&pageSize=10" | jq
```
###### Get graph data by name
```shell
curl -v "$SERVER_URL/api/v1/chemistry/compound/graph?page=1&pageSize=10&name=1-Amino-2-propanol" | jq
```
###### Get SDF data by cid
```shell
curl -v $SERVER_URL/api/v1/chemistry/compound/4 | jq
```
#### Calculator
###### Calculate molar mass by formula
```shell
curl -v $SERVER_URL/api/v1/chemistry/calculator/molar-mass \
  --header 'Content-Type: application/json' \
  --data '{"formula": "H2O"}' | jq
```
```shell
curl -v $SERVER_URL/api/v1/chemistry/calculator/molar-mass \
  --header 'Content-Type: application/json' \
  --data '{"formula": "H2O", "strategy": "custom"}' | jq
```
###### Calculate molar mass by smiles
```shell
curl -v $SERVER_URL/api/v1/chemistry/calculator/molar-mass \
  --header 'Content-Type: application/json' \
  --data '{"smiles": "[OH2]"}' | jq
```
###### Calculate mole by formula
```shell
curl -v $SERVER_URL/api/v1/chemistry/calculator/mole \
  --header 'Content-Type: application/json' \
  --data '{"formula": "HCl", "mass": 10}' | jq
```
###### Calculate mole by smiles
```shell
curl -v $SERVER_URL/api/v1/chemistry/calculator/mole \
  --header 'Content-Type: application/json' \
  --data '{"smiles": "[H]Cl", "mass": 10}' | jq
```
###### Calculate mole by mass and molecular weight
```shell
curl -v $SERVER_URL/api/v1/chemistry/calculator/mole \
  --header 'Content-Type: application/json' \
  --data '{"mass": 10, "molecularWeight": 36.5}' | jq
```
#### FDA
##### Get food additives
```shell
curl -v "$SERVER_URL/api/v1/chemistry/food-additives?page=1&pageSize=10" | jq
```
## Finance
### Stock market
#### Get stocks by ticker
```shell
curl -v "$SERVER_URL/api/v1/finance/stock-market?ticker=KO&page=1&pageSize=10" | jq
```
