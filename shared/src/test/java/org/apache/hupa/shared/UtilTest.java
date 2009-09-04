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

package org.apache.hupa.shared;

import junit.framework.TestCase;

public class UtilTest extends TestCase{
	private String html = "&lt&ltblah&gt&ltwhatever&gt&gt&ltblub&gt<br>lala&lt";
	private String text = "<<blah><whatever>><blub>\nlala<";
	
	public void testText2HTML() {
		assertEquals(Util.toHtml(text),html);
	}
	
	public void testHTML2Text() {
		assertEquals(Util.toString(html),text);
	}
}
