package pingponging.server;

import rmi.RMIException;
import rmi.Skeleton;

import java.net.InetSocketAddress;

/**
 Created by apoorve on 24/04/16.
 */
public class MyDriver {
    public static void main(String[] args) throws RMIException {
        PingPongFactory factory = new PingPongFactoryImpl();
        InetSocketAddress address = new InetSocketAddress(9000);
        Skeleton<PingPongFactory> skeleton = new Skeleton<>(PingPongFactory.class, factory, address);
        skeleton.start();
        System.out.println("Skeleton running at address: " + skeleton.getAddress());
    }
}
