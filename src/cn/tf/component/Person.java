package cn.tf.component;

public class Person {
	
	private Integer pid;
	private String name;
	
/*	private String homeAddress;
	private String homeTel;
	
	private String companyAddress;
	private String companyTel;*/
	
	private Address homeAddress;
	private Address companyAddress;
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Address getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(Address homeAddress) {
		this.homeAddress = homeAddress;
	}
	public Address getCompanyAddress() {
		return companyAddress;
	}
	public void setCompanyAddress(Address companyAddress) {
		this.companyAddress = companyAddress;
	}
	
	
	

}
