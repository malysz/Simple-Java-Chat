package server;

import java.net.InetAddress;

public class ClientInfo {
    private String name;
    private int id;
    private int port;
    private InetAddress address;

    public ClientInfo(String name, int id, int port, InetAddress address) {
        this.name = name;
        this.id = id;
        this.port = port;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getPort() {
        return port;
    }

    public InetAddress getAddress() {
        return address;
    }
}
