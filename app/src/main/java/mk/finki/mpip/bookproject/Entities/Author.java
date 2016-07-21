package mk.finki.mpip.bookproject.Entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Author implements Parcelable {

	private Long id;
	private String name;
	private String surname;
	private String image;
	private String biography;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	@Override
	public String toString() {
		return getName() + " " + getSurname();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(this.id);
		dest.writeString(this.name);
		dest.writeString(this.surname);
		dest.writeString(this.image);
		dest.writeString(this.biography);
	}

	public Author() {
	}

	protected Author(Parcel in) {
		this.id = (Long) in.readValue(Long.class.getClassLoader());
		this.name = in.readString();
		this.surname = in.readString();
		this.image = in.readString();
		this.biography = in.readString();
	}

	public static final Parcelable.Creator<Author> CREATOR = new Parcelable.Creator<Author>() {
		@Override
		public Author createFromParcel(Parcel source) {
			return new Author(source);
		}

		@Override
		public Author[] newArray(int size) {
			return new Author[size];
		}
	};
}
