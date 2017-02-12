package com.jasonpilbrough.helper;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class SmartJFrame extends JFrame {
	
	public SmartJFrame() {
		 try {
				InputStream stream = getClass().getClassLoader().getResourceAsStream("res/book.png");
				if(stream!=null){
					Image book= ImageIO.read(stream);
					setIconImage(book);
				}
				
			}catch (IOException e) {
				e.printStackTrace();
				throw new LogException("File 'res/book.png"+"' not found");
			}
	}

}
