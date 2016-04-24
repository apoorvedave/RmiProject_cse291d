package pingponging.server;

import rmi.RMIException;

/**
 Created by apoorve on 24/04/16.
 */
public class PingPongFactoryImpl implements PingPongFactory {
    @Override
    public PingPongServer makeServer() throws RMIException {
        return new PingPongServerImpl();
    }
}
