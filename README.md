# Run Locally using docker
* install `docker`
* install `jdk 11`
* install `maven` or use `mvnw` wrapper
* run mock-server locally (replace `<absolute-path>` )
```
mvn clean install
docker run -d -p 1080:1080 -e MOCKSERVER_PROPERTY_FILE=/config/mockserver.properties -e MOCKSERVER_INITIALIZATION_JSON_PATH=/config/initializerJson.json -v <absolute-path>/mockserver/config/mockserver/config:/config mockserver/mockserver:mockserver-5.11.1
mvn spring-boot:run
```
# Run using docker-compose
* install docker
* run `docker-compose up`

# mvn test
* install `jdk 11`
* install `maven` or use `mvnw` wrapper
* run `mvn clean test`

# Test
## Request
```
 curl -d '{"value":"test"}' -v pplication/json" -X POST http://localhost:8081/camel/api/data1/camel/api/data
```
## Response
``` json
{"info":{"correlationId":"afdd0c53-fa60-4df0-b587-4e025a34a81b","timestamp":"12-10-2020 10:01:56"},"data":{"value":"mock service response"},"errors":[]}
```