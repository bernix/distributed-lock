package my.bning.distributedlock.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author Bernix Ning
 * @since 2019-02-15
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DemoControllerTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void testDistributedLock() {
        String url = "http://localhost:8080/distributed-lock";
        String uuid = "abcdefg";
        String key = "distributed-lock-test";
        String seconds2live = "10";
        for (int i = 0; i < 10; i++) {
            final int userId = i;
            new Thread(() -> {
                MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                params.add("key", key);
                params.add("uuid", uuid);
                params.add("secondsToLock", seconds2live);
                params.add("userId", String.valueOf(userId));
                String result = testRestTemplate.postForObject(url, params, String.class);
                System.out.println("----------------- " + result);
            }).start();
        }
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDistributedLimit() {
        String url = "http://localhost:8080/distributed-limit";
        for (int i = 0; i < 50; i++) {
            final int userId = i;
            new Thread(() -> {
                MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                params.add("userId", String.valueOf(userId));
                String result = testRestTemplate.postForObject(url, params, String.class);
                System.out.println("----------------- " + result);
            }).start();
        }
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
