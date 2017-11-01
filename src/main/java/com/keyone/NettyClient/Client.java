package com.keyone.NettyClient;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.keyone.codc.RequestEncoder;
import com.keyone.codc.ResponseDecoder;
import com.keyone.model.Request;
import com.keyone.serial.Serializer;
/**
 * netty客户端
 * @author -clearMarkCC-
 *
 */
public class Client {
	public static String IP = "";
	public static <T extends Serializer> void Send(Request re,T obj) throws InterruptedException {
		
		//服务类
		ClientBootstrap bootstrap = new  ClientBootstrap();
		
		//线程池
		ExecutorService boss = Executors.newCachedThreadPool();
		ExecutorService worker = Executors.newCachedThreadPool();
		
		//socket工厂
		bootstrap.setFactory(new NioClientSocketChannelFactory(boss, worker));
		
		//管道工厂
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();
				pipeline.addLast("decoder", new ResponseDecoder());
				pipeline.addLast("encoder", new RequestEncoder());
				pipeline.addLast("clientHandler", new ClientHandler());
				return pipeline;
			}
			
		});
		
		//连接服务端
		ChannelFuture connect = bootstrap.connect(new InetSocketAddress(IP, 10101));
		Channel channel = connect.sync().getChannel();
		
		System.out.println("client start");
		
		//发送请求的封装
		re.setData(obj.getBytes());
		
		//写入
		channel.write(re);
	}

}
