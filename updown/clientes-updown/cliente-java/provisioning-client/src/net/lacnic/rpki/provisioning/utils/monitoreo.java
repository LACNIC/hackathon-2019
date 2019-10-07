package net.lacnic.rpki.provisioning.utils;

import javax.naming.NamingException;

import net.lacnic.rpki.provisioning.client.RPKIProvisioningWsClient;

public class monitoreo {
	public static void main(String[] args) throws NamingException, Exception {
		monitoreo();
	}

	private static void monitoreo() throws Exception {
		try {
			String orgId = "";
			String monitoreo = RPKIProvisioningWsClient.monitoreo(orgId);
			if (monitoreo.equalsIgnoreCase("OK")) {
				System.out.println("Todo esta funcionando bien");
			} else {
				System.out.println(monitoreo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
