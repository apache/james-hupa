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

import com.google.inject.Inject;
import com.google.inject.Provider;

import net.customware.gwt.presenter.client.gin.ProvidedPresenterPlace;
import net.customware.gwt.presenter.client.place.PlaceRequest;

public class IMAPMessageListPresenterPlace extends ProvidedPresenterPlace<IMAPMessageListPresenter>{

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
        int count = 20;
        try {
            count = Integer.parseInt(request.getParameter("count", "20"));
        } catch (NumberFormatException e) {
            // ignore
        }
        int page = 0;
        try {
            page = Integer.parseInt(request.getParameter("page", "0"));
        } catch (NumberFormatException e) {
            // ignore
        }
        String folder = request.getParameter("folder", "INBOX");
        presenter.getDisplay();
        super.preparePresenter(request, presenter);
    }

    @Override
    protected PlaceRequest prepareRequest(PlaceRequest request, IMAPMessageListPresenter presenter) {
        // TODO Auto-generated method stub
        return super.prepareRequest(request, presenter);
    }
    
    
}
