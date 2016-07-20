package mk.finki.mpip.bookproject.Entities;

import java.util.List;

public class Book {
    private Long id;
    private String title;
    private String description;
    private String image;
    private Author author;
    private List<Genre> genres;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
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

    public boolean hasGenre(String genreName) {
        if(genres != null){
            for(Genre genre : genres){
                if(genre.getGenreName().equals(genreName))
                    return true;
            }
        }
        return false;
    }


}
