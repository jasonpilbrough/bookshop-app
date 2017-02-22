package com.jasonpilbrough.helper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.io.CopyStreamAdapter;

public class MyFTPClient {

	 private FTPClient ftp = null;

	    public MyFTPClient(SettingsFile settings) throws FTPException {
	    	try {
	    		ftp = new FTPClient();
		        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		        int reply;
		        ftp.connect(settings.getSettings().get("ftp_url"));
		        reply = ftp.getReplyCode();
		        if (!FTPReply.isPositiveCompletion(reply)) {
		            ftp.disconnect();
		            throw new Exception("Failed to connect to FTP Server");
		        }
		        ftp.login(settings.getSettings().get("ftp_user"), settings.getSettings().get("ftp_password"));
		        ftp.setFileType(FTP.BINARY_FILE_TYPE);
		        ftp.enterLocalPassiveMode();
			} catch (Exception e) {
				throw new FTPException(e);
			}
	        
	    }
	    
	    public void setCopyStreamAdapter(CopyStreamAdapter listener){
    		ftp.setCopyStreamListener(listener);
	    }
	    
	    public int getFileSize(String remoteFilePath) throws IOException{
	    	return (int)ftp.mlistFile(remoteFilePath).getSize();
	    }
	    
	    public void uploadFile(String localFile) throws FTPException{
	    
            boolean done;
			try {
				File firstLocalFile = new File(localFile);
 
				InputStream inputStream = new FileInputStream(firstLocalFile);
 
				done = ftp.storeFile("dev_files/"+localFile, inputStream);
				inputStream.close();
				if (!done) {
	                throw new FTPException("File upload unsuccessful");
	            }
			} catch (IOException e) {
				throw new FTPException(e);
			} catch (FTPException e) {
				throw new FTPException(e);
			}
            
	    }

	    public void downloadFile(String remoteFilePath, String localFilePath) throws IOException{
	        try (FileOutputStream fos = new FileOutputStream(localFilePath)) {
	            if(!this.ftp.retrieveFile(remoteFilePath, fos)){
	            	throw new IOException("Failed to retirive file: "+remoteFilePath);
	            }
	        }
	        
	    }

	    public void disconnect() throws IOException{
	        if (this.ftp.isConnected()) {
	            this.ftp.logout();
	            this.ftp.disconnect();
	        }
	    }
}
