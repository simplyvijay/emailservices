package api.kcorp.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@SuppressWarnings("SerializableHasSerializationMethods")
@Data
@ToString
public class Status implements Serializable {

	private static final long serialVersionUID = 3659172113094222522L;
	private final String message;
	private final Integer code;
	
}
