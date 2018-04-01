package api.kcorp.model;

import lombok.*;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

@SuppressWarnings("SerializableHasSerializationMethods")
@Data
@ToString
public class EmailForm implements Serializable {

	private static final long serialVersionUID = 8768353047145226049L;
	@NotNull
	private String purpose;
	@NotNull
	private String name;
	@NotNull
	private String email;
	private String subject;
	private String message;
	private String jd;
	private String division;
	
}
