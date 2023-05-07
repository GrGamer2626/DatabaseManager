package com.mcfan.command.util;

public class Command {

	private String name;
	private CommandExecutor executor;
	private String correctUsage = "";
	private String description = "";
	
	public Command(String name, CommandExecutor executor) {
		this.name = name;
		this.executor = executor;
		CommandManager.getCommandMap().put(name, this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public CommandExecutor getExecutor() {
		return executor;
	}
	
	public void setExecutor(CommandExecutor executor) {
		this.executor = executor;
	}
	
	public String getCorrectUsage() {
		return correctUsage;
	}
	
	public void setCorrectUsage(String correctUsage) {
		this.correctUsage = correctUsage;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
