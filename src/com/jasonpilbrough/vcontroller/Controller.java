package com.jasonpilbrough.vcontroller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class Controller implements ActionListener {

	@Override
	public abstract void actionPerformed(ActionEvent e);

}
