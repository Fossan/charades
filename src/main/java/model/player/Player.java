package model.player;

import java.io.Serializable;

/**
 * Created by Piotr Staśkiewicz on 09.12.2016.
 */

public class Player implements Serializable {

	private String nickname;
	private int score;
	private int id;
	private boolean isDrawing = false;
	private boolean isReady = false;
	private String lastAction;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean isDrawing() {
		return isDrawing;
	}

	public void setDrawing(boolean drawing) {
		isDrawing = drawing;
	}

	public boolean isReady() {
		return isReady;
	}

	public void setReady(boolean ready) {
		isReady = ready;
	}

	public String getLastAction() {
		return lastAction;
	}

	public void setLastAction(String lastAction) {
		this.lastAction = lastAction;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
