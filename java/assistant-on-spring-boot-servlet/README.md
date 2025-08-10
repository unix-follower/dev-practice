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
### PubChem FDA
#### Get food additives
```shell
curl -v "${assistant_on_sb_servlet_url}/api/v1/chemistry/food-additives?page=1&pageSize=10" | jq
```
## Finance
### Stock market
#### Get stocks by ticker
```shell
curl -v "${assistant_on_sb_servlet_url}/api/v1/finance/stock-market?ticker=KO&page=1&pageSize=10" | jq
```
