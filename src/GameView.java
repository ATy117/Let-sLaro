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

		questionLabel.setMaxWidth(320);
		questionLabel.setWrapText(true);

		primaryStage.setOnCloseRequest(e -> {
			try {
				controller.disconnect();
				timer.cancel();
				timer.purge();
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
					updateButtons();
					questionLabel.setText(state.getCurrentQuestion().getQuestion());
					questionNumLabel.setText("Question " + state.getQuestionNumber() + " of " + state.getnQuestions());
					scoreLabel.setText("Score: " + state.getCurrentPlayer().getScore());
					state.getPlayersList().add(state.getCurrentPlayer());
					populatePlayers(state.getPlayersList());
					startTimer();
				}
		);
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
								chooseAnswer(getWrongAnswer());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						else {
							decSeconds();
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

		ansImageView1.setImage(ansBack1);
		ansImageView2.setImage(ansBack2);
		ansImageView3.setImage(ansBack1);
		ansImageView4.setImage(ansBack2);

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
					chooseAnswer(0);
					setButtonConfirm(ansImageView1, ansBtn1, state.getCurrentQuestion().getAnswersList().get(0).isCorrect());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			});
		}

		if (state.getCurrentQuestion().getAnswersList().size() > 1) {
			ans2Label.setText(state.getCurrentQuestion().getAnswersList().get(1).getAnswer());
			ansBtn2.setOnMouseClicked(e -> {
				try {
					chooseAnswer(1);
					setButtonConfirm(ansImageView2, ansBtn2, state.getCurrentQuestion().getAnswersList().get(1).isCorrect());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			});
		}

		if (state.getCurrentQuestion().getAnswersList().size() > 2) {
			ans3Label.setText(state.getCurrentQuestion().getAnswersList().get(2).getAnswer());
			ansBtn3.setOnMouseClicked(e -> {
				try {
					chooseAnswer(2);
					setButtonConfirm(ansImageView3, ansBtn3, state.getCurrentQuestion().getAnswersList().get(2).isCorrect());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			});
		}

		if (state.getCurrentQuestion().getAnswersList().size() > 3) {
			ans4Label.setText(state.getCurrentQuestion().getAnswersList().get(3).getAnswer());
			ansBtn4.setOnMouseClicked(e -> {
				try {
					chooseAnswer(3);
					setButtonConfirm(ansImageView4, ansBtn4, state.getCurrentQuestion().getAnswersList().get(3).isCorrect());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			});
		}
	}

	private void setButtonConfirm (ImageView iv, JFXButton btn, boolean ans) {
		if (ans) {
			iv.setImage(ansGreenTri);
			btn.setGraphic(ansGreenView);
		}
		else {
			iv.setImage(ansRedTri);
			btn.setGraphic(ansRedView);
		}
	}

	private void chooseAnswer(int n) throws Exception {
		popUp();
		timerLabel.setText("WAITING");
		ansBtn1.setOnMouseClicked(null);
		ansBtn2.setOnMouseClicked(null);
		ansBtn3.setOnMouseClicked(null);
		ansBtn4.setOnMouseClicked(null);
		timer.cancel();
		timer.purge();
		controller.selectAnswer(n);
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
		popup = new JFXPopup();
		anchorPane = new AnchorPane();
		Image loading = new Image("resources/loading.gif");
		ImageView viewLoading = new ImageView(loading);
		Label words = new Label("Waiting for Others");

		gameAnchor.getStylesheets().add("theme.css");
		anchorPane.getStyleClass().add("anchorpane-Pop");
		words.getStyleClass().add("label-players");

		viewLoading.setFitHeight(130);
		viewLoading.setFitWidth(300);

		anchorPane.setMinSize(300, 300);
		anchorPane.setMaxSize(300, 300);

		words.setMaxWidth(Double.MAX_VALUE);
		words.setAlignment(Pos.CENTER);

		AnchorPane.setTopAnchor(words, 50.0);
		AnchorPane.setTopAnchor(viewLoading, 150.0);
		AnchorPane.setLeftAnchor(words, 40.0);

		anchorPane.getChildren().add(words);
		anchorPane.getChildren().add(viewLoading);
		popup.setPopupContent(anchorPane);
		popup.show(gameAnchor, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, 145.0, 40.0);

	}



}
