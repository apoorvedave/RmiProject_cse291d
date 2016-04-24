package toyRmi.server;

import rmi.RMIException;

import java.io.Serializable;

/**
 * Created by apoorve on 15/04/16.
 */
public interface MyRemoteInterface extends Serializable {
    public Integer add(Integer a, Integer b) throws RMIException;
}
