package cz.maara.strandgui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class CommunicatorThread extends Thread {

    public static interface CommunicatorListener {
        public void onConnectionEstablished();
        public void onConnectionLost();
    }

    private final List<String> addresses;
    private final int port;
    private final CommunicatorListener listener;
    private final AtomicBoolean terminated = new AtomicBoolean(false);

    private class Message {
        public final String message;
        public final Runnable onConfirm;

        public Message(String message, Runnable onConfirm) {
            this.message = message;
            this.onConfirm = onConfirm;
        }
        public String toString() {
            return message;
        }
    }
    private final LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>();

    public CommunicatorThread(List<String> addresses, int port, CommunicatorListener listener) {
        super("CommunicatorThread thread");
        if (addresses.contains(null)) throw new NullPointerException("Address list may not contain null: " + addresses);
        this.addresses = Collections.unmodifiableList(addresses);
        this.port = port;
        this.listener = listener;
    }

    public void terminate() {
        this.terminated.set(true);
    }

    public void sendMessage(String text, Runnable onConfirm) {
        queue.add(new Message(text, onConfirm));
    }

    public void run() {

        listener.onConnectionLost();

        System.out.println("CommunicatorThread running in thread " + Thread.currentThread().getId());

        boolean reportedConnectionUp = false;

        while (! terminated.get()) {

            for (String address : addresses) {
                try (Socket socket = new Socket(address, port)) {
                    System.out.println("Established connection to " + address + ":" + port);
                    listener.onConnectionEstablished();
                    reportedConnectionUp = true;

                    connectionLoop(socket);
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (reportedConnectionUp) {
                listener.onConnectionLost();
                reportedConnectionUp = false;
            }
        }
    }

    private void connectionLoop(Socket socket) throws IOException {

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

//        String handshake = in.readLine();

        while (! terminated.get()) {
            try {
                System.out.println("Waiting for message");
                Message message = queue.poll(5, TimeUnit.SECONDS);

                if (message != null) {
                    System.out.println("Sending message " + message + "\"");
                    out.println(message.message);

                    String reply = in.readLine();

                    if (message.onConfirm != null) message.onConfirm.run();

                    System.out.println("Message: \"" + message + "\", reply: \"" + reply + "\"");

                } else {
                    System.out.println("... waiting for message ...");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
