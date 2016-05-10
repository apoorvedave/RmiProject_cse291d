package toyRmi.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

/**
 Created by apoorve on 23/04/16.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket s = new ServerSocket(9000);
        //ServerSocket s2 = new ServerSocket(9000);
        System.out.println(s.getInetAddress());
        System.out.println(s.getLocalPort());
        System.out.println(s.getLocalSocketAddress());
        System.out.println(s.hashCode());
        //System.out.println(InetAddress.getLocalHost().getHostAddress());
    }
}
