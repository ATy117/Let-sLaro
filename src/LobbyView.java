
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Scanner;

public class LobbyView extends View {
	@FXML JFXButton enterBtn;
	@FXML JFXTextField usernameField, IPField;
	@FXML AnchorPane lobbyAnchor;
	@FXML Label enterNameLabel;

	public LobbyView(ClientController controller, Stage primaryStage) throws Exception {
		super(controller);
		this.primaryStage = primaryStage;

		System.out.println("You are in lobby view");

		FXMLLoader loader = new FXMLLoader(getClass().getResource("lobbyTemplate.fxml"));
		loader.setController(this);
		
		StageManager sm = new StageManager(primaryStage);
		sm.loadScene(loader);
		sm.setWindowName("Lobby");

		init();
	}


	public void joinGame (ActionEvent actionEvent) throws Exception {
		//System.out.print("Enter server ip: ");
		//Scanner sc = new Scanner (System.in);
		//String hostname = sc.nextLine();
		String hostname = IPField.getText();

		//System.out.print("Enter a username: ");
		//sc = new Scanner (System.in);
		//String username = sc.nextLine();
		String username = usernameField.getText();

		if (!hostname.isEmpty() && !username.isEmpty()) {
			controller.submitUsername(hostname, username);
		}

	}

	public void init(){
		Image enter = new Image("resources/enter.png");
		ImageView enterView = new ImageView(enter);
		enterView.setFitHeight(35);
		enterView.setFitWidth(85);
		enterBtn.setGraphic(enterView);

		lobbyAnchor.getStylesheets().add("theme.css");
		usernameField.getStyleClass().add("text-field-username");
		IPField.getStyleClass().add("text-field-address");

	}

	public void popUp(){
		JFXPopup popup = new JFXPopup();
		AnchorPane anchorPane = new AnchorPane();
		Image loading = new Image("resources/loading.gif");
		ImageView viewLoading = new ImageView(loading);
		Label words = new Label("Waiting for Others");

		lobbyAnchor.getStylesheets().add("theme.css");
		anchorPane.getStyleClass().add("anchorpane-Pop");
		words.getStyleClass().add("label-players");

		viewLoading.setFitHeight(130);
		viewLoading.setFitWidth(300);

		anchorPane.setMinSize(300,300);
		anchorPane.setMaxSize(300, 300);

		words.setMaxWidth(Double.MAX_VALUE);
		words.setAlignment(Pos.CENTER);

		AnchorPane.setTopAnchor(words, 50.0);
		AnchorPane.setTopAnchor(viewLoading, 150.0);
		AnchorPane.setLeftAnchor(words, 40.0);

		anchorPane.getChildren().add(words);
		anchorPane.getChildren().add(viewLoading);
		popup.setPopupContent(anchorPane);
		popup.show(lobbyAnchor, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, 145.0, 40.0);
	}


	@Override
	public void Update() {

	}
}
