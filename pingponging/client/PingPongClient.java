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
        InetSocketAddress address = new InetSocketAddress(args[0], Integer.parseInt(args[1]));
        PingPongFactory factory = Stub.create(PingPongFactory.class, address);
        PingPongServer stub = factory.makeServer();
        int total = 0;
        int fail = 0;
        for (int i = 0; i < 4; i++) {
            total++;
            String result = stub.pong(i);
            System.out.println(result);
            if (!result.equals("pong " + i)) {
                fail++;
            }
            System.out.println(total +" tests completed, " + fail + " tests Failed");
        }
    }
}
