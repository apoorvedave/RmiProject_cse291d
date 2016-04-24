package toyRmi.server;

import rmi.RMIException;

/**
 * Created by apoorve on 15/04/16.
 */
public class MyRemoteClass implements MyRemoteInterface {
    @Override
    public Integer add(Integer a, Integer b) throws RMIException {
        return a + b;
    }
}
