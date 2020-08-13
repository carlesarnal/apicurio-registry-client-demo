package io.apicurio.registry.client.demo;

import io.apicurio.registry.client.RegistryService;
import io.apicurio.registry.rest.beans.ArtifactMetaData;
import io.apicurio.registry.rest.beans.IfExistsType;
import io.apicurio.registry.types.ArtifactType;
import io.registry.client.CompatibleClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletionStage;


/**
 * Simple demo app that shows how to use the client.
 *
 * 1) Register a new schema in the Registry.
 * 2) Fetch the newly created schema.
 * 3) Delete the schema.
 *
 *
 * @author eric.wittmann@gmail.com
 */
public class RegistryDemoApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistryDemoApp.class);

    private static RegistryService service;

    static {
        // Create a Service Registry client
        String registryUrl = "http://localhost:8080/api";
        service = CompatibleClient.createCompatible(registryUrl);
    }

    public static void main(String[] args) throws Exception {
        try {
            LOGGER.info("\n\n--------------\nBootstrapping the JSON Schema demo.\n--------------\n");

            // Register the JSON Schema schema in the Apicurio registry.

            String artifactId = UUID.randomUUID().toString();

            try {
                createSchemaInServiceRegistry(artifactId, Constants.SCHEMA);
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
            service.close();
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
            CompletionStage<ArtifactMetaData> artifact = service.createArtifact(ArtifactType.JSON, artifactId, IfExistsType.RETURN, content);
            ArtifactMetaData metaData = artifact.toCompletableFuture().get();
            LOGGER.info("=====> Successfully created JSON Schema artifact in Service Registry: {}", metaData);
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
