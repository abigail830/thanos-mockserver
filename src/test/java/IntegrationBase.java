import com.thanos.contract.mockserver.domain.mapping.MockMappingService;
import org.junit.BeforeClass;

public class IntegrationBase {

    static MockMappingService mockMappingService;

    @BeforeClass
    public static void beforeAll() throws Exception {
        final MockServerMain main = new MockServerMain();
        main.start();
        mockMappingService = main.getMockMappingService();
    }

}
