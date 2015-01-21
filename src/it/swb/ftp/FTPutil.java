package it.swb.ftp;

import java.io.IOException;
import java.net.SocketException;
import java.util.Properties;

import org.apache.commons.net.ftp.FTPClient;

import it.swb.database.DataSource;
import it.swb.log.Log;


public class FTPutil {
	
//	private static FTPutil istanza = null;
//	public FTPClient f = null;
//	
//	private FTPutil(){
//		createFtpConnection();
//	}
//	
//
//	public static synchronized FTPutil getInstance() {
//		if (istanza == null)
//		{
//			synchronized(FTPutil.class) {  
//				istanza = new FTPutil();
//			}
//		}		
//		return istanza;
//	}
//    
//    public FTPClient getConnection(){
//    	if (f==null || !f.isAvailable() || !f.isConnected()) 
//    		createFtpConnection();
//   		return f;
//    }
    
	public static FTPClient getConnection(){
		FTPClient f = new FTPClient();
		try {
			//Class.forName("org.apache.commons.net.ftp.FTPClient").newInstance();
			
			Properties config = new Properties();	        
			config.load(DataSource.class.getResourceAsStream("/ftp.properties"));
			
			String server = config.getProperty("ftp_server");
	        String user = config.getProperty("ftp_username");
	        String pwd = config.getProperty("ftp_password");
	        String dir = config.getProperty("ftp_directory_iniziale");
			
			f.connect(server);
			f.login(user, pwd);
			f.changeWorkingDirectory(dir);	
			
		} catch (SocketException e) {
			Log.info(e); e.printStackTrace();
		} catch (Exception e) {
			Log.info(e); e.printStackTrace();
		} 
		return f;
	}
	
//	public void closeConnection(){
//		try {
//			f.disconnect();
//		}catch (IOException e) {
//			Log.info(e); e.printStackTrace();
//		}
//	}
	
	public static void closeConnection(FTPClient fc){
		try {
			fc.disconnect();
		}catch (IOException e) {
			Log.info(e); e.printStackTrace();
		}
	}

}
