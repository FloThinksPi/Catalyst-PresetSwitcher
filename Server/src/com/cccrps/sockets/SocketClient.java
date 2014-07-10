package com.cccrps.sockets;

import java.net.Socket;

public class SocketClient extends BaseMessageClient {

	private Server server;

	public SocketClient(Socket client, Server server) {
		super(client);
		this.server = server;
	}

	@Override
	public void onMessage(String message) {
		System.out.println( "Got message from client:" );
		System.out.println( message );
		this.server.onMessage(message, this);
	}

}
