package toyRmi.server;

import rmi.RMIException;

/**
 Created by apoorve on 15/04/16.
 */
public class MyRemoteClass implements MyRemoteInterface {
    @Override
    public Integer add(Integer a, Integer b) throws RMIException {
        return a + b;
    }

    public void disp(Integer a) throws RMIException {
        System.out.println(a);
    }

    public Integer play() throws RMIException {
        return 42;
    }

    public Integer thrower() throws RMIException {
        throw new RMIException("File problems");
    }
}
