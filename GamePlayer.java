import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JFrame;

public class GamePlayer {
	Scanner in;
	PrintWriter out;
	static Person user;

	public static void main(String args[]) throws IOException {
		user = new Person("2", "dbwhdans2", "ab6ix", 75, 0);
		GamePlayer player1 = new GamePlayer();
		player1.run();
	}
	
	public void run() throws IOException {
		WaitWindowView waitRoom = null;
		try {
			Socket socket = new Socket("localhost", 59001);
			in = new Scanner(socket.getInputStream());
			out = new PrintWriter(socket.getOutputStream(), true);

			waitRoom = new WaitWindowView(user, out, in);
			waitRoom.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			waitRoom.run();

		} finally { // ä���� �����ϰ��� �Ѵٸ�
			waitRoom.frame.setVisible(false); // frameâ�� �ݴ´�.
			waitRoom.frame.dispose(); // �� client�� frame�� �����Ѵ�.
		} // close finally
	}

}