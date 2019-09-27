package net.lacnic.rpki.provisioning.client;

public class SocketFactory {

	private static SocketFactory instance;

	private SocketFactory() {
	}

	public static SocketFactory getInstance() {
		if (instance == null) {
			instance = new SocketFactory();
		}
		return instance;
	}

	public void invalidate() {
		instance = null;
	}
}
