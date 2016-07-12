package com.casic.watermeter;

import java.net.InetSocketAddress;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import com.casic.watermeter.codec.WaterMeterCodecFactory;
import com.casic.watermeter.codec.WaterMeterDecoder;
import com.casic.watermeter.codec.WaterMeterEncoder;
import com.casic.watermeter.handler.ClientHandler;
import com.casic.watermeter.msg.Msg;

/**
 *
 */
public class ClientApp {
	private static String HOST = "127.0.0.1";

	private static int PORT = 6666;

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		// 创建一个非阻塞的客户端程序
		IoConnector connector = new NioSocketConnector();
		// 设置链接超时时间
		connector.setConnectTimeout(30000);
		// 添加过滤器

		// 添加过滤器
		connector.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new WaterMeterCodecFactory(new WaterMeterDecoder(), new WaterMeterEncoder())));
		// 添加业务逻辑处理器类
		connector.setHandler(new ClientHandler());
		
		IoSession session = null;
		try {
			ConnectFuture future = connector.connect(new InetSocketAddress(HOST, PORT));// 创建连接
			future.awaitUninterruptibly();// 等待连接创建完成
			session = future.getSession();// 获得session
			Msg msg = new Msg();
			session.write(msg);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("客户端链接异常...");
		}

		session.getCloseFuture().awaitUninterruptibly();// 等待连接断开
		connector.dispose();
	}

	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}
}
