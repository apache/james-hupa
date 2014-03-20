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

package org.apache.hupa.widgets.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event which get fired on any edit operation
 *
 *
 */
public class EditEvent extends GwtEvent<EditHandler>{
    public static final Type<EditHandler> TYPE = new Type<EditHandler>();
    private EventType eType;
    private Object oldValue;
    private Object newValue;

    /**
     * The edit types
     *
     */
     public enum  EventType{
        Start,
        Stop,
        Cancel
    }

     public EditEvent(EventType eType,Object oldValue,Object newValue) {
         this.eType = eType;
         this.oldValue = oldValue;
         this.newValue = newValue;
     }

     /**
      * Return the edit type
      *
      * @return eType
      */
     public EventType getEventType() {
         return eType;
     }

     /**
      * Return the oldvalue of the editing component
      *
      * @return oldValue
      */
     public Object getOldValue() {
         return oldValue;
     }

     /**
      * Return the newvalue of the editing component
      *
      * @return newValue
      */
     public Object getNewValue() {
         return newValue;
     }

    @Override
    protected void dispatch(EditHandler handler) {
        handler.onEditEvent(this);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<EditHandler> getAssociatedType() {
        return TYPE;
    }


}
