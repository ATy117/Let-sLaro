package Model;

import java.io.Serializable;

public class PlayerResponse implements Serializable {

	private static final long serialVersionUID = 9813788888923L;

	private Player player;
	private Answer answer;

	public PlayerResponse (Player player, Answer answer) {
		this.player = player;
		this.answer = answer;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}

}
