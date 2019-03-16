import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class LoginView {
    Stage primaryStage;
    Controller cotroller;
    StageManager stageManager;

    public LoginView(Stage primaryStage, Controller controller){
        this.primaryStage = primaryStage;
        this.cotroller = controller;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginTemplate.fxml"));
        loader.setController(this);

        stageManager = new StageManager(primaryStage);
        stageManager.loadScene(loader);
    }

}
