package io.apicurio.registry.client.demo;

import io.apicurio.registry.client.SimpleRegistryClient;
import io.apicurio.registry.client.SimpleRegistryService;
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
public class RegistryDemoApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistryDemoApp.class);

    private static final SimpleRegistryService service;

    static {
        // Create a Service Registry client
        String registryUrl = "http://localhost:8080/api/";
        service = SimpleRegistryClient.create(registryUrl);
    }

    public static void main(String[] args) throws Exception {

        // Register the JSON Schema schema in the Apicurio registry.
        String artifactId = UUID.randomUUID().toString();

        createSchemaInServiceRegistry(artifactId, Constants.SCHEMA);

        //Wait for the artifact to be available.
        Thread.sleep(1000);

        getSchemaFromRegistry(artifactId);

        deleteSchema(artifactId);

        System.exit(0);
    }

    /**
     * Create the artifact in the registry (or update it if it already exists).
     *
     * @param artifactId
     * @param schema
     */
    private static void createSchemaInServiceRegistry(String artifactId, String schema) {

        LOGGER.info("---------------------------------------------------------");
        LOGGER.info("=====> Creating artifact in the registry for JSON Schema with ID: {}", artifactId);
        try {
            ByteArrayInputStream content = new ByteArrayInputStream(schema.getBytes(StandardCharsets.UTF_8));
            ArtifactMetaData metaData = service.createArtifact(ArtifactType.JSON, artifactId, IfExistsType.RETURN, content);
            assert metaData != null;
            LOGGER.info("=====> Successfully created JSON Schema artifact in Service Registry: {}", metaData);
            LOGGER.info("---------------------------------------------------------");
        } catch (Exception t) {
            throw t;
        }
    }

    /**
     * Get the artifact from the registry.
     *
     * @param artifactId
     */
    private static ArtifactMetaData getSchemaFromRegistry(String artifactId) {

        LOGGER.info("---------------------------------------------------------");
        LOGGER.info("=====> Fetching artifact from the registry for JSON Schema with ID: {}", artifactId);
        try {
            ArtifactMetaData metaData = service.getArtifactMetaData(artifactId);
            assert metaData != null;
            LOGGER.info("=====> Successfully fetched JSON Schema artifact in Service Registry: {}", metaData);
            LOGGER.info("---------------------------------------------------------");
            return metaData;
        } catch (Exception t) {
            throw t;
        }
    }

    /**
     * Delete the artifact from the registry.
     *
     * @param artifactId
     */
    private static void deleteSchema(String artifactId) {

        LOGGER.info("---------------------------------------------------------");
        LOGGER.info("=====> Deleting artifact from the registry for JSON Schema with ID: {}", artifactId);
        try {
            service.deleteArtifact(artifactId);
            LOGGER.info("=====> Successfully deleted JSON Schema artifact in Service Registry.");
            LOGGER.info("---------------------------------------------------------");
        } catch (Exception t) {
            throw t;
        }
    }
}
