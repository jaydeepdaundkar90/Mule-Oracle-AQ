package com.connection.oracle.aq;

import java.sql.SQLException;
import oracle.AQ.AQDriverManager;
import oracle.AQ.AQEnqueueOption;
import oracle.AQ.AQException;
import oracle.AQ.AQMessage;
import oracle.AQ.AQObjectPayload;
import oracle.AQ.AQOracleQueue;
import oracle.AQ.AQQueue;
import oracle.AQ.AQSession;
import oracle.jdbc.pool.*;
//import oracle.sql.ORADataFactory;
import oracle.xdb.XMLType;



public class JMSAQConnection {
     
	
	public JMSAQConnection() {
	    // TODO Auto-generated constructor stub
	  }
	public void addMessage(String queueOwner,String queueName,String xmlMessage, String DriverType,String ServerName,int PortNumber,String DatabaseName,String User,String Password) throws SQLException, AQException, ClassNotFoundException {
		
		OracleDataSource ds = new OracleDataSource();
		
        ds.setDriverType(DriverType);

		ds.setServerName(ServerName);

		ds.setPortNumber(PortNumber);

		ds.setDatabaseName(DatabaseName); // sid

		ds.setUser(User);

		ds.setPassword(Password);

		java.sql.Connection aqconn = ds.getConnection();
		
		aqconn.setAutoCommit(false);

		AQSession aqsession = null;

		// Register the Oracle AQ Driver

		Class.forName("oracle.AQ.AQOracleDriver");

		try {

			AQEnqueueOption enqueueOption = new AQEnqueueOption();


			aqsession = AQDriverManager.createAQSession(aqconn);

		   AQQueue queue = aqsession.getQueue(queueOwner, queueName);
			
		    /* Enable enqueue on this queue */
		     queue.start();

			AQMessage msg = ((AQOracleQueue) queue).createMessage();



			AQObjectPayload payload = msg.getObjectPayload();

			XMLType payloadData = XMLType.createXML(aqconn, xmlMessage);

			payload.setPayloadData(payloadData);


                
			queue.enqueue(enqueueOption, msg);

			aqconn.commit();

			System.out.println("Message succesfully enqueued..");

		}

		catch (Exception ex) {

			ex.printStackTrace();

		}

		finally {

			aqsession.close();

			aqconn.close();

		}

	}



	/*public static OracleDataSource getOracleDataSource(String DriverType,String ServerName,Integer PortNumber,String DatabaseName,String User,String Password) throws SQLException {

		OracleDataSource ds = new OracleDataSource();

		ds.setDriverType(DriverType);

		ds.setServerName(ServerName);

		ds.setPortNumber(PortNumber);

		ds.setDatabaseName(DatabaseName); // sid

		ds.setUser(User);

		ds.setPassword(Password);



		return ds;

	}*/



}