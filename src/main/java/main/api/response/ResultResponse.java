package main.api.response;

import java.util.Map;

public class ResultResponse {
    private boolean result;
    private Map<String, String> errors;
    private int id;

    public boolean isResult() {
        return result;
    }

    public ResultResponse setResult(boolean result) {
        this.result = result;
        return this;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public ResultResponse setErrors(Map<String, String> errors) {
        this.errors = errors;
        return this;
    }

    public int getId() {
        return id;
    }

    public ResultResponse setId(int id) {
        this.id = id;
        return this;
    }
}
