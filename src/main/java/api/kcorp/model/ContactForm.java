package api.kcorp.model;

import lombok.*;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

@SuppressWarnings("SerializableHasSerializationMethods")
@Data
@ToString
public class ContactForm implements Serializable {

	private static final long serialVersionUID = -9074494598542244504L;
	@NotNull
	private String name;
	@NotNull
	private String email;
	@NotNull
	private String subject;
	@NotNull
	private String message;
	@NotNull
	private Boolean optin;
}
