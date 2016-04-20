package rmi;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 RMI skeleton

 <p>
 A skeleton encapsulates a multithreaded TCP server. The server's clients are
 intended to be RMI stubs created using the <code>Stub</code> class.

 <p>
 The skeleton class is parametrized by a type variable. This type variable
 should be instantiated with an interface. The skeleton will accept from the
 stub requests for calls to the methods of this interface. It will then
 forward those requests to an object. The object is specified when the
 skeleton is constructed, and must implement the remote interface. Each
 method in the interface should be marked as throwing
 <code>RMIException</code>, in addition to any other exceptions that the user
 desires.

 <p>
 Exceptions may occur at the top level in the listening and service threads.
 The skeleton's response to these exceptions can be customized by deriving
 a class from <code>Skeleton</code> and overriding <code>listen_error</code>
 or <code>service_error</code>.
 */
public class Skeleton<T> {
    //Private member variables
    private Class<T> interfaceClass;
    private T server;
    private InetSocketAddress address;
    private ListeningThread listeningThread;
    private final Set<ServiceThread> serviceThreads = new HashSet<>();

    public InetSocketAddress getAddress() {
        return address;
    }

    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }

    //Definition of ListeningThread class
    private class ListeningThread extends Thread {

        private boolean stopSignal;

        private ListeningThread() {
            stopSignal = false;
        }

        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                if (address == null) {
                    // TODO: fix this
                } else {
                    serverSocket = new ServerSocket(address.getPort());
                }
                while (!stopSignal) {
                    try {
                        serverSocket.setSoTimeout(10); //TODO - change the timeout value
                        ServiceThread new_thread = new ServiceThread(serverSocket.accept());
                        new_thread.start();
                    } catch (SocketTimeoutException e) {
                        // Ignoring the timeout
                    }
                }
                // Exited safely
                Skeleton.this.stopped(null);
            } catch (IOException e) {
                e.printStackTrace();
                Skeleton.this.stopped(e);
            } finally {
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        }
    }

    //Definition of ServiceThread class
    private class ServiceThread extends Thread {
        private Socket socket;

        public ServiceThread(Socket socket) {
            this.socket = socket;
            serviceThreads.add(this);
        }

        @Override
        public void run() {
            ObjectInputStream in = null;
            ObjectOutputStream out = null;
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                out.flush();
                in = new ObjectInputStream(socket.getInputStream());

                String methodName = (String) in.readObject();
                Class[] parameterTypes = (Class[]) in.readObject();
                Object[] args = (Object[]) in.readObject();

                Method method;
                if (parameterTypes == null) {
                    method = interfaceClass.getMethod(methodName);
                    out.writeObject(method.invoke(server));
                } else {
                    method = interfaceClass.getMethod(methodName, parameterTypes);
                    out.writeObject(method.invoke(server, args));
                }


            } catch (Exception e) {
                if (out != null) {
                    try {
                        out.writeObject(new RMIException(e));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            } finally {
                serviceThreads.remove(this);
                try {
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     Creates a <code>Skeleton</code> with no initial server address. The
     address will be determined by the system when <code>start</code> is
     called. Equivalent to using <code>Skeleton(null)</code>.

     <p>
     This constructor is for skeletons that will not be used for
     bootstrapping RMI - those that therefore do not require a well-known
     port.

     @param c An object representing the class of the interface for which the
     skeleton server is to handle method call requests.
     @param server An object implementing said interface. Requests for method
     calls are forwarded by the skeleton to this object.
     @throws Error If <code>c</code> does not represent a remote interface -
     an interface whose methods are all marked as throwing
     <code>RMIException</code>.
     @throws NullPointerException If either of <code>c</code> or
     <code>server</code> is <code>null</code>.
     */
    public Skeleton(Class<T> interfaceClass, T server) {
        //Checks for null and unsupported class
        if (server == null) {
            throw new NullPointerException("Server is null");
        }
        if (interfaceClass == null) {
            throw new NullPointerException("Class is null");
        }
        Method methods[] = interfaceClass.getDeclaredMethods();
        for (Method method : methods) {
            Class<?>[] exceptionTypes = method.getExceptionTypes();

            if (!Arrays.asList(exceptionTypes).contains(RMIException.class)) {
                throw new Error("Given class does not represent remote interface");
            }
        }
        //Set the member variables to the parameters passed
        this.interfaceClass = interfaceClass;
        this.server = server;
        //set the address to null
        this.address = null;
        //throw new UnsupportedOperationException("not implemented");
    }

    /**
     Creates a <code>Skeleton</code> with the given initial server address.

     <p>
     This constructor should be used when the port number is significant.

     @param c An object representing the class of the interface for which the
     skeleton server is to handle method call requests.
     @param server An object implementing said interface. Requests for method
     calls are forwarded by the skeleton to this object.
     @param address The address at which the skeleton is to run. If
     <code>null</code>, the address will be chosen by the
     system when <code>start</code> is called.
     @throws Error If <code>c</code> does not represent a remote interface -
     an interface whose methods are all marked as throwing
     <code>RMIException</code>.
     @throws NullPointerException If either of <code>c</code> or
     <code>server</code> is <code>null</code>.
     */
    public Skeleton(Class<T> interfaceClass, T server, InetSocketAddress address) {
        //Checks for null and unsupported class
        if (server == null) {
            throw new NullPointerException("Server is null");
        }
        if (interfaceClass == null) {
            throw new NullPointerException("Class is null");
        }
        for (Method method : interfaceClass.getDeclaredMethods()) {
            Class<?>[] exceptionTypes = method.getExceptionTypes();

            if (!Arrays.asList(exceptionTypes).contains(RMIException.class)) {
                throw new Error("Given class does not represent remote interface");
            }
        }
        //Set the member variables to the parameters passed
        this.interfaceClass = interfaceClass;
        this.server = server;
        this.address = address;
    }

    /**
     Called when the listening thread exits.

     <p>
     The listening thread may exit due to a top-level exception, or due to a
     call to <code>stop</code>.

     <p>
     When this method is called, the calling thread owns the lock on the
     <code>Skeleton</code> object. Care must be taken to avoid deadlocks when
     calling <code>start</code> or <code>stop</code> from different threads
     during this call.

     <p>
     The default implementation does nothing.

     @param cause The exception that stopped the skeleton, or
     <code>null</code> if the skeleton stopped normally.
     */
    protected void stopped(Throwable cause) {
        while (!serviceThreads.isEmpty()) {
            try {
                Thread t = null;
                synchronized (serviceThreads) {
                    if (!serviceThreads.isEmpty()) {
                        t = serviceThreads.iterator().next();
                    } else {
                        return;
                    }
                }
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    /**
     Called when an exception occurs at the top level in the listening
     thread.

     <p>
     The intent of this method is to allow the user to report exceptions in
     the listening thread to another thread, by a mechanism of the user's
     choosing. The user may also ignore the exceptions. The default
     implementation simply stops the server. The user should not use this
     method to stop the skeleton. The exception will again be provided as the
     argument to <code>stopped</code>, which will be called later.

     @param exception The exception that occurred.
     @return <code>true</code> if the server is to resume accepting
     connections, <code>false</code> if the server is to shut down.
     */
    protected boolean listen_error(Exception exception) {
        return false;
    }

    /**
     Called when an exception occurs at the top level in a service thread.

     <p>
     The default implementation does nothing.

     @param exception The exception that occurred.
     */
    protected void service_error(RMIException exception) {
    }

    /**
     Starts the skeleton server.

     <p>
     A thread is created to listen for connection requests, and the method
     returns immediately. Additional threads are created when connections are
     accepted. The network address used for the server is determined by which
     constructor was used to create the <code>Skeleton</code> object.

     @throws RMIException When the listening socket cannot be created or
     bound, when the listening thread cannot be created,
     or when the server has already been started and has
     not since stopped.
     */
    public synchronized void start() throws RMIException {
        if (listeningThread == null) {
            listeningThread = new ListeningThread();
        } else if (listeningThread.isAlive()) {
            throw new RMIException("Server already running");
        }
        listeningThread.start();
    }

    /**
     Stops the skeleton server, if it is already running.

     <p>
     The listening thread terminates. Threads created to service connections
     may continue running until their invocations of the <code>service</code>
     method return. The server stops at some later time; the method
     <code>stopped</code> is called at that point. The server may then be
     restarted.
     */
    public synchronized void stop() {
        if (listeningThread != null && listeningThread.isAlive()) {
            listeningThread.stopSignal = true;
        }
    }
}
