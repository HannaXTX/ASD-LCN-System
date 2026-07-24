package me.hkaibni.dto;

public class SearchResultDTO {

    private String type;
    private String id;
    private Object display;

    public SearchResultDTO(String type, Object id, Object display) {
        this.type = type;
        this.id = String.valueOf(id);
        this.display = display;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public Object getDisplay() {
        return display;
    }
}