import java.util.Scanner;

public class LobbyView extends View {

	public LobbyView(ClientController controller) throws Exception{
		super(controller);

		System.out.print("Enter a username: ");
		Scanner sc = new Scanner (System.in);
		String username = sc.nextLine();
		username = username.trim();

		controller.submitUsername(username);
	}



	@Override
	public void Update() {

	}
}
