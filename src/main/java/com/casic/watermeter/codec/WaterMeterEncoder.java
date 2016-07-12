package com.casic.watermeter.codec;


import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;
import com.casic.watermeter.msg.Msg;


public class WaterMeterEncoder implements MessageEncoder<Msg> {
	


	@Override
	public void encode(IoSession session, Msg msg, ProtocolEncoderOutput out) {
		// TODO Auto-generated method stub
		 IoBuffer buf=IoBuffer.allocate(1024).setAutoExpand(true);
		 
		 buf.put(hexStringToBytes("fefe6899163600000068"));
		 buf.flip();
	     out.write(buf);
	}
	
	public  byte[] hexStringToBytes(String hexString) {
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
	
	private  byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

}
