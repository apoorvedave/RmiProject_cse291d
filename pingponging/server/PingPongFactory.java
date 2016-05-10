package pingponging.server;

import rmi.RMIException;

/**
 Created by apoorve on 24/04/16.
 */
public interface PingPongFactory {
    public PingPongServer makeServer() throws RMIException;
}
