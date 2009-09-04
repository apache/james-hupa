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

package org.apache.hupa.client.widgets;

import com.google.gwt.widgetideas.client.ProgressBar;
import com.google.gwt.widgetideas.client.ProgressBar.TextFormatter;

import gwtupload.client.BasicProgress;
import gwtupload.client.IUploadStatus;

public class UploadProgress extends BasicProgress {

    ProgressBar prg = new ProgressBar();
    TextFormatter formater = new TextFormatter() {
            protected String getText(ProgressBar bar, double curProgress) {
                    return fileNameLabel.getText() + "  (" + (int) curProgress + " %)";
            }
    };

    public UploadProgress() {
            setProgressWidget(prg);
            prg.setTextFormatter(formater);
    }

    @Override
    public void setPercent(int percent) {
            super.setPercent(percent);
            prg.setProgress(percent);
    }

    @Override
    public IUploadStatus newInstance() {
            return new UploadProgress();
    }

}

