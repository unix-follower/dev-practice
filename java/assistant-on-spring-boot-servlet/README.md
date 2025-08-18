#### Define variables
```shell
assistant_on_sb_servlet_url=http://localhost:8080
```
## API calls
#### Get Actuator endpoints
```shell
curl -v ${assistant_on_sb_servlet_url}/actuator | jq
```
#### Get Prometheus metrics
```shell
curl -v ${assistant_on_sb_servlet_url}/actuator/prometheus
```
## Chemistry
### PubChem
#### Graph
##### Get all
```shell
curl -v "${assistant_on_sb_servlet_url}/api/v1/chemistry/graph/compound?page=1&pageSize=10" | jq
```
##### Get by name
```shell
curl -v "${assistant_on_sb_servlet_url}/api/v1/chemistry/graph/compound?page=1&pageSize=10&name=1-Amino-2-propanol" | jq
```
#### FDA
##### Get food additives
```shell
curl -v "${assistant_on_sb_servlet_url}/api/v1/chemistry/food-additives?page=1&pageSize=10" | jq
```
## Finance
### Stock market
#### Get stocks by ticker
```shell
curl -v "${assistant_on_sb_servlet_url}/api/v1/finance/stock-market?ticker=KO&page=1&pageSize=10" | jq
```
