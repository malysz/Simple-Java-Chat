package client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {
    private DatagramSocket socket;
    private InetAddress address;
    private int port;
    private boolean running;
    private String name;

    public Client(String name, String address, int port) {
        try{
            this.socket = new DatagramSocket();
            this.address = InetAddress.getByName(address);
            this.port = port;
            this.name = name;
            running = true;
            listen();
            send("\\con:"+name);
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void send(String message){
        try{
            if(!message.startsWith("\\")) message = name + "-> " + message;
            message += "\\e";
            byte[] data = message.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            socket.send(packet);
            System.out.println("Sent message to "+address.getHostAddress()+ " "+port);
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void listen(){
        Thread listenThread = new Thread("ChatProgramListener"){
            public void run(){
                try{
                    while(running){
                        byte[] data = new byte[1024];
                        DatagramPacket packet = new DatagramPacket(data, data.length);
                        socket.receive(packet);
                        String message = new String(data);
                        message = message.substring(0, message.indexOf("\\e"));

                        if(!isCommand(message, packet)){
                            ClientWindow.printToConsole(message);
                        }

                    }
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }; listenThread.start();
    }

    private static boolean isCommand(String message, DatagramPacket packet){
        if(message.startsWith("\\con:")){
                //skip command
            return true;
        }
        if(message.startsWith("\\dis:")){
                //skip command
            return true;
        }

        return false;
    }
}
