import org.junit.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


public class StandaloneModeIntegrationTest extends IntegrationBase {

    private static final int RETRY_TIMES = 10;
    private static final String HOST = "127.0.0.1";

    @Test
    public void test_consumer1_schema1() throws Exception {
        final Integer port = getPort("provider-consumer1");
        if (port == null) fail("Fail to get port for mock provider-consumer1");
        System.out.println("Testing with mock provider-consumer1 : " + port);

        final String REQUEST = "00schema1123456123456789012345671";
        final String response = raiseRequest(port, REQUEST);

        assertTrue(response.startsWith("SUPPLEMENT"));
        assertTrue(response.endsWith("This is the expected memo for choice 1  "));

    }

    @Test
    public void test_consumer2_schema2() throws Exception {
        final Integer port = getPort("provider-consumer2");
        if (port == null) fail("Fail to get port for mock provider-consumer2");
        System.out.println("Testing with mock provider-consumer2 : " + port);

        final String REQUEST = "00schema2123456123456789012345672";
        final String response = raiseRequest(port, REQUEST);

        assertTrue(response.equalsIgnoreCase("SUPPLEMENT"));

    }

    private String raiseRequest(Integer port, String request) throws IOException {

        Socket socket = new Socket(HOST, port);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bw.write(request);
        bw.flush();
        socket.shutdownOutput();

        //读取服务器返回的消息
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String response = br.readLine();
        System.out.println("ProviderMain responsed：" + response);
        socket.close();

        return response;
    }

    private Integer getPort(String index) throws InterruptedException {
        Integer port = mockMappingService.getPortByIndex(index);
        int times = 1;
        while (port == null && times < RETRY_TIMES) {
            System.out.println("Retry to get port for " + index + " : " + +times + " times");
            Thread.sleep(1000);
            port = mockMappingService.getPortByIndex(index);
            times++;
        }
        return port;
    }

}
