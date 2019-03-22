import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

public class GameView extends View{

	private final int COUNTDOWN = 10;
	private final int RESULTTIME = 3;
	private int seconds;
	private Timer timer;
	private int selected;

	@FXML Text playersText;
	@FXML Label questionLabel;
	@FXML Label questionNumLabel;
	@FXML Label usernameLabel;
	@FXML Label ptsLabel;
	@FXML Label ans1Label, ans2Label, ans3Label, ans4Label;
	@FXML JFXButton ansBtn1, ansBtn2, ansBtn3, ansBtn4;
	@FXML ImageView ansImageView1, ansImageView2, ansImageView3, ansImageView4, ansGreenView, ansRedView;
	@FXML JFXListView playersListView;
	@FXML Label scoreLabel;
	@FXML Label timerLabel;
	@FXML AnchorPane gameAnchor;
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
		questionLabel.setText("");
		ans1Label.setText("");
		ans2Label.setText("");
		ans3Label.setText("");
		ans4Label.setText("");
		questionNumLabel.setText("");
		timerLabel.setText("Timer: " + COUNTDOWN);
		usernameLabel.setText("Username: " + controller.getUsername());
		scoreLabel.setText("Score: 0");

		usernameLabel.setMaxWidth(400);

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

		ansImageView1.setFitWidth(149);
		ansImageView1.setFitHeight(35);
		ansImageView2.setFitWidth(149);
		ansImageView2.setFitHeight(35);
		ansImageView3.setFitWidth(149);
		ansImageView3.setFitHeight(35);
		ansImageView4.setFitWidth(149);
		ansImageView4.setFitHeight(35);

		ansGreenTri = new Image("resources/greenTriangle.png");
		ansGreenButton = new Image("resources/greenButton.png");

		ansRedTri = new Image("resources/redTriangle.png");
		ansRedButton = new Image("resources/redButton.png");

		ansBack1 = new Image("resources/Answer1Triangle.png");
		ansBack2 = new Image("resources/Answer2Triangle.png");

		ansGreenView = new ImageView(ansGreenButton);
		ansRedView = new ImageView(ansRedButton);

		ansGreenView.setFitWidth(70);
		ansGreenView.setFitHeight(30);
		ansRedView.setFitWidth(70);
		ansRedView.setFitHeight(30);

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

		questionLabel.setMaxWidth(430);
		questionLabel.setWrapText(true);
		questionLabel.setAlignment(Pos.CENTER);

		ans1Label.setMaxWidth(180);
		ans1Label.setWrapText(true);
		ans2Label.setMaxWidth(180);
		ans2Label.setWrapText(true);
		ans3Label.setMaxWidth(180);
		ans3Label.setWrapText(true);
		ans4Label.setMaxWidth(180);
		ans4Label.setWrapText(true);

		primaryStage.setOnCloseRequest(e -> {
			try {
				controller.disconnect();
				Platform.exit();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
	}

	@Override
	public void Update()  {
		Platform.runLater(
				() -> {
					this.state = controller.getMystate();
					if (popup != null) {
						popup.hide();
					}
					selected = -1;
					updateButtons();
					questionLabel.setText(state.getCurrentQuestion().getQuestion());
					questionNumLabel.setText("Question " + state.getQuestionNumber() + " of " + state.getnQuestions());
					scoreLabel.setText("Score: " + state.getCurrentPlayer().getScore());
					ptsLabel.setText(state.getCurrentQuestion().getPoints() + " pts");
					state.getPlayersList().add(state.getCurrentPlayer());
					populatePlayers(state.getPlayersList());
					startTimer();
				}
		);
	}

	private void startTimer () {

		timer = new Timer();
		seconds = COUNTDOWN + RESULTTIME;
		timerLabel.setText("Timer: " + COUNTDOWN);

		timer.schedule(new TimerTask() {
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						if (seconds == 0) {
							if (selected == -1) {
								controller.selectAnswer(getWrongAnswer());
							}
							timer.purge();
							timer.cancel();
						}
						else if (seconds == RESULTTIME) {
							timerLabel.setText("Time's Up!");
							showIfAnswerCorrect(selected);
							disableButtons();
							decSeconds();
						}
						else {
							decSeconds();
							if (seconds >= RESULTTIME) {
								int updatedtime = seconds - RESULTTIME;
								timerLabel.setText("Timer: " + updatedtime);
							}
						}
					}
				});
			}
		}, 1000, 1000);
	}

	private int decSeconds () {
		return --seconds;
	}

	private void disableButtons() {
		ansBtn1.setOnMouseClicked(null);
		ansBtn2.setOnMouseClicked(null);
		ansBtn3.setOnMouseClicked(null);
		ansBtn4.setOnMouseClicked(null);
	}

	private void updateButtons() {

		resetButtonImages();

		ans1Label.setText("");
		ans2Label.setText("");
		ans3Label.setText("");
		ans4Label.setText("");
		disableButtons();

		if (state.getCurrentQuestion().getAnswersList().size() > 0) {
			ans1Label.setText(state.getCurrentQuestion().getAnswersList().get(0).getAnswer());
			ansBtn1.setOnMouseClicked(e -> {
				selectAnswer(ansImageView1, ansBtn1, 0);
			});
		}

		if (state.getCurrentQuestion().getAnswersList().size() > 1) {
			ans2Label.setText(state.getCurrentQuestion().getAnswersList().get(1).getAnswer());
			ansBtn2.setOnMouseClicked(e -> {
				selectAnswer(ansImageView2, ansBtn2, 1);
			});
		}

		if (state.getCurrentQuestion().getAnswersList().size() > 2) {
			ans3Label.setText(state.getCurrentQuestion().getAnswersList().get(2).getAnswer());
			ansBtn3.setOnMouseClicked(e -> {
				selectAnswer(ansImageView3, ansBtn3, 2);
			});
		}

		if (state.getCurrentQuestion().getAnswersList().size() > 3) {
			ans4Label.setText(state.getCurrentQuestion().getAnswersList().get(3).getAnswer());
			ansBtn4.setOnMouseClicked(e -> {
				selectAnswer(ansImageView4, ansBtn4, 3);
			});
		}
	}

	public void selectAnswer (ImageView iv, JFXButton btn, int answer) {
		selected = answer;
		controller.selectAnswer(answer);
		setButtonSelect(iv, btn);
	}

	private void showIfAnswerCorrect (int chosen) {
		resetButtonImages();


		if (chosen == 0) {
			if (state.getCurrentQuestion().getAnswersList().get(chosen).isCorrect()) {
				ansImageView1.setImage(ansGreenTri);
				ansBtn1.setGraphic(ansGreenView);
			}
			else {
				ansImageView1.setImage(ansRedTri);
				ansBtn1.setGraphic(ansRedView);
			}
		}
		else if (chosen == 1) {
			if (state.getCurrentQuestion().getAnswersList().get(chosen).isCorrect()) {
				ansImageView2.setImage(ansGreenTri);
				ansBtn2.setGraphic(ansGreenView);
			}
			else {
				ansImageView2.setImage(ansRedTri);
				ansBtn2.setGraphic(ansRedView);
			}
		}
		else if (chosen == 2) {
			if (state.getCurrentQuestion().getAnswersList().get(chosen).isCorrect()) {
				ansImageView3.setImage(ansGreenTri);
				ansBtn3.setGraphic(ansGreenView);
			}
			else {
				ansImageView3.setImage(ansRedTri);
				ansBtn3.setGraphic(ansRedView);
			}
		}
		else if (chosen == 3) {
			if (state.getCurrentQuestion().getAnswersList().get(chosen).isCorrect()) {
				ansImageView4.setImage(ansGreenTri);
				ansBtn4.setGraphic(ansGreenView);
			}
			else {
				ansImageView4.setImage(ansRedTri);
				ansBtn4.setGraphic(ansRedView);
			}
		}

	}

	private void setButtonSelect (ImageView iv, JFXButton btn) {
		resetButtonImages();
		iv.setImage(ansGreenTri);
		btn.setGraphic(ansGreenView);
	}

	private void resetButtonImages() {
		ansImageView1.setImage(ansBack1);
		ansImageView2.setImage(ansBack2);
		ansImageView3.setImage(ansBack1);
		ansImageView4.setImage(ansBack2);

		ansBtn1.setGraphic(ansView2);
		ansBtn2.setGraphic(ansView);
		ansBtn3.setGraphic(ansView4);
		ansBtn4.setGraphic(ansView3);
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

		Collections.sort(players, new Comparator<Player>() {
			@Override
			public int compare(Player o1, Player o2) {
				return o2.getScore() - o1.getScore();
			}
		});

		playersListView.getItems().clear();
		playersListView.getStylesheets().add("theme.css");
		playersListView.getStyleClass().add("jfx-list-cell");
		for(Player p: players){
			AnchorPane playerAnchor = new AnchorPane();
			Label playerName = new Label(p.getName());
			Label playerScore = new Label(p.getScore()+"");

			playerName.setMaxWidth(120);

			if(p == state.getCurrentPlayer()) {
				playerName.getStyleClass().add("selected-player-list");
				playerScore.getStyleClass().add("selected-player-list");
			}
			else {
				playerName.getStyleClass().add("label-players");
				playerScore.getStyleClass().add("label-players");
			}

			AnchorPane.setLeftAnchor(playerScore, 130.0);

			playerAnchor.getChildren().add(playerName);
			playerAnchor.getChildren().add(playerScore);

			playersListView.getItems().add(playerAnchor);
		}
	}

	public void popUp(){
		Platform.runLater(
				() -> {
					popup = new JFXPopup();
					anchorPane = new AnchorPane();
					Image loading = new Image("resources/loading.gif");
					ImageView viewLoading = new ImageView(loading);
					Label words = new Label("Waiting for Others");

					gameAnchor.getStylesheets().add("theme.css");
					anchorPane.getStyleClass().add("anchorpane-Pop");
					words.getStyleClass().add("label-pop");
					words.setMaxWidth(180);
					words.setWrapText(true);
					words.setAlignment(Pos.CENTER);

					viewLoading.setFitHeight(130);
					viewLoading.setFitWidth(300);

					anchorPane.setMinSize(300, 300);
					anchorPane.setMaxSize(300, 300);

					words.setMaxWidth(Double.MAX_VALUE);
					words.setAlignment(Pos.CENTER);

					AnchorPane.setTopAnchor(words, 70.0);
					AnchorPane.setTopAnchor(viewLoading, 150.0);
					AnchorPane.setLeftAnchor(words, 20.0);

					anchorPane.getChildren().add(words);
					anchorPane.getChildren().add(viewLoading);
					popup.setPopupContent(anchorPane);
					popup.show(gameAnchor, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, 405.0, 150.0);
				});
	}



}
