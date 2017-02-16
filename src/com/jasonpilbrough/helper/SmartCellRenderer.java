package com.jasonpilbrough.helper;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class SmartCellRenderer extends DefaultTableCellRenderer{
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		
		JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		if(table.getModel().getColumnClass(column)==Id.class){
			setHorizontalAlignment(JLabel.LEFT);
		} else if(table.getModel().getColumnClass(column)==Money.class){
			setHorizontalAlignment(JLabel.RIGHT);
			if(value.toString().length()>0){
				l.setText(new Money(value).toStringWithoutCurrency());
			}
			
		} else if(table.getModel().getColumnClass(column)==Integer.class){
			setHorizontalAlignment(JLabel.RIGHT);
		}
		
		return l;
	}
}
