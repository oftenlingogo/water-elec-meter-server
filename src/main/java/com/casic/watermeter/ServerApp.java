package com.casic.watermeter;

import java.net.InetSocketAddress;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.casic.watermeter.codec.WaterMeterCodecFactory;
import com.casic.watermeter.codec.WaterMeterDecoder;
import com.casic.watermeter.codec.WaterMeterEncoder;
import com.casic.watermeter.handler.ServerHandler;

/**
 * Hello world!
 */
public class ServerApp 
{
	private static int PORT = 6666;
    public static void main( String[] args )
    {
    	 //ClassPathXmlApplicationContext ct =	new ClassPathXmlApplicationContext("applicationContext-mina.xml");
    	 IoAcceptor acceptor = null;
         try {
             //鍒涘缓涓�釜闈為樆濉炵殑server绔殑Socket
             acceptor = new NioSocketAcceptor();
             // 璁剧疆杩囨护鍣紙娣诲姞鑷甫鐨勭紪瑙ｇ爜鍣級
             acceptor.getFilterChain().addLast(
                     "codec",
                     new ProtocolCodecFilter(new WaterMeterCodecFactory(
                     new WaterMeterDecoder(),new WaterMeterEncoder() )));
             // 璁剧疆鏃ュ織杩囨护鍣�
             LoggingFilter lf = new LoggingFilter();
             lf.setMessageReceivedLogLevel(LogLevel.DEBUG);
             acceptor.getFilterChain().addLast("logger", lf);
             // 鑾峰緱IoSessionConfig瀵硅薄
             IoSessionConfig cfg = acceptor.getSessionConfig();
             // 璇诲啓閫氶亾10绉掑唴鏃犳搷浣滆繘鍏ョ┖闂茬姸鎬�
             cfg.setIdleTime(IdleStatus.BOTH_IDLE, 100);
             // 缁戝畾閫昏緫澶勭悊鍣�
             acceptor.setHandler(new ServerHandler());
             // 缁戝畾绔彛
             acceptor.bind(new InetSocketAddress(PORT));
             System.out.println("鎴愬姛寮�惎鏈嶅姟鍣ㄧ...");
         } catch (Exception e) {
             e.printStackTrace();
         }
    }
}
