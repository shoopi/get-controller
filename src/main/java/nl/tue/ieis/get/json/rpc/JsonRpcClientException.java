package main.java.nl.tue.ieis.get.json.rpc;

public class JsonRpcClientException extends RuntimeException {

	private static final long serialVersionUID = -216210306174506617L;

	public JsonRpcClientException(String message) {
        super(message);
    }

    public JsonRpcClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
