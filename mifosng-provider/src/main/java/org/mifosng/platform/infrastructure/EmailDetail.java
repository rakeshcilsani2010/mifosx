package org.mifosng.platform.infrastructure;

public class EmailDetail {

	private final String organisationName;
	private final String username;
	private final String contactName;
	private final String address;

	public EmailDetail(String organisationName, String contactName, String address, String username) {
		this.organisationName = organisationName;
		this.contactName = contactName;
		this.address = address;
		this.username = username;
	}

	public String getOrganisationName() {
		return this.organisationName;
	}

	public String getUsername() {
		return this.username;
	}

	public String getContactName() {
		return this.contactName;
	}

	public String getAddress() {
		return this.address;
	}
}