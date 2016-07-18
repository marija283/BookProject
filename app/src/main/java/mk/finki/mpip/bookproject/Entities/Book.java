package mk.finki.mpip.bookproject.Entities;

/**
 * Created by Riste on 14.7.2016.
 */
public class Book {

    private Long id;
    private String title;
    private String description;
    private String image;

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }


    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }


    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return title + description.substring(1,10);
    }
}
