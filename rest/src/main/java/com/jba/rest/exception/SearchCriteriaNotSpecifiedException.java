package com.jba.rest.exception;

public class SearchCriteriaNotSpecifiedException extends Throwable {
    public SearchCriteriaNotSpecifiedException(String s) {
        super(s);
    }

    public SearchCriteriaNotSpecifiedException() {
        super();
    }

    public SearchCriteriaNotSpecifiedException(String message, Throwable cause) {
        super(message, cause);
    }

    public SearchCriteriaNotSpecifiedException(Throwable cause) {
        super(cause);
    }

    protected SearchCriteriaNotSpecifiedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
