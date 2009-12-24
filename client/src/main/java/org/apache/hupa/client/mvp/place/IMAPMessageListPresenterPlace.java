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
package org.apache.hupa.client.mvp.place;


import org.apache.hupa.client.mvp.IMAPMessageListPresenter;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.inject.Provider;

import net.customware.gwt.presenter.client.gin.ProvidedPresenterPlace;
import net.customware.gwt.presenter.client.place.PlaceRequest;

public class IMAPMessageListPresenterPlace extends ProvidedPresenterPlace<IMAPMessageListPresenter>{

    private final static String ROWS_PER_PAGE_INDEX = "rowsPerPageIndex";
    private final static String PAGE = "page";
    private final static String SEARCH = "search";

    @Inject
    public IMAPMessageListPresenterPlace(Provider<IMAPMessageListPresenter> presenter) {
        super(presenter);
    }

    @Override
    public String getName() {
        return "MessageList";
    }

    @Override
    protected void preparePresenter(PlaceRequest request, IMAPMessageListPresenter presenter) {
        int page = 0;
        try {
            page = Integer.parseInt(request.getParameter(PAGE, "0"));
            if (page < 0 ) {
                page = 0;
            }
        } catch (NumberFormatException e) {
            // ignore
        }
        presenter.getDisplay().goToPage(page);
        
        int rowsPerPageIndex = 0;
        try {
            rowsPerPageIndex = Integer.parseInt(request.getParameter(ROWS_PER_PAGE_INDEX, "0"));
            if (rowsPerPageIndex < 0) {
                rowsPerPageIndex = 0;
            }
        } catch (NumberFormatException e) {
            // ignore
        }

        String searchValue = request.getParameter(SEARCH, "");
        presenter.getDisplay().getSearchValue().setValue(searchValue);
        
        GWT.log("PRES="+request.toString(),null);

    }

    @Override
    protected PlaceRequest prepareRequest(PlaceRequest request, IMAPMessageListPresenter presenter) {
        request = request.with(PAGE, presenter.getDisplay().getCurrentPage() +"");
        request = request.with(ROWS_PER_PAGE_INDEX, presenter.getDisplay().getRowsPerPageIndex() + "");
      
        String searchValue = presenter.getDisplay().getSearchValue().getValue();
        if (searchValue != null && searchValue.length() > 0) {
            request = request.with(SEARCH, searchValue);

        }
        
        GWT.log("REQ="+request.toString(),null);

        return request;
    }
    
    
}
