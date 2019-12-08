package practice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import practice.netty.NettyServer;

@SpringBootApplication
public class PracticeApplication {

	@Autowired
	private ApplicationContext context;

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(PracticeApplication.class, args);
		NettyServer nettyServer = context.getBean(NettyServer.class);
		nettyServer.start();
	}

}
