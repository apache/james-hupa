/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/
package org.apache.hupa.client.mvp;

import com.google.inject.Module;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.presenter.client.EventBus;

import org.apache.hupa.client.HupaCallback;
import org.apache.hupa.client.HupaMvpTestCase;
import org.apache.hupa.client.guice.GuiceMvpTestModule;
import org.apache.hupa.client.guice.GuiceMvpTestModule.DispatchTestAsync;
import org.apache.hupa.client.mvp.MessageSendPresenter.Type;
import org.apache.hupa.client.guice.GuiceClientTestModule;
import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.data.Message;
import org.apache.hupa.shared.data.MessageDetails;
import org.apache.hupa.shared.data.SMTPMessage;
import org.apache.hupa.shared.events.FlashEvent;
import org.apache.hupa.shared.events.SentMessageEvent;
import org.apache.hupa.shared.events.ServerStatusEvent;
import org.apache.hupa.shared.rpc.ForwardMessage;
import org.apache.hupa.shared.rpc.GenericResult;
import org.apache.hupa.shared.rpc.ReplyMessage;
import org.apache.hupa.shared.rpc.SendMessage;
import org.easymock.EasyMock;

import java.util.ArrayList;
import java.util.Arrays;

public class MessageSendPresenterTest extends HupaMvpTestCase {

    
    protected Module[] getModules() {
        return new Module[]{new GuiceClientTestModule(), new GuiceMvpTestModule() {
            @Override
            protected void configure() {
                dispatchAsyncInstance = EasyMock.createStrictMock(DispatchAsync.class);
                super.configure();
            }
        }};
    }

    MessageSendPresenter presenter = injector.getInstance(MessageSendPresenter.class);
    MessageSendPresenter.Display display = presenter.getDisplay();
    
    public void testRemoveEmailFromList() {
        String[] list = new String[]{"a@a.a", "e@dom.com", "<e@dom.com>", "Name <e@dom.com>", "Name < e@dom.com >"};
        ArrayList<String> newList = presenter.removeEmailFromList(Arrays.asList(list), " e@dom.com ");
        assertEquals(1, newList.size());
    }

    public void testValidations() throws Exception {
        assertFalse(presenter.validate());

        display.getToText().setText("invalid@address");
        assertFalse(presenter.validate());

        display.getToText().setText("nobody@domain.com");
        assertTrue(presenter.validate());
    }

    public void testDispatchMessage() {
        SMTPMessage message = new SMTPMessage();
        message.setFrom("from@dom.com");
        message.setTo(presenter.emailTextToArray("to@dom.com"));
        message.setText("message");

        DispatchAsync dispatcher = injector.getInstance(DispatchTestAsync.class);
        EventBus eventBus = EasyMock.createMock(EventBus.class);
        
        // When a success message is sent, the eventbus receives a SentMessageEvent
        // and the display is reset
        EasyMock.reset(eventBus);
        eventBus.fireEvent(EasyMock.isA(ServerStatusEvent.class));
        eventBus.fireEvent(EasyMock.isA(SentMessageEvent.class));
        EasyMock.replay(eventBus);
        display.getToText().setText("whatever");
        presenter.dispatchMessage(dispatcher, eventBus, new SendMessage(message));
        assertEquals("", display.getToText().getText());
        EasyMock.verify(eventBus);

        // When a error happens sending a message, the eventbus receives a FlashEvent
        // and the display is not reset
        EasyMock.reset(eventBus);
        eventBus.fireEvent(EasyMock.isA(ServerStatusEvent.class));
        eventBus.fireEvent(EasyMock.isA(FlashEvent.class));
        EasyMock.replay(eventBus);
        display.getToText().setText("whatever");
        presenter.dispatchMessage(dispatcher, eventBus, new SendMessage(null));
        assertEquals("whatever", display.getToText().getText());
        EasyMock.verify(eventBus);
    }

    public void testSendInvalidMessage() throws Exception {
        presenter.onBind();
        presenter.revealDisplay(testUser);
        assertEquals(testUser.getName(), display.getFromText().getText());
        
        display.getSubjectText().setText("Test");
        display.getMessageHTML().setHTML("Message");
        
        fireSendEvent(null);
        
        assertNull(presenter.message);
        assertEquals("Test", display.getSubjectText().getText());
        assertEquals("Message", display.getMessageHTML().getHTML());
    }

    public void testSendMessage() throws Exception {
        presenter.onBind();
        presenter.revealDisplay(testUser);
        assertEquals(testUser.getName(), display.getFromText().getText());
        
        display.getToText().setText("to1@dom.com; to2@dom.com");
        display.getCcText().setText("cc1@dom.com, , cc2@dom.com ");
        display.getBccText().setText("bcc1@dom.com, bcc2@dom.com");
        display.getSubjectText().setText("Test");
        display.getMessageHTML().setHTML("Message");
        
        fireSendEvent(SendMessage.class);
        
        assertEquals("Test", presenter.message.getSubject());
        assertEquals("Message", presenter.message.getText());
        assertEquals(testUser.getName(), presenter.message.getFrom());
        assertEquals(2, presenter.message.getTo().size());
        assertEquals("to1@dom.com", presenter.message.getTo().get(0));
        assertEquals(2, presenter.message.getCc().size());
        assertEquals(2, presenter.message.getBcc().size());
    }
    

    public void testMailTo() throws Exception {
        presenter.onBind();
        presenter.revealDisplay(testUser, "mailto@dom.com");
        assertEquals(testUser.getName(), display.getFromText().getText());
        assertEquals("mailto@dom.com", display.getToText().getText());
    }
    
    public void testReply() {
        createMockMessageAndRevealDisplay(Type.REPLY);
        fireSendEvent(ReplyMessage.class);
        
        assertEquals("Re: Subject", presenter.message.getSubject());
        assertTrue(presenter.message.getText().contains("Message"));
        assertTrue(presenter.message.getText().contains("from@dom.com"));
        
        assertEquals(testUser.getName(), presenter.message.getFrom());
        assertEquals(1, presenter.message.getTo().size());
        assertEquals("replyto@dom.com", presenter.message.getTo().get(0));
        assertEquals(0, presenter.message.getCc().size());
        assertEquals(0, presenter.message.getBcc().size());
    }

    public void testReplyAll() {
        createMockMessageAndRevealDisplay(Type.REPLY_ALL);
        fireSendEvent(ReplyMessage.class);
        
        assertEquals("Re: Subject", presenter.message.getSubject());
        assertTrue(presenter.message.getText().contains("Message"));
        assertTrue(presenter.message.getText().contains("from@dom.com"));
        
        assertEquals(testUser.getName(), presenter.message.getFrom());
        assertEquals(1, presenter.message.getTo().size());
        assertEquals("from@dom.com", presenter.message.getTo().get(0));
        assertEquals(5, presenter.message.getCc().size());
        assertEquals("replyto@dom.com", presenter.message.getCc().get(0));
        assertEquals("to1@dom.com", presenter.message.getCc().get(1));
        assertEquals("to2@dom.com", presenter.message.getCc().get(2));
        assertEquals("cc1@dom.com", presenter.message.getCc().get(3));
        assertEquals("cc2@dom.com", presenter.message.getCc().get(4));
        assertEquals(0, presenter.message.getBcc().size());
    }

    public void testForward() {
        createMockMessageAndRevealDisplay(Type.FORWARD);
        presenter.getDisplay().getToText().setText("valid@dom.com");
        fireSendEvent(ForwardMessage.class);
        
        assertEquals("Fwd: Subject", presenter.message.getSubject());
        assertTrue(presenter.message.getText().contains("Message"));
        assertTrue(presenter.message.getText().contains("from@dom.com"));
        assertTrue(presenter.message.getText().contains("Forwarded message"));
        assertTrue(presenter.message.getText().contains("Subject"));
        assertEquals(testUser.getName(), presenter.message.getFrom());
        assertEquals(1, presenter.message.getTo().size());
        assertEquals("valid@dom.com", presenter.message.getTo().get(0));
        assertEquals(0, presenter.message.getCc().size());
        assertEquals(0, presenter.message.getBcc().size());
    }
    
    private void createMockMessageAndRevealDisplay(Type type) {
        Message oldmessage = new Message();
        oldmessage.setFrom("from@dom.com");
        ArrayList<String> to = new ArrayList<String>(Arrays.asList(new String[]{"to1@dom.com", "to2@dom.com"}));
        oldmessage.setTo(to);
        ArrayList<String> cc = new ArrayList<String>(Arrays.asList(new String[]{"cc1@dom.com", "cc2@dom.com"}));
        oldmessage.setCc(cc);
        oldmessage.setReplyto("replyto@dom.com");
        oldmessage.setSubject("Subject");
        
        MessageDetails oldDetails = new MessageDetails();
        oldDetails.setText("Message");
        oldDetails.setUid(0l);
        
        ImapFolderImpl folder = new ImapFolderImpl();
        folder.setFullName("FOLDER");
        
        presenter.bind();
//        presenter.revealDisplay(testUser, folder, oldmessage, oldDetails, type);
    }

    @SuppressWarnings("unchecked")
    private void fireSendEvent(Class<? extends Action<GenericResult>> commandClass) {
        DispatchAsync dispatcher = injector.getInstance(DispatchAsync.class);
        EasyMock.reset(dispatcher);
        if (commandClass != null)
            dispatcher.execute(EasyMock.isA(commandClass), EasyMock.isA(HupaCallback.class));
        EasyMock.replay(dispatcher);
        display.getSendClick().fireEvent(null);
        EasyMock.verify(dispatcher);
    }
}
