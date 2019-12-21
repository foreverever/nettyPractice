package practice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import practice.netty.NettyServer;

@EnableScheduling
@SpringBootApplication
public class PracticeApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(PracticeApplication.class, args);
		NettyServer nettyServer = context.getBean(NettyServer.class);
		nettyServer.start();
	}
}
