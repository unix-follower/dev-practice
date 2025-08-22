### Install dependencies
```shell
npm i
```
### Start server in dev mode
```shell
npm run start
```
### Observability
```shell
curl -v http://localhost:9464/metrics
```
### Define variables
```shell
export SERVER_URL=localhost:3000
```
### MistralAI Chat API
```shell
curl -v $SERVER_URL/api/v1/ml/mistralai/chat | jq
curl -v $SERVER_URL/api/v1/ml/mistralai/chat?useTemplate=true | jq
curl -v $SERVER_URL/api/v1/ml/mistralai/chat/streaming | jq
```
