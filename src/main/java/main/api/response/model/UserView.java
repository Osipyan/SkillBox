package main.api.response.model;

public class UserView {
    private int id;
    private String name;
    private String photo;

    public int getId() {
        return id;
    }

    public UserView setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserView setName(String name) {
        this.name = name;
        return this;
    }

    public String getPhoto() {
        return photo;
    }

    public UserView setPhoto(String photo) {
        this.photo = photo;
        return this;
    }
}
