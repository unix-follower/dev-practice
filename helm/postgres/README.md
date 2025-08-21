#### Add repository
```shell
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update
```
### Install Postgres
```shell
export PGPASSWORD=<PUT PASSWORD HERE>
./install-postgres.sh
```
### Upgrade Postgres chart
```shell
helm upgrade postgres17 -n assistant --values postgres-values.yaml oci://registry-1.docker.io/bitnamicharts/postgresql
```
### Show Postgres manifest
```shell
helm get manifest postgres17 -n assistant
```
### Uninstall Postgres
```shell
helm uninstall -n assistant postgres17
```
### Stop Postgres
```shell
kubectl -n assistant scale sts postgres17-postgresql --replicas 0
```
### Get pod logs
```shell
kubectl logs -n assistant postgres17-postgresql-0
```
### Get pod events
```shell
kubectl events -n assistant postgres17-postgresql-0
```
### Get Persistent Volumes
```shell
kubectl get pvc -n assistant
kubectl get pv
```
### Verify external access
```shell
nc -vz $(minikube ip) 32000
```
### Connect
```shell
psql --host $(minikube ip) -U postgres -p 32000
```
