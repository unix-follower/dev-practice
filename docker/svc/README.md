### Prerequisites
Manually copy the config files into Minikube.
```shell
minikube cp ./prometheus.yml /tmp/prometheus.yml
minikube cp ./otel-collector-config.yaml /tmp/otel-collector-config.yaml
```
### Start containers
```shell
docker-compose up -d
```
### Delete containers
```shell
docker-compose down -v
```
### Verify access
```shell
curl http://192.168.105.8:16686
curl http://192.168.105.8:9090
nc -vz $(minikube ip) 16686
```
