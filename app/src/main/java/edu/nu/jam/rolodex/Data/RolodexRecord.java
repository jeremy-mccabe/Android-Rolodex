package edu.nu.jam.rolodex.Data;

public class RolodexRecord
{
	private String lastName;
	private String firstName;
	private String middleName;
	private String phoneNumber;

	public RolodexRecord()
	{
		this.lastName = "";
		this.firstName = "";
		this.middleName = "";
		this.phoneNumber = "";
	}

	public RolodexRecord(String lastName, String firstName, String phoneNumber)
	{
		this.lastName = lastName;
		this.firstName = firstName;
		this.middleName = "";
		this.phoneNumber = phoneNumber;
	}

	public RolodexRecord(String lastName, String firstName, String middleName, String phoneNumber)
	{
		this.lastName = lastName;
		this.firstName = firstName;
		this.middleName = middleName;
		this.phoneNumber = phoneNumber;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getMiddleName()
	{
		return middleName;
	}

	public void setMiddleName(String middleName)
	{
		this.middleName = middleName;
	}

	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}

	public String nameToString()
	{
		return String.format("%s, %s %s",
				this.getLastName(),
				this.getFirstName(),
				this.getMiddleName()
		);
	}

	@Override
	public String toString()
	{
		return this.lastName
				+ " "
				+ this.firstName
				+ " "
				+ this.middleName
				+ " "
				+ this.phoneNumber;
	}
}
