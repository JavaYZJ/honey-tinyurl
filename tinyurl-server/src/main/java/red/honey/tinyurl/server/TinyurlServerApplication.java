package red.honey.tinyurl.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import red.honey.redis.annotation.EnableHoneyRedis;

@SpringBootApplication
@EnableHoneyRedis
public class TinyurlServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TinyurlServerApplication.class, args);
    }

}
