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

package org.apache.hupa.server.handler;

import java.util.ArrayList;

import javax.mail.Flags;
import javax.mail.Flags.Flag;

import org.apache.hupa.shared.data.Message.IMAPFlag;

/**
 * Util class which helps to convert from hupa internal data representation to javamaill classes
 *
 */
public class JavamailUtil {
	
	/**
	 * Convert the given Flags to a ArrayList of IMAPFlags
	 * 
	 * @param flags
	 * @return imapFlags
	 */
	public static ArrayList<IMAPFlag> convert(Flags flags) {
		ArrayList<IMAPFlag> fList = new ArrayList<IMAPFlag>();
		Flag[] flagArray = flags.getSystemFlags();
		for (int i = 0; i < flagArray.length; i++) {
			Flag flag = flagArray[i];
			if (flag.equals(Flag.SEEN)) {
				fList.add(IMAPFlag.SEEN);
			} else if (flag.equals(Flag.RECENT)) {
				fList.add(IMAPFlag.RECENT);
			} else if (flag.equals(Flag.ANSWERED)) {
				fList.add(IMAPFlag.ANSWERED);
			} else if (flag.equals(Flag.DELETED)) {
				fList.add(IMAPFlag.DELETED);
			}
		}
		return fList;
		
	}
	
	/**
	 * Convert the given ArrayList of IMAPFlags to a Flags object
	 * 
	 * @param imapFlags
	 * @return flags
	 */
	public static Flags convert(ArrayList<IMAPFlag> imapFlags) {
		Flags iFlags = new Flags();
		for ( int i = 0; i< imapFlags.size(); i++) {
			Flag f = null;
			IMAPFlag flag = imapFlags.get(i);
			if (flag.equals(IMAPFlag.SEEN)) {
				f = Flag.SEEN;
			} else if (flag.equals(IMAPFlag.RECENT)) {
				f = Flag.RECENT;
			} else if (flag.equals(IMAPFlag.ANSWERED)) {
				f = Flag.ANSWERED;
			} else if (flag.equals(IMAPFlag.DELETED)) {
				f = Flag.DELETED;
			}
			if (f != null) {
				iFlags.add(f);
			}
		}
		return iFlags;
	}

}
