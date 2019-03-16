import javafx.stage.Stage;

public class Controller {

    public Controller (Stage primaryStage){
        LoginView login = new LoginView(primaryStage, this);
    }
}
