# Apicurio Registry Client Demo

Simple Java application that shows how to use the Apicurio Registry Client.

## How to run the demo

* Clone and build Apicurio registry project: https://github.com/Apicurio/apicurio-registry

`git clone git@github.com:Apicurio/apicurio-registry.git`

`mvn clean install -DskipTests -Pasyncmem`

* Run one instance of registry

`java -jar -Dquarkus.profile=dev /home/carles/IdeaProjects/apicurio-registry/storage/asyncmem/target/apicurio-registry-storage-asyncmem-1.2.3-SNAPSHOT-runner.jar`

* Run demo's Main (from IDE)

`https://github.com/carlesarnal/apicurio-registry-client-demo/blob/master/src/main/java/io/apicurio/registry/client/demo/RegistryDemoApp.java`
