package model;

import java.io.Serializable;

public class Info implements Serializable {
	//field
	private String id;
	private String name;
	private String follower;
	private String following;
	private String url;

	//Constructor
	Info (){}

	public Info(String id, String name, String follower, String following) {
		super();
		this.id = id;
		this.name = name;
		this.follower = follower;
		this.following = following;
		this.url = "https://newspicks.com/user/" + id;
	}

	//setter,getter
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFollower() {
		return follower;
	}

	public void setFollower(String follower) {
		this.follower = follower;
	}

	public String getFollowing() {
		return following;
	}

	public void setFollowing(String following) {
		this.following = following;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	};

	//setter,getter


}
