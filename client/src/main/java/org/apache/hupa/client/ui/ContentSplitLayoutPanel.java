package org.apache.hupa.client.ui;

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
}
