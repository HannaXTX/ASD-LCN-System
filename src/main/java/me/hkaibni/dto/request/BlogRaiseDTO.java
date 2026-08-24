package me.hkaibni.dto.request;

public class BlogRaiseDTO {

    private String blogId;
    private long raiseCount;
    private boolean raisedByCurrentUser;


    public BlogRaiseDTO() {
    }


    public BlogRaiseDTO(
            String blogId,
            long raiseCount,
            boolean raisedByCurrentUser
    ) {
        this.blogId = blogId;
        this.raiseCount = raiseCount;
        this.raisedByCurrentUser = raisedByCurrentUser;
    }


    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public long getRaiseCount() {
        return raiseCount;
    }

    public void setRaiseCount(long raiseCount) {
        this.raiseCount = raiseCount;
    }

    public boolean isRaisedByCurrentUser() {
        return raisedByCurrentUser;
    }

    public void setRaisedByCurrentUser(boolean raisedByCurrentUser) {
        this.raisedByCurrentUser = raisedByCurrentUser;
    }
}