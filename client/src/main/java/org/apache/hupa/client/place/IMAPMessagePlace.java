package org.apache.hupa.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class IMAPMessagePlace extends Place {

  @Prefix("IMAPMessage")
  public static class Tokenizer implements PlaceTokenizer<IMAPMessagePlace> {

    @Override
    public IMAPMessagePlace getPlace(String token) {
      return new IMAPMessagePlace();
    }

    @Override
    public String getToken(IMAPMessagePlace place) {
      return "IMAPMessage";
    }
  }
  
  public String toString(){
	  return this.getClass().getName()+"->[IMAPMessage]";
  }

}
