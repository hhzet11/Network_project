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

		} finally { // 채팅을 종료하고자 한다면
			waitRoom.frame.setVisible(false); // frame창을 닫는다.
			waitRoom.frame.dispose(); // 이 client의 frame만 종료한다.
		} // close finally
	}

}