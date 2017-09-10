package model;

public class CustomToken {
	private String name;
	private int line;
	private int type;
	private String value;
	
	public CustomToken(String name, int line, int type, String value) {
		this.name = name;
		this.line = line;
		this.type = type;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}

	public int getLine() {
		return line;
	}

	@Override 
	public String toString() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + type;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomToken other = (CustomToken) obj;
		if (type != other.type)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
}