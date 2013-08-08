package org.jboss.as.quickstart.hibernate4.model;

public class EmailAddress {

	private String user;
	private String domain;
	@SuppressWarnings("unused")
	private EmailAddress() {
		this(null, null);
	}
	public EmailAddress(String user, String domain) {
		if(user == null || domain == null)
			throw new IllegalArgumentException("user and domain are mandatory");
		this.user = user;
		this.domain = domain;
	}
	
	public static EmailAddress valueOf(String v) {
		if(v != null && v.contains("@")) {
			String[] tokens = v.split("@");
			return new EmailAddress(tokens[0], tokens[1]);
		}
		return null;
	}
	
	public String getUser() {
		return user;
	}
	public String getDomain() {
		return domain;
	}
	
	public String getValue() {
		return user != null && domain != null ? toString() : null;
	}

	@Override
	public String toString() {
		return user + "@" + domain;
	}
}
