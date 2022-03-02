package main;
public class MyProcess {
	private String errorMessage;
	protected void error(String errorMessage) {
		this.errorMessage = errorMessage;
		Thread.currentThread().interrupt();
	}
	public String getErrorMessage() {
		return errorMessage;
	}
}