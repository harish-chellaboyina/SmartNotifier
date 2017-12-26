package DTO;

public class MailInfoDTO {
	
	private String from;
	private String command;
	
	public MailInfoDTO(String from, String command) {
		this.from = from;
		this.command = command;
		
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	
	
}
