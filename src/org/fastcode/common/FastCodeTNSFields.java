/**
 * This class has been generated by Fast Code Eclipse Plugin
 * For more information please go to http://fast-code.sourceforge.net/
 * @author : ${user}
 * Created : ${today}
 */
package org.fastcode.common;

public class FastCodeTNSFields {

	private String host;
	private String port;
	private String server;
	private String serviceName;

	/**
	 *
	 * getter method for host
	 * @return
	 *
	 */
	public String getHost() {
		return this.host;
	}

	public FastCodeTNSFields(final String host, final String port, final String server, final String serviceName) {
		super();
		this.host = host;
		this.port = port;
		this.server = server;
		this.serviceName = serviceName;
	}

	/**
	 *
	 * setter method for host
	 * @param host
	 *
	 */
	public void setHost(final String host) {
		this.host = host;
	}

	/**
	 *
	 * getter method for port
	 * @return
	 *
	 */
	public String getPort() {
		return this.port;
	}

	/**
	 *
	 * setter method for port
	 * @param port
	 *
	 */
	public void setPort(final String port) {
		this.port = port;
	}

	/**
	 *
	 * getter method for server
	 * @return
	 *
	 */
	public String getServer() {
		return this.server;
	}

	/**
	 *
	 * setter method for server
	 * @param server
	 *
	 */
	public void setServer(final String server) {
		this.server = server;
	}

	/**
	 *
	 * getter method for serviceName
	 * @return
	 *
	 */
	public String getServiceName() {
		return this.serviceName;
	}

	/**
	 *
	 * setter method for serviceName
	 * @param serviceName
	 *
	 */
	public void setServiceName(final String serviceName) {
		this.serviceName = serviceName;
	}
}
