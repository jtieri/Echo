package com.esoteric.echo;


import com.esoteric.echo.client.EchoClient;

public class App {
    public static void main(String[] args) throws Exception {
        EchoClient.create("localhost", 12321).start();
    }
}
