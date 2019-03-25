package com.cool.server.pizza;

import com.cool.server.pizza.annotations.ChocoServlet;
import com.cool.server.pizza.exceptions.MethodNotSupportedException;
import com.cool.server.pizza.methods.HTTPMethod;
import com.cool.server.pizza.methods.HTTPRequestResolverFactory;
import org.reflections.Reflections;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

public class PizzaServer implements Runnable {

    public static final int PORT = 4444;

    private Socket socket;

    public static Map<String, Class<?>> servlets;


    PizzaServer(Socket connection) {
        this.socket = connection;
        servlets = loadServlets();
    }


    /**
     * -DpoolSize=5 -DmaxPoolSize=10 -DqueueSize=100 -DsocketTimeOut=4000 -DwriteToSameFile=true -Dverbose=false
     * @param args
     */

    public static void main(String args[]) {

        CommandLineArguments arguments = CommandLineArguments.getCommandLineArgument(args);

        try (final ServerSocket serverSocket = new ServerSocket(arguments.getPort())) {

            System.out.println("Starting Pizza server on port.. " + serverSocket.getLocalPort());
            System.out.println("Please hit localhost:"+ arguments.getPort());

            ThreadPoolExecutor executor =  ThreadFactory.getThreadPoolExecutor(arguments);

            while (true) {
                System.out.println("Waiting for incoming connection..");

                Socket newConnection = serverSocket.accept();
                newConnection.setKeepAlive(true);
                newConnection.setSoTimeout(arguments.getSocketTimeOut());

                PizzaServer server = new PizzaServer(newConnection);

                executor.execute(server);
                BlockingQueue<Runnable> queue = executor.getQueue();
                if (arguments.isVerbose()) {
                    System.out.println("Number of threads in the queue : " + queue.size());
                    queue.forEach(runnable -> System.out.println("In the queue : " + runnable.toString()));
                }

                System.out.println("Current pool size : " + executor.getPoolSize());
                System.out.println("Current active threads : " + executor.getActiveCount());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {


        FileInputStream fileInput = null;
        try {

            HTTPRequest request = new HTTPRequest(socket);

            HTTPMethod method = HTTPRequestResolverFactory.resolveMethod(request, this.socket);
            method.execute();

        } catch (IOException e) {

            e.printStackTrace();
            if (fileInput != null) {
                try {
                    fileInput.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        } catch (MethodNotSupportedException e) {
            e.printStackTrace();
        }

        /**
        try {
            socket.setKeepAlive(true);
            System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + "Is Socket closed? : " + socket.isClosed()
                    + " Is input shutdown? : " + socket.isInputShutdown()
                    + " Is connected? : " + socket.isConnected());
        } catch (SocketException e) {
            e.printStackTrace();
        }
         **/

        System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + " exiting..");

    }

    private Map<String, Class<?>> loadServlets() {

        Reflections reflections = new Reflections("com.cool.server");

        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(ChocoServlet.class);
        if (annotated == null || annotated.size() == 0) {
            return new HashMap<>(0);
        }

        Map<String, Class<?>> servlets = new HashMap<>(annotated.size());

        Iterator<Class<?>> it = annotated.iterator();
        while(it.hasNext()) {
            Class<?> clazz = it.next();
            ChocoServlet annotation = clazz.getDeclaredAnnotation(ChocoServlet.class);
            servlets.put(annotation.path(), clazz);

        }
        return servlets;

    }

}
