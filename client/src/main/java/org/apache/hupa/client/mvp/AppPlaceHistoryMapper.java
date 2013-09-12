package org.apache.hupa.client.mvp;

import org.apache.hupa.client.place.DefaultPlace;
import org.apache.hupa.client.place.MailInboxPlace;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

@WithTokenizers({
    DefaultPlace.Tokenizer.class,
    MailInboxPlace.Tokenizer.class
})
public interface AppPlaceHistoryMapper extends PlaceHistoryMapper {
}
