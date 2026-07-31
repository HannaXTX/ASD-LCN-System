package me.hkaibni.dto.search;

public abstract class SearchDTO {

    private Integer page;
    private Integer pageSize;
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int resolvedPage() {
        return page == null || page < 0 ? 0 : page;
    }

    public int resolvedPageSize() {
        if (pageSize == null || pageSize <= 0) {
            return 20;
        }

        return Math.min(pageSize, 100);
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}