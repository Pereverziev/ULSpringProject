package springProject.model;

import javax.validation.constraints.NotBlank;

public class RequestModel {

    @NotBlank
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
