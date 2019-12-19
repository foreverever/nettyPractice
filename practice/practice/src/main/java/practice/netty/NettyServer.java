package practice.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:/application.properties")    //프로퍼티 설정값 가져오기
public class NettyServer {

    @Value("${tcp.port}")
    private int tcpPort;

    @Value("${boss.thread.count}")
    private int bossCount;

    @Value("${worker.thread.count}")
    private int workerCount;

    @Autowired
    private NettyServerInitializer nettyServerInitializer;

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)                              //서버 소켓 입출력 모드를 NIO로 설정
                    .childHandler(nettyServerInitializer);

            ChannelFuture channelFuture = b.bind(tcpPort).sync();   //서버를 비동기식으로 바인딩, sync()은 바인딩이 완료되기를 대기
            channelFuture.channel().closeFuture().sync().channel();   //채널의 closeFuture을 얻고 완료될 때가지 현재 스레드를 블로킹 요청기다림
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //이벤트루프그룹을 종료 및 모든 리소스 해제
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

//초기화 순서//
//클라이언트가 서버 소켓에 접속 요청
//해당 연결에 대응하는 클라이언트 소켓 채널 객체 생성
//빈 채널 파이프라인 객체 생성, 클라이언트 소켓 채널에 할당
//클라이언트 소켓 채널에 등록된 ChannelInitalizer 객체를 가져와서 initChannel 호출
//클라이언트 소켓 채널에 할당되어있는 파이프라인을 가져와서 이벤트 핸들러 등록