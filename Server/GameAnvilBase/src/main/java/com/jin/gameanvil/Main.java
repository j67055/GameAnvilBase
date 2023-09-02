package com.yourcompany.gameanvil;
import com.nhn.gameanvil.GameAnvilServer;
import protocol.BasicProtocol;

public class Main {

    public static void main(String[] args) {
        GameAnvilServer server = GameAnvilServer.getInstance();

        server.addProtoBufClass(0, BasicProtocol.getDescriptor());

        server.run();
    }

}