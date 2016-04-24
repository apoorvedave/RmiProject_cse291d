package pingponging.client;

import pingponging.server.PingPongFactory;
import pingponging.server.PingPongServer;
import rmi.RMIException;
import rmi.Stub;

import java.net.InetSocketAddress;

/**
 Created by apoorve on 24/04/16.
 */
public class PingPongClient {
    public static void main(String[] args) throws RMIException {
        InetSocketAddress address = new InetSocketAddress(9000);
        PingPongFactory factory = Stub.create(PingPongFactory.class, address);
        PingPongServer stub = factory.makeServer();
        System.out.println(stub.pong(42));;
    }
}
