import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.thanos.contract.mockserver.controller.MockServerController;
import com.thanos.contract.mockserver.controller.RestApiController;
import com.thanos.contract.mockserver.domain.mapping.MockMappingCache;
import com.thanos.contract.mockserver.domain.mapping.MockMappingService;
import com.thanos.contract.mockserver.domain.mockserver.MockServerRepository;
import com.thanos.contract.mockserver.domain.mockserver.MockServerService;
import com.thanos.contract.mockserver.infrastructure.cache.FileBaseCacheRepoImpl;
import com.thanos.contract.mockserver.infrastructure.client.ContractRestClientRepoImpl;
import com.thanos.contract.mockserver.infrastructure.parser.PropertiesParser;
import io.muserver.Method;
import io.muserver.MuServer;
import io.muserver.rest.CORSConfigBuilder;
import io.muserver.rest.RestHandlerBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static io.muserver.MuServerBuilder.httpServer;

@Slf4j
public class MockServerMain {

    private static int httpPort = 8080;

    private static MuServer webServer;
    private static MockServerController mockServerController;
    private static MockMappingService mockMappingService;
    private static MockServerService mockServerService;
    private static MockServerRepository mockServerRepository;

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        try {
            //read properties
            PropertiesParser propertiesParser = new PropertiesParser();
            propertiesParser.init();


            // for diff mode
            if (PropertiesParser.isPlatformMode()) {
                mockServerRepository = new ContractRestClientRepoImpl();
            } else {
                mockServerRepository = new FileBaseCacheRepoImpl();
            }

            //prepare services
            mockMappingService = new MockMappingService(new MockMappingCache());
            mockServerService = new MockServerService(mockServerRepository);

            //startup restful web server
            startupWebServer(mockMappingService, mockServerService);

            //startup mockController
            mockServerController = new MockServerController(mockMappingService, mockServerService);
            mockServerController.initMock();

            printStartupLog();

        } catch (IOException e) {
            log.error("{}", e);
            log.error("IOException during startup, going to exit...");
            System.exit(5);
        } catch (Exception ex) {
            log.error("{}", ex);
            log.error("Exception during startup, going to exit...");
            System.exit(9);
        }
        addShutdownHook();
    }

    synchronized static void printStartupLog() {
        log.info("Startup as platform mode: {}", PropertiesParser.isPlatformMode());
        log.info("###########################################");
        log.info("            Service is up!                 ");
        log.info("###########################################");
    }

    static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Going to cleanup MockMapping cache ...");
            mockMappingService.cleanupMockMapping();
            log.info("Going to shutdown webServer ...");
            webServer.stop();
            log.info("###########################################");
            log.info("           Shut down complete !            ");
            log.info("###########################################");
        }));
    }

    private static void startupWebServer(MockMappingService mockMappingService,
                                         MockServerService mockServerService) throws IOException {
        PropertiesParser.getPropValues("port")
                .ifPresent(s -> httpPort = Integer.valueOf(s));

        webServer = httpServer()
                .withHttpPort(httpPort)
                .addHandler(RestHandlerBuilder.restHandler(
                        new RestApiController(mockMappingService, mockServerService))
                        .addCustomWriter(new JacksonJaxbJsonProvider())
                        .addCustomReader(new JacksonJaxbJsonProvider())
                        .withOpenApiJsonUrl("/openapi.json")
                        .withOpenApiHtmlUrl("/api.html").withCORS(
                                CORSConfigBuilder.corsConfig()
                                        .withAllowedOriginRegex("http(s)?://localhost:[0-9]+")
                        ))
                .addHandler(Method.GET, "/health", ((muRequest, muResponse, map) -> {
                    muResponse.write("UP");
                }))
                .addHandler(Method.GET, "/", ((muRequest, muResponse, map) -> {
                    muResponse.redirect("/api.html");
                })).start();

        log.info("Web Server started at " + webServer.uri());
    }

    public MockMappingService getMockMappingService() {
        return mockMappingService;
    }

    public MockServerService getMockServerService() {
        return mockServerService;
    }
}
