import java.io.*;
import java.net.*;

class clientudp {
    public static void main(String args[]) {
        DatagramSocket socket = null;
        try {
            InetAddress id = InetAddress.getByName("localhost");
            int port = 12345;
            socket = new DatagramSocket();
            System.out.println("Client is running...");
            byte[] receive = new byte[1024];
            byte[] send = new byte[1024];
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            long ya = 0;
            long xa = 3;
            long q =11;

            if(xa<q)
            {
                 ya = (long) Math.pow(2,xa) % q;
            }
            System.out.println("YA = "+ya);
            String m = String.valueOf(q);
            send = m.getBytes();
            DatagramPacket se = new DatagramPacket(send, send.length, id, port);
            socket.send(se);
            DatagramPacket rev = new DatagramPacket(receive, receive.length);
            socket.receive(rev);

            String mes = new String(rev.getData()).trim();
            System.out.println("YB = " + mes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        socket.close();
    }
}
