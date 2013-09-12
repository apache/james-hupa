package org.apache.hupa.client.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.hupa.client.CachingDispatchAsync;
import org.apache.hupa.client.evo.HupaEvoCallback;
import org.apache.hupa.client.place.IMAPMessagePlace;
import org.apache.hupa.client.ui.WidgetDisplayable;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.data.Message;
import org.apache.hupa.shared.data.MessageAttachment;
import org.apache.hupa.shared.data.MessageDetails;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.events.BackEvent;
import org.apache.hupa.shared.events.ForwardMessageEvent;
import org.apache.hupa.shared.events.LoadMessagesEvent;
import org.apache.hupa.shared.events.ReplyMessageEvent;
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
import org.apache.hupa.shared.rpc.DeleteMessageByUid;
import org.apache.hupa.shared.rpc.DeleteMessageResult;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

public class IMAPMessageActivity  extends AbstractActivity {

	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
        updateDisplay();
		bind();
		container.setWidget(display.asWidget());
	}
	 
	public IMAPMessageActivity with(IMAPMessagePlace place){
        this.message = place.getMessage();
        this.messageDetails = place.getMessageDetails();
        this.folder = place.getFolder();
        this.user = place.getUser();
        return this;
	}

    private void updateDisplay() {
        display.setAttachments(messageDetails.getMessageAttachments(), folder.getFullName(),message.getUid());
        display.setHeaders(message);
        display.setContent(messageDetails.getText());
    }
    
    protected void bind(){
    	display.getDeleteButtonClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                ArrayList<Long> uidList = new ArrayList<Long>();
                uidList.add(message.getUid());
                dispatcher.execute(new DeleteMessageByUid(folder, uidList), new HupaEvoCallback<DeleteMessageResult>(dispatcher, eventBus) {
                    public void callback(DeleteMessageResult result) {
                        eventBus.fireEvent(new LoadMessagesEvent(user,folder));
                    }
                }); 
            }

        });
    	display.getForwardButtonClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                eventBus.fireEvent(new ForwardMessageEvent(user,folder,message, messageDetails));
            }
            
        });
    	display.getReplyButtonClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                eventBus.fireEvent(new ReplyMessageEvent(user,folder,message, messageDetails, false));
            }
            
        });
    	display.getReplyAllButtonClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                eventBus.fireEvent(new ReplyMessageEvent(user,folder,message, messageDetails, true));
            }
            
        });
    	display.getBackButtonClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                eventBus.fireEvent(new BackEvent());
            }
            
        });
    	display.getShowRawMessageClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                String message_url = GWT.getModuleBaseURL() + SConsts.SERVLET_SOURCE + 
                "?" + SConsts.PARAM_UID + "=" + message.getUid() + 
                "&" + SConsts.PARAM_FOLDER + "=" + folder.getFullName();
                Window.open(message_url, "_blank", "");
            }
            
        });
    	
    }
    
    @Inject
    public IMAPMessageActivity(Displayable display, EventBus eventBus, PlaceController placeController,
    		 CachingDispatchAsync dispatcher){
    	this.display = display;
    	this.dispatcher = dispatcher;
    	this.eventBus = eventBus;
    	this.placeController = placeController;
    	
    	
    }
    private MessageDetails messageDetails;
    private Message message;
    private CachingDispatchAsync dispatcher;
    private IMAPFolderProxy folder;
    private User user;
	private final Displayable display;
	private final EventBus eventBus;
	private final PlaceController placeController;
	public interface Displayable extends WidgetDisplayable{
        public void setHeaders(Message msg);
        public void setAttachments(List<MessageAttachment> attachements, String folder,  long uid);
        public void setContent(String content);
        
        public HasClickHandlers getShowRawMessageClick();
        public HasClickHandlers getDeleteButtonClick();
        public HasClickHandlers getReplyButtonClick();
        public HasClickHandlers getReplyAllButtonClick();
        public HasClickHandlers getForwardButtonClick();
        public HasClickHandlers getBackButtonClick();
	}
}
