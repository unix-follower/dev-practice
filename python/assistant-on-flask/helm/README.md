### Install chart
```shell
helm install assistant ./assistant-on-flask \
  -n assistant \
  --values ./assistant-on-flask/values.yaml
```
### Show manifest
```shell
helm get manifest -n assistant assistant-on-flask
```
### Get pod logs
```shell
kubectl logs -n assistant assistant-on-flask-<ID>
```
### Describe pod
```shell
kubectl describe -n assistant pod/assistant-on-flask-<ID>
```
### Get pod events
```shell
kubectl events -n assistant assistant-on-flask-<ID>
```
### Verify external access
```shell
nc -vz $(minikube ip) 8000
```
### Stop finance predictor
```shell
kubectl -n assistant scale deployment assistant-on-flask --replicas 0
```
### Uninstall chart
```shell
helm uninstall -n assistant assistant-on-flask
```
