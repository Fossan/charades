package model.message;

import java.io.Serializable;

/**
 * Created by Piotr Staśkiewicz on 09.01.2017.
 */
public class ChatMessage implements Serializable {

	private String nickname;
	private String message;

	@Override
	public String toString() {
		return nickname + ": " + message;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
