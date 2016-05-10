package pingponging.server;

import rmi.RMIException;

/**
 Created by apoorve on 24/04/16.
 */
public class PingPongServerImpl implements PingPongServer {
    @Override
    public String pong(int num) throws RMIException {
        return "pong " + num;
    }
}
