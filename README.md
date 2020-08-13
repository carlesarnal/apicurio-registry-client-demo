# Apicurio Registry Client Demo

## How to run the demo

* Clone and build Apicurio registry project: https://github.com/Apicurio/apicurio-registry

`git clone git@github.com:Apicurio/apicurio-registry.git`

`mvn clean install -DskipTests `

* Run two instances of registry

`java -jar -Dquarkus.profile=dev /home/carles/IdeaProjects/apicurio-registry/storage/asyncmem/target/apicurio-registry-storage-asyncmem-1.2.3-SNAPSHOT-runner.jar`

`java -jar -Dquarkus.profile=dev -Dquarkus.http.port=8081 /home/carles/IdeaProjects/apicurio-registry/storage/asyncmem/target/apicurio-registry-storage-asyncmem-1.2.3-SNAPSHOT-runner.jar`

* Run demo's Main (from IDE)

`https://github.com/alesj/registry-demo/blob/master/src/main/java/io/apicurio/registry/demo/Main.java`

* Run demo's TestMain (from IDE)

`https://github.com/alesj/registry-demo/blob/master/src/test/java/io/apicurio/registry/test/TestMain.java`
