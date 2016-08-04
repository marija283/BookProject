package mk.finki.mpip.bookproject.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

public class User implements Parcelable {

	private Long id;
	private String fname;
	private String lname;
	private String username;
	private String password;
	private String biography;
	private String image;
	private Date dateCreated;

	private List<Genre> genres;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public List<Genre> getGenres() {
		return genres;
	}

	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}
	
	public void addGenre(Genre genre){
		this.genres.add(genre);
	}
	
	 public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(this.id);
		dest.writeString(this.fname);
		dest.writeString(this.lname);
		dest.writeString(this.username);
		dest.writeString(this.password);
		dest.writeString(this.biography);
		dest.writeString(this.image);
		dest.writeLong(this.dateCreated != null ? this.dateCreated.getTime() : -1);
		dest.writeTypedList(this.genres);
	}

	public User() {
	}

	protected User(Parcel in) {
		this.id = (Long) in.readValue(Long.class.getClassLoader());
		this.fname = in.readString();
		this.lname = in.readString();
		this.username = in.readString();
		this.password = in.readString();
		this.biography = in.readString();
		this.image = in.readString();
		long tmpDateCreated = in.readLong();
		this.dateCreated = tmpDateCreated == -1 ? null : new Date(tmpDateCreated);
		this.genres = in.createTypedArrayList(Genre.CREATOR);
	}

	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
		@Override
		public User createFromParcel(Parcel source) {
			return new User(source);
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	};
}
