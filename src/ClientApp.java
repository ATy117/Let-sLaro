import java.util.Scanner;

public class ClientApp {

	public static void main(String[] args) throws Exception{

		System.out.print("Enter server ip: ");
		Scanner sc = new Scanner (System.in);
		String hostname = sc.nextLine();
		ClientController controller = new ClientController(hostname);
	}
}
