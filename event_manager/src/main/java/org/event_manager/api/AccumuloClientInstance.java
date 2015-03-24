package org.event_manager.api;

import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.Instance;
import org.apache.accumulo.core.client.ZooKeeperInstance;
import org.apache.accumulo.core.client.security.tokens.AuthenticationToken;
import org.apache.accumulo.core.client.security.tokens.PasswordToken;
import org.apache.accumulo.core.conf.AccumuloConfiguration;

public class AccumuloClientInstance {
   private static Connector accumuloClient = null;
   
   public static synchronized Connector getInstance(){
	   if(accumuloClient == null){
		   Instance instances = new ZooKeeperInstance("accumulo", "accumuloServer");
		   AccumuloConfiguration ac = instances.getConfiguration();
		   AuthenticationToken token = new PasswordToken("123456789");
		   try {
			accumuloClient = instances.getConnector("root", token);
		} catch (AccumuloException | AccumuloSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();   
		}
	   }
	   return accumuloClient;
   }
}
