package org.apache.hupa.client.ui;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.inject.Inject;

public class ContentSplitLayoutPanel extends SplitLayoutPanel {

	@Inject MessagesCellTable table;
	
	public ContentSplitLayoutPanel(){
		super();
	}
	public ContentSplitLayoutPanel(int splitterSize){
		super(splitterSize);
	}

	@Override
	public void onResize() {
		super.onResize();
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				table.refresh();
			}
		});
	}
}
