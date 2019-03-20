

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TriviaGame {

	private List<Question> questionsList;
	private List<Player> playersList;
	private Question currentQuestion;
	private Connection connection;
	private int nQuestions;
	private boolean gameDone;

	public TriviaGame (int nQuestions) {

		this.nQuestions = nQuestions;
		playersList = new ArrayList<>();

		dbConnection connector = new dbConnection();
		connection = connector.getConnection();
	}

	public GameState getGameState(Player player) {

		GameState state = new GameState();
		List<Player> otherplayers = new ArrayList<>(playersList);
		otherplayers.remove(player);

		state.setCurrentQuestion(currentQuestion);
		state.setCurrentPlayer(player);
		state.setPlayersList(otherplayers);
		state.setDone(gameDone);
		state.setQuestionNumber(getQuestionNumber());

		System.out.println(playersList);

		return state;
	}


	private List<Question> buildQuestions (List selectedNums) {

		QuestionDAODB questionsDAO = new QuestionDAODB(connection);
		AnswersDAODB answersDAO = new AnswersDAODB(connection);

		List<Question> readyQs = new ArrayList<>();

		for (Object n: selectedNums) {
			int selected = ((Integer)n);

			Question q = questionsDAO.getQuestion(selected);
			q.setAnswersList(answersDAO.getAnswers(selected));

			readyQs.add(q);
		}

		return readyQs;
	}

	private List getSelectedQuestionIDs (int nSelected){
		QuestionDAODB test = new QuestionDAODB(connection);
		int totalQuestions = test.getNQuestions();

		if (nSelected > totalQuestions) {
			nSelected = totalQuestions;
		}

		// get all songs and shuffle
		ArrayList randompool = new ArrayList();
		for (int i=0; i<totalQuestions; i++) {
			randompool.add(i+1);
		}
		Collections.shuffle(randompool);

		// select first n IDs
		ArrayList selectedpool = new ArrayList();
		for(int i=0; i<nSelected; i++) {
			selectedpool.add(randompool.get(i));
		}

		return selectedpool;
	}

	public boolean startGame () {
		gameDone = false;
		System.out.println("\n\nWELCOME TO TRIVIA GAME\n\n");
		questionsList = buildQuestions(getSelectedQuestionIDs(nQuestions));
		for (Question q: questionsList) {
			System.out.println(q.getQuestion());
		}
		return true;
	}

	public boolean connectPlayer(Player dude) {
		playersList.add(dude);
		System.out.println(dude.getName() + " has joined the game");
		return true;
	}

	public boolean disconnectPlayer(String username) {

		for (Player p: playersList) {
			if (p.getName().equals(username)){
				playersList.remove(p);
				System.out.println(p.getName() + " has left the game");
				break;
			}
		}

		return true;
	}

	public boolean askQuestion () {

		for (Player p: playersList) {
			p.setAnswered(false);
		}

		if(questionsList.isEmpty()) {
			gameDone = true;
			return false;
		}

		currentQuestion = questionsList.get(0);

		System.out.println(questionsList.get(0).getQuestion() + "(" + questionsList.get(0).getPoints() + " Points)");
		System.out.println("--------CHOICES--------");

		for (Answer a: questionsList.get(0).getAnswersList()) {
			System.out.println(a.getAnswer());
		}

		questionsList.remove(0);

		return true;
	}

	public boolean checkAnswer (Answer answer, Player dude) {

		for (Player p: playersList) {

			if (p.getName().equals(dude.getName())) {
				p.setAnswered(true);

				if (answer.isCorrect()) {
					int score = p.getScore();
					p.setScore(score + currentQuestion.getPoints());
					return true;
				}
				break;
			}
		}

		return false;
	}

	public boolean questionDone() {

		for (Player p: playersList) {
			if (!p.isAnswered()) {
				System.out.println("not all finish");
				return false;
			}
		}

		return true;
	}

	public List<Player> getPlayersList () {
		return playersList;
	}

	public int getQuestionNumber () {
		return nQuestions - questionsList.size();
	}

	public void setPlayersList(List<Player> playersList) {
		this.playersList = playersList;
	}

	public void endGame () {
		gameDone =true;
		connection = null;
	}

	public boolean isGameDone() {
		return gameDone;
	}

	public Question getCurrentQuestion() {
		return currentQuestion;
	}

}
