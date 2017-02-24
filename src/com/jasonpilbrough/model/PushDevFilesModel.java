package com.jasonpilbrough.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.apache.commons.net.io.CopyStreamAdapter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.jasonpilbrough.helper.Database;
import com.jasonpilbrough.helper.Logger;
import com.jasonpilbrough.helper.MyFTPClient;
import com.jasonpilbrough.helper.Percent;
import com.jasonpilbrough.helper.SettingsFile;
import com.jasonpilbrough.helper.SmartFile;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class PushDevFilesModel {
	private Database db;
	private PropertyChangeSupport changefirer;
	private SwingWorker<Object, Object> worker;
	private boolean terminateOnComplete;
	
	
	public PushDevFilesModel(Database db, SettingsFile settings, boolean terminateOnComplete) {
		super();
		this.db = db;
		changefirer = new PropertyChangeSupport(this);
		worker = new MySwingWorker(settings);
		this.terminateOnComplete = terminateOnComplete;
		
	}

	public void addListener(PropertyChangeListener listener) {
		changefirer.addPropertyChangeListener(listener);
	}
	
	public void pushDevFiles(){
		if(!new File("dev").exists()){
			changefirer.firePropertyChange("failed", null, "No files to upload");
			if(terminateOnComplete){
				System.exit(0);
			}
			return;
		}
		worker.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				switch(evt.getPropertyName()){
				case "complete":
					if(terminateOnComplete){
						System.exit(0);
					}
					changefirer.firePropertyChange("complete", null, evt.getNewValue());
					break;
				case "progress":
					changefirer.firePropertyChange("progress", null, evt.getNewValue());
					break;
				case "failed":
					if(terminateOnComplete){
						System.exit(0);
					}
					changefirer.firePropertyChange("failed", null, evt.getNewValue());
					break;
				}
				
			}
		});
		worker.execute();
	}
}

class MySwingWorker extends SwingWorker<Object, Object>{

	private SettingsFile settings;
	private Logger logger;
	
	public MySwingWorker(SettingsFile settings) {
		this.settings = settings;
		this.logger = new Logger(settings);
	}
	
	@Override
	protected Object doInBackground() throws Exception {
		String foldername = "dev";
		publish("Preparing to upload...");
		final MyFTPClient ftp = new MyFTPClient(settings);
		String zipFilename  = zip(foldername);
		
		final double fileSize = new File(zipFilename).length();
		ftp.setCopyStreamAdapter(new CopyStreamAdapter(){
			@Override
		    public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {
				
		       double progress = (totalBytesTransferred/fileSize);
		       if(progress==1){
		    	   publish("Finalising upload...");
		       }else{
		    	   publish("Uploading: "+new Percent(progress)+ " complete");
		       }
		    }
		});
		ftp.uploadFile(zipFilename);
		SmartFile folder = new SmartFile("dev/","");
		folder.delete();
		SmartFile file = new SmartFile(zipFilename);
		file.delete();
		ProcessBuilder pb = new ProcessBuilder("java", "-jar", "EmailTool.jar");
		Process p = pb.start();
		publish("Upload complete");
		return new Object();
	}
	
	@Override
	protected void process(List<Object> chunks) {
		firePropertyChange("progress", null, chunks.get(chunks.size()-1));
	}
	
	@Override
	protected void done() {
		try {
			get();
			firePropertyChange("complete", null, "");
		}catch (InterruptedException | ExecutionException e) {
			 firePropertyChange("failed", null, "Unable to upload files");
			 e.printStackTrace();
			 try {
				logger.log(e);
			} catch (IOException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, "Error writing to app logs, contact developer immediately","Critical Error", JOptionPane.ERROR_MESSAGE);
			}
		} 
	}
	
	private String zip(String source) throws ZipException{
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd__HH-mm-ss");
		String destination = source+"-"+fmt.print(new DateTime())+".zip";

         ZipFile zipFile = new ZipFile(destination);
         ZipParameters parameters = new ZipParameters();
         parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
         parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
         zipFile.addFolder(source, parameters);
         return destination;
	}
	
	
	
}
