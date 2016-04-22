package com.yifan.domain.things;

public class Thing {
	private String description;
	private String due;
	private String color;
	private String completed;
	private String id;
	private String ownerId;

	public Thing( ) {
		
	}
	
	public Thing( Builder b ) {
		this.description = b.description;
		this.due = b.due;
		this.color = b.color;
		this.completed = b.completed;
		this.id = b.id;
		this.ownerId = b.ownerId;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDue() {
		return due;
	}

	public void setDue(String due) {
		this.due = due;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getCompleted() {
		return completed;
	}

	public void setCompleted(String completed) {
		this.completed = completed;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}



	public static class Builder {
		private String description;
		private String due;
		private String color;
		private String completed;
		private String id;
		private String ownerId;
		
		public Builder description( String description ) {
			this.description = description;
			return this;
		}
		
		public Builder due( String due ) {
			this.due = due;
			return this;
		}
		
		public Builder color( String color ) {
			this.color = color;
			return this;
		}
		
		public Builder completed( String completed ) {
			this.completed = completed;
			return this;
		}
		
		
		public Builder id( String id ) {
			this.id = id;
			return this;
		}
		
		public Builder ownerId( String ownerId ) {
			this.ownerId = ownerId;
			return this;
		}
		
		public Thing build() {
			return new Thing( this );
		}
	}
}
