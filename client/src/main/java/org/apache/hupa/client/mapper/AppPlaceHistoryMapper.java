package org.apache.hupa.client.mapper;

import org.apache.hupa.client.place.DefaultPlace;
import org.apache.hupa.client.place.IMAPMessagePlace;
import org.apache.hupa.client.place.MailFolderPlace;
import org.apache.hupa.client.place.MessageSendPlace;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

@WithTokenizers({
    DefaultPlace.Tokenizer.class,
    MailFolderPlace.Tokenizer.class,
    MessageSendPlace.Tokenizer.class,
    IMAPMessagePlace.Tokenizer.class
})
public interface AppPlaceHistoryMapper extends PlaceHistoryMapper {
}
