package lk.malindu.fileStorage.dto;

import java.io.Serializable;

public class FileDTO implements Serializable {

    private int id;
    private String url;
    private String type;


    public FileDTO() {
    }

    public FileDTO(String url, String type) {
        this.url = url;
        this.type = type;
    }

    public FileDTO(int id, String url, String type) {
        this.id = id;
        this.url = url;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

