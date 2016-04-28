package toyRmi.server;

import rmi.Skeleton;

import java.net.InetSocketAddress;

/**
 * Created by apoorve on 15/04/16.
 */
public class MyDriver {
    public static void main(String[] args) {
        try {
            InetSocketAddress address = new InetSocketAddress("localhost", 9001);

            MyRemoteClass myRemoteObject = new MyRemoteClass();
            Skeleton<MyRemoteInterface> skeleton = new Skeleton<>(MyRemoteInterface.class, myRemoteObject, address);
            skeleton.start();

            System.out.println("Skeleton Started");
            //String name = "Adder";
            //MyRemoteInterface adder = Stub.create(MyRemoteInterface.class, address);

            //Registry registry = Stub.create(Registry.class, new InetSocketAddress("localhost", 10001));
            //registry.rebind(name, adder);
        } catch (Exception e) {
            System.out.println("Exception Occured:");
            e.printStackTrace();
        }
    }
}
