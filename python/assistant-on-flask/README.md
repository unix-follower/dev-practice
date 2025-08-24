### Install dependencies
```shell
make i
```
### Start server in debug mode
```shell
make run
```
### Define variables
```shell
export SERVER_URL=http://localhost:8000
#export SERVER_URL="$(minikube ip):8000"
```
### Health check
```shell
curl -v $SERVER_URL/healthz/live
curl -v $SERVER_URL/healthz/ready
```
### Predict API
```shell
curl -v $SERVER_URL/api/v1/predict \
 --header 'Content-Type: application/json' \
 --data '{
  "prices": [
    164.36000061035156,
    166.50999450683594,
    166.47000122070312,
    167.64999389648438
  ]
 }'
```
### Docker commands
#### Build an image
```shell
docker build -t assistant-on-flask:latest .
```
#### Run tensorflow in interactive mode
```shell
docker run -it --rm --name tensorflow tensorflow/tensorflow:2.16.2 /bin/bash
```
#### Run assistant-on-flask
```shell
docker run \
  --rm \
  --name assistant-on-flask \
  --hostname assistant-on-flask \
  --network local-bridge \
  --publish 8000:8000 \
  assistant-on-flask:latest
```
#### Connect to running container 
```shell
docker exec -it assistant-on-flask /bin/bash
```
