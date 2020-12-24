package pharmacy.reference.data;

public class Response<T> {

    public final T body;
    public final State state;

    public Response(T body, State state) {
        this.body = body;
        this.state = state;
    }

    public enum State {
        SUCCESS,
        ERROR
    }

}
