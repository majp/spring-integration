/*
 * Copyright 2002-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.ip.udp;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import org.springframework.integration.core.MessagingException;

/**
 * @author Gary Russell
 * @since 2.0
 */
public class MulticastReceivingChannelAdapter extends UnicastReceivingChannelAdapter {

	protected String group;


	/**
	 * @param port
	 */
	public MulticastReceivingChannelAdapter(String group, int port) {
		super(port);
		this.group = group;
	}

	/**
	 * @param port
	 * @param lengthCheck
	 */
	public MulticastReceivingChannelAdapter(String group, int port, boolean lengthCheck) {
		super(port, lengthCheck);
		this.group = group;
	}


	@Override
	protected synchronized DatagramSocket getSocket() {
		if (this.socket == null) {
			try {
				MulticastSocket socket = new MulticastSocket(this.port);
				socket.setSoTimeout(this.soTimeout);
				if (this.soReceiveBufferSize > 0) {
					socket.setReceiveBufferSize(this.soReceiveBufferSize);
				}
				socket.joinGroup(InetAddress.getByName(this.group));
				this.socket = socket;
			}
			catch (IOException e) {
				throw new MessagingException("failed to create DatagramSocket", e);
			}
		}
		return this.socket;
	}

}
