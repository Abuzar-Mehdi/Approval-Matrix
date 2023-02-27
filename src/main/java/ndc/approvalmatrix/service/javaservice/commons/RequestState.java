package ndc.approvalmatrix.service.javaservice.commons;

public enum RequestState {


    NOT_ASSIGNED(1,"NOT ASSIGNED"),
    IN_PROGRESS(2,"IN PROGRESS"),
    PENDING(3,"PENDING"),
    APPROVED(4,"APPROVED"),
    REJECTED(5,"REJECTED"),
    CANCEL(6,"CANCEL");


    private final int code;
    private final String name;

    RequestState(int code,String name) {
        this.code = code;
        this.name = name;
    }

    public static RequestState valueOf(int code) {
        RequestState state = null;
        switch (code) {
            case 1:
                state = NOT_ASSIGNED;
                break;
            case 2:
                state = IN_PROGRESS;
                break;
            case 3:
                state = PENDING;
                break;
            case 4:
                state = APPROVED;
                break;
            case 5:
                state = REJECTED;
                break;
            case 6:
                state = CANCEL;
                break;
        }
        return state;
    }
}
