package org.apache.hupa.client.ui;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.TreeViewModel;

/**
 * CellTree with right click event menu drop down
 */
public class RightCellTree extends CellTree {
	final DecoratedPopupPanel simplePopup = new DecoratedPopupPanel(true);

	public <T> RightCellTree(TreeViewModel viewModel, T rootValue, Resources resources) {
		super(viewModel, rootValue, resources);
		this.sinkEvents(Event.ONMOUSEUP | Event.ONDBLCLICK | Event.ONCONTEXTMENU);
		MenuBar popup = new MenuBar(true);
		MenuItem newItem = new MenuItem("New subfolder", true, newSubfolderCommand);
		MenuItem markItem = new MenuItem("Mark all as read", true, markAllReadCommand);
		newItem.addStyleName(Resources.INSTANCE.cellTreeStyle().menuItem());
		markItem.addStyleName(Resources.INSTANCE.cellTreeStyle().menuItem());
		popup.addItem(newItem);
		popup.addItem(markItem);
		popup.setVisible(true);
		simplePopup.add(popup);
		simplePopup.addStyleName(Resources.INSTANCE.cellTreeStyle().popup());
	}

	public RightCellTree(FoldersTreeViewModel viewModel) {
		this(viewModel, null, Resources.INSTANCE);
	}

	Command newSubfolderCommand = new Command() {
		public void execute() {
			Window.alert("//TODO New subfolder");
		}
	};

	Command markAllReadCommand = new Command() {
		public void execute() {
			Window.alert("//TODO Mark all as read");
		}
	};

	@Override
	public void onBrowserEvent(Event event) {
		GWT.log("onBrowserEvent", null);
		event.stopPropagation(); // propagated
		event.preventDefault();
		super.onBrowserEvent(event);
		switch (DOM.eventGetType(event)) {
		case Event.ONMOUSEUP:
			if (DOM.eventGetButton(event) == Event.BUTTON_LEFT) {
				GWT.log("left clicked.", null);
			}
			if (DOM.eventGetButton(event) == Event.BUTTON_RIGHT) {
				GWT.log("right clicked.", null);
				int x = DOM.eventGetClientX(event);
				int y = DOM.eventGetClientY(event);
				simplePopup.setPopupPosition(x, y);
				simplePopup.show();
			}
			break;
		case Event.ONDBLCLICK:
			break;

		case Event.ONCONTEXTMENU:
			GWT.log("Event.ONCONTEXTMENU", null);
			break;

		default:
			break; // Do nothing
		}
	}

	public interface RightClickHandler extends ClickHandler {
		void onRightClick(Widget sender, Event event);
	}

	public interface Css extends Style {
		String popup();
		String menuItem();
	}

	public interface Resources extends CellTree.Resources {

		Resources INSTANCE = GWT.create(Resources.class);

		@Source("res/CssFolderListView.css")
		public Css cellTreeStyle();

		@Source("res/listicons.png")
		public ImageResource listicons();
	}
}
