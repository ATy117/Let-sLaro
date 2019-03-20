import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class GameView extends View{

	private final int COUNTDOWN = 10;
	private int seconds;
	private Timer timer;

	@FXML Text playersText;
	@FXML Label questionLabel;
	@FXML Label questionNumLabel;
	@FXML Label usernameLabel;
	@FXML Label ans1Label, ans2Label, ans3Label, ans4Label;
	@FXML JFXButton ansBtn1, ansBtn2, ansBtn3, ansBtn4;
	@FXML ImageView ansImageView1, ansImageView2, ansImageView3, ansImageView4, ansGreenView, ansRedView;
	@FXML JFXListView playersListView;
	@FXML Label scoreLabel;
	@FXML Label timerLabel;
	Image ansGreenTri, ansRedTri, ansGreenButton, ansRedButton, ansBack1, ansBack2;
	ImageView ansView, ansView2, ansView3, ansView4;


	public GameView(ClientController controller, Stage primaryStage) {
		super(controller);
		this.primaryStage = primaryStage;

		System.out.println("You are in game view");

		FXMLLoader loader = new FXMLLoader(getClass().getResource("gameTemplate.fxml"));
		loader.setController(this);

		StageManager sm = new StageManager(primaryStage);
		sm.loadScene(loader);
		sm.setWindowName("Trivia");

		init();
	}

	public void init(){
		this.state = controller.getMystate();
		questionLabel.setText("Waiting for enough players to connect.");
		ans1Label.setText("");
		ans2Label.setText("");
		ans3Label.setText("");
		ans4Label.setText("");
		questionNumLabel.setText("");
		timerLabel.setText("Timer: " + COUNTDOWN);
		usernameLabel.setText("Username: " + controller.getUsername());
		scoreLabel.setText("Score: 0");

		DropShadow dropShadow = new DropShadow();
		dropShadow.setRadius(7.0);
		dropShadow.setColor(Color.color(0, 0, 0.10));
		playersText.setEffect(dropShadow);
		questionLabel.setEffect(dropShadow);
		scoreLabel.setEffect(dropShadow);
		ans1Label.setEffect(dropShadow);
		ans2Label.setEffect(dropShadow);
		ans3Label.setEffect(dropShadow);
		ans4Label.setEffect(dropShadow);

		ansGreenTri = new Image("resources/greenTriangle.png");
		ansGreenButton = new Image("resources/greenButton.png");

		ansRedTri = new Image("resources/redTriangle.png");
		ansRedButton = new Image("resources/redButton.png");

		ansBack1 = new Image("resources/Answer1Triangle.png");
		ansBack2 = new Image("resources/Answer2Triangle.png");

		ansGreenView = new ImageView(ansGreenButton);
		ansRedView = new ImageView(ansRedButton);

		Image ansTri = new Image("resources/ansButton.png");
		Image ansTri2 = new Image("resources/ansButton2.png");
		ansView = new ImageView(ansTri);
		ansView2 = new ImageView(ansTri2);
		ansView3 = new ImageView(ansTri);
		ansView4 = new ImageView(ansTri2);
		ansBtn1.setGraphic(ansView2);
		ansBtn2.setGraphic(ansView);
		ansBtn3.setGraphic(ansView4);
		ansBtn4.setGraphic(ansView3);
		ansView.setFitWidth(70);
		ansView.setFitHeight(30);
		ansView2.setFitWidth(70);
		ansView2.setFitHeight(30);
		ansView3.setFitWidth(70);
		ansView3.setFitHeight(30);
		ansView4.setFitWidth(70);
		ansView4.setFitHeight(30);
	}

	@Override
	public void Update()  {
		this.state = controller.getMystate();
		updateButtons();
		questionLabel.setText(state.getCurrentQuestion().getQuestion());
		questionNumLabel.setText("Question " + state.getQuestionNumber() + " of " + state.getnQuestions());
		scoreLabel.setText("Score: " + state.getCurrentPlayer().getScore());
		populatePlayers(state.getPlayersList());
		startTimer();
	}

	private void startTimer () {

		timer = new Timer();
		seconds = COUNTDOWN;
		timerLabel.setText("Timer: " + seconds);

		timer.schedule(new TimerTask() {
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						if (seconds == 0) {
							try {
								timer.cancel();
								timer.purge();
								controller.selectAnswer(getWrongAnswer());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						else {
							System.out.println(decSeconds());
							timerLabel.setText("Timer: " + seconds);
						}
					}
				});
			}
		}, 1000, 1000);
	}

	private int decSeconds () {
		return --seconds;
	}


	private void updateButtons() {

		ansView.setImage(ansBack2);
		ansView2.setImage(ansBack1);
		ansView3.setImage(ansBack2);
		ansView4.setImage(ansBack1);
		ansBtn1.setGraphic(ansView2);
		ansBtn2.setGraphic(ansView);
		ansBtn3.setGraphic(ansView4);
		ansBtn4.setGraphic(ansView3);
		ans1Label.setText("");
		ans2Label.setText("");
		ans3Label.setText("");
		ans4Label.setText("");
		ans1Label.setOnMouseClicked(e -> {});
		ans2Label.setOnMouseClicked(e -> {});
		ans3Label.setOnMouseClicked(e -> {});
		ans4Label.setOnMouseClicked(e -> {});

		if (state.getCurrentQuestion().getAnswersList().size() > 0) {
			ans1Label.setText(state.getCurrentQuestion().getAnswersList().get(0).getAnswer());
			ansBtn1.setOnMouseClicked(e -> {
				try {
					timer.cancel();
					timer.purge();
					controller.selectAnswer(0);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			});
		}

		if (state.getCurrentQuestion().getAnswersList().size() > 1) {
			ans2Label.setText(state.getCurrentQuestion().getAnswersList().get(1).getAnswer());
			ansBtn2.setOnMouseClicked(e -> {
				try {
					timer.cancel();
					timer.purge();
					controller.selectAnswer(1);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			});
		}

		if (state.getCurrentQuestion().getAnswersList().size() > 2) {
			ans3Label.setText(state.getCurrentQuestion().getAnswersList().get(2).getAnswer());
			ansBtn3.setOnMouseClicked(e -> {
				try {
					timer.cancel();
					timer.purge();
					controller.selectAnswer(2);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			});
		}

		if (state.getCurrentQuestion().getAnswersList().size() > 3) {
			ans4Label.setText(state.getCurrentQuestion().getAnswersList().get(3).getAnswer());
			ansBtn4.setOnMouseClicked(e -> {
				try {
					timer.cancel();
					timer.purge();
					controller.selectAnswer(3);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			});
		}
	}

	private int getWrongAnswer () {

		for (int i=0; i< state.getCurrentQuestion().getAnswersList().size(); i++) {
			if (!state.getCurrentQuestion().getAnswersList().get(i).isCorrect()) {
				return i;
			}
		}
		return 0;
	}


	public void populatePlayers(List<Player> players){
		playersListView.getItems().clear();
		playersListView.getStylesheets().add("theme.css");
		playersListView.getStyleClass().add("jfx-list-cell");
		for(Player p: players){
			AnchorPane playerAnchor = new AnchorPane();
			Label playerName = new Label(p.getName());
			Label playerScore = new Label(p.getScore()+"");

			playerName.getStyleClass().add("label-players");
			playerScore.getStyleClass().add("label-players");

			AnchorPane.setLeftAnchor(playerScore, 130.0);

			playerAnchor.getChildren().add(playerName);
			playerAnchor.getChildren().add(playerScore);

			playersListView.getItems().add(playerAnchor);
		}
	}
}
