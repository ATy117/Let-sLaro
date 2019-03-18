import java.util.Scanner;

public class ServerApp {

	public static void main (String[] args) throws Exception{
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter player count: ");
		int n = sc.nextInt();
		ServerController controller = new ServerController(n);
	}
}
