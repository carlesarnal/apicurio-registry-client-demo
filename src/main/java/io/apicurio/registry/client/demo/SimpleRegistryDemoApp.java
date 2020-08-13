package io.apicurio.registry.client.demo;

import io.apicurio.registry.client.SimpleRegistryClient;
import io.apicurio.registry.client.SimpleRegistryService;
import io.apicurio.registry.client.demo.util.RegistryDemoUtil;
import io.apicurio.registry.rest.beans.ArtifactMetaData;
import io.apicurio.registry.rest.beans.IfExistsType;
import io.apicurio.registry.types.ArtifactType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;


/**
 * Simple demo app that shows how to use the client.
 * <p>
 * 1) Register a new schema in the Registry.
 * 2) Fetch the newly created schema.
 * 3) Delete the schema.
 *
 * @author Carles Arnal <carnalca@redhat.com>
 */
public class SimpleRegistryDemoApp {

    private static final SimpleRegistryService service;

    static {
        // Create a Service Registry client
        String registryUrl = "http://localhost:8080/api/";
        service = SimpleRegistryClient.create(registryUrl);
    }

    public static void main(String[] args) throws Exception {

        // Register the JSON Schema schema in the Apicurio registry.
        String artifactId = UUID.randomUUID().toString();

        RegistryDemoUtil.createSchemaInServiceRegistry(service, artifactId, Constants.SCHEMA);

        //Wait for the artifact to be available.
        Thread.sleep(1000);

        RegistryDemoUtil.getSchemaFromRegistry(service, artifactId);

        RegistryDemoUtil.deleteSchema(service, artifactId);

        System.exit(0);
    }
}
