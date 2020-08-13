package io.apicurio.registry.client.demo;

import io.apicurio.registry.rest.beans.ArtifactMetaData;
import io.apicurio.registry.rest.beans.IfExistsType;
import io.apicurio.registry.types.ArtifactType;
import io.registry.client.RegistryClient;
import io.registry.client.RegistryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;


/**
 * Simple demo app that shows how to use the client.
 *
 * 1) Register a new schema in the Registry.
 * 2) Fetch the newly created schema.
 * 3) Delete the schema.
 *
 *
 * @author Carles Arnal <carnalca@redhat.com>
 */
public class RegistryDemoApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistryDemoApp.class);

    private static final RegistryService service;

    static {
        // Create a Service Registry client
        String registryUrl = "http://localhost:8080/api";
        service = RegistryClient.create(registryUrl);
    }

    public static void main(String[] args) throws Exception {
        try {
            LOGGER.info("\n\n--------------\nBootstrapping the JSON Schema demo.\n--------------\n");

            // Register the JSON Schema schema in the Apicurio registry.

            String artifactId = UUID.randomUUID().toString();

            try {
                createSchemaInServiceRegistry(artifactId, Constants.SCHEMA);

                getSchemaFromRegistry(artifactId);

                deleteSchema(artifactId);

            } catch (Exception e) {
                if (is409Error(e)) {
                    LOGGER.warn("\n\n--------------\nWARNING: Schema already existed in registry!\n--------------\n");
                    return;
                } else {
                    throw e;
                }
            }

            LOGGER.info("\n\n--------------\nBootstrapping complete.\n--------------\n");
        } finally {
        }
    }

    /**
     * Create the artifact in the registry (or update it if it already exists).
     *
     * @param artifactId
     * @param schema
     * @throws Exception
     */
    private static void createSchemaInServiceRegistry(String artifactId, String schema) throws Exception {

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
     * @throws Exception
     */
    private static ArtifactMetaData getSchemaFromRegistry(String artifactId) throws Exception {

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
     * @throws Exception
     */
    private static void deleteSchema(String artifactId) throws Exception {

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

    private static boolean is409Error(Exception e) {
        if (e.getCause() instanceof WebApplicationException) {
            WebApplicationException wae = (WebApplicationException) e.getCause();
            if (wae.getResponse().getStatus() == 409) {
                return true;
            }
        }
        return false;
    }
}
