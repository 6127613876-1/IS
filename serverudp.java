import java.io.*;
import java.net.*;

class serverudp {
    public static void main(String arg[]) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(12345);
            System.out.println("Server is running...");
            byte[] receive = new byte[1024];
            byte[] send = new byte[1024];
            long xb = 5;
            long yb=0;
            DatagramPacket rev = new DatagramPacket(receive, receive.length);
            socket.receive(rev);
            InetAddress cid = rev.getAddress();
            int port = rev.getPort();
            String mes = new String(rev.getData()).trim();
            System.out.println("YA = " + mes);
            long q = Long.parseLong(String.valueOf(mes));
            if(xb<q)
            {
                yb = (long) Math.pow(2,xb) %q;
            }
            System.out.println("YB = "+yb);
            String m = Long.toString(yb);
            send = m.getBytes();
            DatagramPacket se = new DatagramPacket(send, send.length, cid, port);
            socket.send(se);
        } catch (Exception e) {
            e.printStackTrace();
        }
        socket.close();
    }
}
