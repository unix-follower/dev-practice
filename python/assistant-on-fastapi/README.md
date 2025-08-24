# Assistant on FastAPI
### Run locally
To run the application in dev mode with plain text logging mode (default is json)
```shell
make run-dev
```
## Define variables
```shell
export SERVER_URL=http://localhost:8000
```
To verify it is running
```shell
curl -v $SERVER_URL/health/liveness
curl -v $SERVER_URL/health/readiness
curl -v $SERVER_URL/version
```
## OpenAPI
```shell
curl $SERVER_URL/openapi.json
curl $SERVER_URL/docs
curl $SERVER_URL/redoc
```
## Food predict API
```shell
curl -v $SERVER_URL/api/v1/chemistry/ml/food/predict \
  --header 'Content-Type: application/json' \
  --data '{
  "model": "foodb-3feat",
  "feature": {
      "name": "Herbal tea",
      "description": "is not specified",
      "category": "unknown"
  }
}' | jq
```
