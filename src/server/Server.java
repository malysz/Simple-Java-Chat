package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class Server {

    private static DatagramSocket socket;
    private static boolean running;
    private static ArrayList<ClientInfo> clients = new ArrayList<>();
    private static int clinetID;

    public static void start(int port){
        try{
            socket = new DatagramSocket(port);
            running = true;
            listen();
            System.out.println("Server started on port "+port);
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private static void send(String message, InetAddress address, int port){
        try{
            message += "\\e";
            byte[] data = message.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            socket.send(packet);
            System.out.println("Sent message to "+address.getHostAddress()+": "+port);
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private static void sendToAll(String message){
        for(ClientInfo client : clients){
            send(message, client.getAddress(), client.getPort());
        }
    }

    private static void listen(){
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
                            sendToAll(message);
                        }

                    }
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }; listenThread.start();
    }

    /*
        Server command list:
        \con:[name] -> connects client to server
        \dis:[id] -> disconnects client from server
     */

    private static boolean isCommand(String message, DatagramPacket packet){
        if(message.startsWith("\\con:")){
            String name = message.substring(message.indexOf(":")+1);
            clients.add(new ClientInfo(name, clinetID++, packet.getPort(), packet.getAddress()));
            sendToAll("User "+name+" has just connected!");
            return true;
        }
        if(message.startsWith("\\dis:")){
            int id = Integer.parseInt(message.substring(message.indexOf(":")+1));
            String name=null;
            int index=-1;
            for(ClientInfo client : clients){
                if(client.getId()==id){
                    name = client.getName();
                    index = clients.indexOf(client);
                }
            }
            if(index!=-1 && name!=null){
                clients.remove(index);
                sendToAll("User "+name+" has disconnected!");
            }
            return true;
        }

        return false;
    }

    public static void stop(){
        running = false;
    }
}
