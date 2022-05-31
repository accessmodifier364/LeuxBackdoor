package me.accessmodifier364.leuxbackdoor.client.modules;

public enum Category {
	player("Player",  false),
	combat("Combat",  false),
	movement("Movement",  false),
	render("Render", false),
	exploit("Exploit", false),
	misc("Misc",  false),
	client("Client",  false),
	hidden("Hidden", true);

	String name;
	boolean isHidden;

	Category(String name, boolean isHidden) {
		this.name   = name;
		this.isHidden = isHidden;
	}

	public boolean is_hidden() {
		return this.isHidden;
	}

	public String get_name() {
		return this.name;
	}
}