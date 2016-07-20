package mk.finki.mpip.bookproject.Entities;

import java.util.List;



public class Genre {

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
}
