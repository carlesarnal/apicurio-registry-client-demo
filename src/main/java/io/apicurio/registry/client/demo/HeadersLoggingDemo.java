package io.apicurio.registry.client.demo;

import io.apicurio.registry.client.SimpleRegistryClient;
import io.apicurio.registry.client.SimpleRegistryService;
import io.apicurio.registry.client.demo.util.RegistryDemoUtil;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

/**
 * Simple demo app that shows how to use the client and improve the logs.
 * <p>
 * 1) Register a new schema in the Registry.
 * 2) Fetch the newly created schema.
 * 3) Delete the schema.
 *
 * @author Carles Arnal <carnalca@redhat.com>
 */
public class HeadersLoggingDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeadersLoggingDemo.class);

    private static final SimpleRegistryService service;

    static {
        // Create a Service Registry client
        final String registryUrl = "http://localhost:8080/api/";

        final OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new HeadersInterceptor())
                .build();

        service = SimpleRegistryClient.create(registryUrl, httpClient);
    }

    public static void main(String[] args) throws Exception {

        // Register the JSON Schema schema in the Apicurio registry.
        final String artifactId = UUID.randomUUID().toString();

        RegistryDemoUtil.createSchemaInServiceRegistry(service, artifactId, Constants.SCHEMA);

        //Wait for the artifact to be available.
        Thread.sleep(1000);

        RegistryDemoUtil.getSchemaFromRegistry(service, artifactId);

        RegistryDemoUtil.deleteSchema(service, artifactId);

        System.exit(0);
    }

    private static final class HeadersInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {

            final Request request = chain.request();
            final Headers requestHeaders = request.headers();

            for (String name : requestHeaders.names()) {
                LOGGER.info("Request header with name: {} and value: {}", name, requestHeaders.get(name));
            }

            final Response response = chain.proceed(request);
            final Headers responseHeaders = response.headers();

            for (String name : responseHeaders.names()) {
                LOGGER.info("Response header with name: {} and value: {}", name, responseHeaders.get(name));
            }

            return response;
        }
    }
}
