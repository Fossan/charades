package model.packet;

import model.image.SerializableImage;
import model.message.ChatMessage;

import java.io.Serializable;

/**
 * Created by Piotr Staśkiewicz on 03.01.2017.
 */
public class Packet implements Serializable {

	private SerializableImage image;
	private ChatMessage chatText = new ChatMessage();

	public SerializableImage getImage() {
		return image;
	}

	public void setImage(SerializableImage image) {
		this.image = image;
	}

	public ChatMessage getChatText() {
		return chatText;
	}

	public void setChatText(ChatMessage chatText) {
		this.chatText = chatText;
	}


}
