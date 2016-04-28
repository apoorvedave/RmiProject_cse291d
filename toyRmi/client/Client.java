package toyRmi.client;

import rmi.Stub;
import toyRmi.server.MyRemoteInterface;

import java.net.InetSocketAddress;

/**
 Created by apoorve on 15/04/16.
 */
public class Client {

    public static void main(String[] args) {
        try {
            String name = "Adder";
            InetSocketAddress address = new InetSocketAddress("localhost", 9001);
            //Registry registry = LocateRegistry.getRegistry(registryAddress);
            //MyRemoteInterface stub = (MyRemoteInterface) registry.lookup(name);
            MyRemoteInterface stub = Stub.create(MyRemoteInterface.class, address);
            //stub.disp(6);
            System.out.println(stub.add(6, 80));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
