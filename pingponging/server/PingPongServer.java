package pingponging.server;

import rmi.RMIException;

/**
 Created by apoorve on 24/04/16.
 */
public interface PingPongServer {
    public String pong(int num) throws RMIException;
}
