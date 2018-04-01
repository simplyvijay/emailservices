package api.kcorp;

@SuppressWarnings("SerializableHasSerializationMethods")
class EmailServicesException extends Exception {

	private static final long serialVersionUID = 5380604043551324316L;

	public EmailServicesException(String message) {
		super(message);
	}
}
