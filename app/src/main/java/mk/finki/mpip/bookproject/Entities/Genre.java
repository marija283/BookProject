package mk.finki.mpip.bookproject.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;



public class Genre implements Parcelable {

	private Long id;
	private String genreName;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGenreName() {
		return genreName;
	}

	public void setGenreName(String genreName) {
		this.genreName = genreName;
	}


	
	@Override
	public String toString() {
		String s ="genre: "+genreName;
		
		 return s;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(this.id);
		dest.writeString(this.genreName);
	}

	public Genre() {
	}

	protected Genre(Parcel in) {
		this.id = (Long) in.readValue(Long.class.getClassLoader());
		this.genreName = in.readString();
	}

	public static final Parcelable.Creator<Genre> CREATOR = new Parcelable.Creator<Genre>() {
		@Override
		public Genre createFromParcel(Parcel source) {
			return new Genre(source);
		}

		@Override
		public Genre[] newArray(int size) {
			return new Genre[size];
		}
	};
}
