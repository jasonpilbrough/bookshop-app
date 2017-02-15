package com.jasonpilbrough.tablemodel;

import java.util.List;

import javax.swing.SwingWorker;

public class TableModelWorker extends SwingWorker<Object[][], Object> {

	private int currentRowCount = 0;
	private SwingWorkerActions actions;
	
	public TableModelWorker(SwingWorkerActions actions) {
		this.actions = actions;
	}

	@Override
	protected Object[][] doInBackground() throws Exception {
		Object[][] ans = new Object[actions.getRowCount()][actions.getColumnCount()];
		for (int i = 0; i < actions.getRowCount(); i++) {
			Object[] temp = new Object[actions.getColumnCount()];
			for (int j = 0; j < actions.getColumnCount(); j++) {
				temp[j] = actions.getValueAt(i, j);
			}
			currentRowCount++;
			if(currentRowCount%20==0){
				publish(ans);
			}
			ans[i] = temp;
		}
		
		firePropertyChange("data", null, ans);
		firePropertyChange("row_count", null, actions.getRowCount());
		return ans;
	}
	
	@Override
	protected void process(List<Object> chunks) {
		firePropertyChange("data", null, chunks.get(chunks.size()-1));
		firePropertyChange("row_count", null, currentRowCount);
	}
	



	
	
}