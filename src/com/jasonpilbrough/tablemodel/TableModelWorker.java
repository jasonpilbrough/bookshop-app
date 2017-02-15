package com.jasonpilbrough.tablemodel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

public class TableModelWorker extends SwingWorker<Object[][], Object> {

	private int currentRowCount = 0;
	private SwingWorkerActions actions;
	
	public TableModelWorker(SwingWorkerActions actions) {
		this.actions = actions;
	}

	@Override
	protected Object[][] doInBackground() throws Exception {
		List<List<Object>> ans = new ArrayList<>();
		int step = 20;
		int numSteps = actions.getRowCount()/step +1;
		
		for (int i = 0; i < numSteps; i++) {
			ans.addAll((List<List<Object>>) actions.getValueAt(step, step*i));
			currentRowCount+=step;
			publish(toArray(ans));
			
		}
		
		if(ans.size()==0){
			return new Object[0][0];
		}
		return toArray(ans);
		
		/*Object[][] ans = new Object[actions.getRowCount()][actions.getColumnCount()];
		for (int i = 0; i < actions.getRowCount(); i++) {
			Object[] temp = (Object[])actions.getValueAt(i);
			
			currentRowCount++;
			if(currentRowCount%20==0){
				publish(ans);
			}
			ans[i] = temp;
		}
		
		return ans;*/
		
	}
	
	private Object[][] toArray(List<List<Object>> list){
		Object[][] arr = new Object[list.size()][list.get(0).size()];
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				arr[i][j] = list.get(i).get(j);
			}
		}
		return arr;
	}
	
	@Override
	protected void process(List<Object> chunks) {
		firePropertyChange("data", null, (Object[][])chunks.get(chunks.size()-1));
		firePropertyChange("row_count", null, currentRowCount);
		double progress = (double)currentRowCount/(double)actions.getRowCount();
		firePropertyChange("progress", null, progress);
	}
	
	@Override
	protected void done() {
		try {
			firePropertyChange("data", null, get());
			firePropertyChange("row_count", null, actions.getRowCount());
			firePropertyChange("progress", null, 1.0);
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	



	
	
}