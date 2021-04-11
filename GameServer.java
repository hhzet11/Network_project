
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//792 591
public class GameServer {

	// Person���� ������ ������ Set (���Ͽ��� �о�� ��. ��� �о���� ������ ready�� �ȴ�.)
	public static Set<Person> players = new HashSet<>();
	// ������ �̸�.
	public static String fileName = "person.txt";
	// id�� ������ ������ Set (���Ͽ��� �о�� ��. ��� �о���� ������ ready�� �ȴ�.)
	public static Set<String> ids = new HashSet<>();

	// ä�ÿ��� ����� ids
	public static Set<String> ids_chat = new HashSet<>();

	// speaker�� person_id�� ���� ��.(key : print writer / value : name)
	public static HashMap<PrintWriter, String> writers = new HashMap<PrintWriter, String>();

	// ������ �κ�
	//
	//
	//
	// ���� ������
	public static NumberBaseBall manager = new NumberBaseBall();

	// ���ӿ� ���� num(answer)
	public static int num = manager.makeNewNum();
	
	// ready�� ���� ����� ��.(2���Ǹ� ���ӽ���.)
	public static int ready = 0;
	//
	//
	//
	//

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		// check
		System.out.println("The Game server is running...");
		// ���Ͽ� �����ؼ� �о�� stream ����.
		ObjectInputStream inputStream = null;

		// �ӽ÷� Person�ֵ� ���ֱ�
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(GameServer.fileName)));
		out.close();
		// Person p1 = new Person("a333","ida333","pwa333",0,0);
		// out.writeObject(p1);
		// Person p2= new Person("b333","idb333","pwb333",0,0);
		// out.writeObject(p2);
		// Person p3 = new Person("c333","idc333","pwc333",0,0);
		// out.writeObject(p3);
		// players.add(p1);
		// players.add(p2);
		// players.add(p3);

		try {
			// inputStream ����.
			inputStream = new ObjectInputStream(new FileInputStream(new File(GameServer.fileName)));
		} catch (EOFException e) {
			// nothing
		}

		// Person �о �ӽ� ������ ��ü ����.
		Person reader = null;
		try {
			// ��� Person �о����.(���� ���� �� ����)
			while ((reader = (Person) inputStream.readObject()) != null) {
				// players�� Person ��ü ���� �߰�.
				players.add(reader);
				// ids�� id�� �߰�.
				ids.add(reader.getId());
				// ids_chat�� id�� �߰�.
				ids_chat.add(reader.getId());

				// check
				// System.out.println(reader.getId() + " " + reader.getName() + " " +
				// reader.getPw());
			}
		} catch (Exception e) {
			// nothing to do
		}

		// check
		// for (Person p : players) {
		// System.out.println(p.getId() + " " + p.getName() + " " + p.getPw());
		// }

		// ready
		System.out.println("Server Ready!");

		// thread to command
		new Thread(new Runnable() {
			public void run() {
				Scanner sc = new Scanner(System.in);
				String str;
				while (true) {
					str = sc.nextLine();
					// help
					if (str.equalsIgnoreCase("/help") || str.equalsIgnoreCase("/?")) {
						System.out.println("========================");
						System.out.println("/show : show all people");
						System.out.println("/stop : stop server & save information");
						System.out.println("========================");
					}

					// ����Ǿ��ִ� person�� �����ֱ�
					if (str.equalsIgnoreCase("/show")) {
						System.out.println("============ONLINE============");
						for (String p : ids) {
							System.out.println("Person Id: " + p);
						}
						System.out.println("============INCHAT============");
						for (String p : ids_chat) {
							System.out.println("Person Id: " + p);
						}
						System.out.println("==============================");

					}
					// �����ϰ� �����ϱ�
					if (str.equalsIgnoreCase("/stop")) {
						ObjectOutputStream outputStream = null;
						try {
							// ���Ͽ� ������ outputStream ����.
							outputStream = new ObjectOutputStream(new FileOutputStream(new File(GameServer.fileName)));
							// players�� ����� ��� Person ���� ����.
							for (Person p : players) {
								outputStream.writeObject(p);
							}
							// �Ϸ� Ȯ��.
							System.out.println("========================");
							System.out.println("SAVE COMPLETE.");
							System.out.println("========================");
							// ����.
							System.exit(0);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							try {
								// stream close.
								outputStream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}

			}
		}).start();

		// Thread 500�� ����.
		ExecutorService pool = Executors.newFixedThreadPool(500);// thread 500�� ����
		// serverSocket ����(port number : 59001)
		try (ServerSocket listener = new ServerSocket(59001)) {
			// ��� �޴´�.
			while (true) {
				// client���� ���� ��û�� ����,
				pool.execute(new Handler(listener.accept())); // �������� ���ϼ������ֱ� with thread
			}

		}

	} // main close.

	private static class Handler implements Runnable {
		private Person newUser; // client�� id�� ������ String
		
		private String i_d;
		
		private Socket socket; // socket
		private Scanner in; // text receiver from Client
		private PrintWriter out; // text sender to Client
		private Person user;

		/**
		 * Constructs a handler thread, squirreling away the socket. All the interesting
		 * work is done in the run method. Remember the constructor is called from the
		 * server's main method, so this has to be as short as possible.
		 */
		// constructor
		public Handler(Socket socket) {
			this.socket = socket;
		}

		/**
		 * Services this thread's client by repeatedly requesting a screen name until a
		 * unique one has been submitted, then acknowledges the name and registers the
		 * output stream for the client in a global set, then repeatedly gets inputs and
		 * broadcasts them.
		 */
		public synchronized void run() {
			try { // login ���� �� �����.

				in = new Scanner(socket.getInputStream()); // Client���� ���� stream
				out = new PrintWriter(socket.getOutputStream(), true); // Client���� ���� stream
				user = new Person("", "", "", 0, 0);

				// check
				System.out.println("Connected: " + socket);

				// ���� �޼����� �ִٸ�,
				while (in.hasNext()) {
					// ���� �޾ƿ���
					String str = in.nextLine();
					// ����غ��ô�.(check)
					// System.out.println(str);

					// ���� ���� '&'�� ������
					String[] splitMessage = str.split("&");

					// check
					for (int i = 0; i < splitMessage.length; i++) {
						System.out.println("ACK: " + splitMessage[i]);
					}

					// �������� ���ٴ� ��û�� ����,
					if (splitMessage[0].toLowerCase().startsWith("gotogame")) {
						System.out.println("ACK: " + splitMessage[1]);
						// ids_chat���� �� ���� ���ְ� (�ϸ� �ȵ�)
						ids_chat.remove(splitMessage[1]);
						// ��� Ŭ���̾�Ʈ�鿡��
						for (PrintWriter writer : writers.keySet()) {
							// �� ������! �˷��ֱ�
							writer.println("GOTOGAME&" + splitMessage[1]);
						}
						System.out.println(splitMessage[1] + "is gone into game!");
					}

					if (splitMessage[0].equalsIgnoreCase("chat")) {
						// �׳� enter ����.
						if (splitMessage.length < 2)
							continue;
						// �� ��° �޼����� start���,
						if (splitMessage[1].toLowerCase().startsWith("start")) {

							// Person ��ü ����.
							newUser = new Person(splitMessage[2], splitMessage[3], splitMessage[4],
									Integer.parseInt(splitMessage[5]), Integer.parseInt(splitMessage[6]));

							// user������ ���⿡ �߰��ϱ�.
							ids.add(newUser.getId());
							ids_chat.add(newUser.getId());
							players.add(newUser);

							// check
							System.out.println("ID : " + newUser.getId() + " NAME : " + newUser.getName() + " PW : "
									+ newUser.getPw());

							// ä�þȿ� �ִ»���鿡�� �� ������ ��ư �߰����ֱ�.
							//for (String id : ids_chat) {
							//	//(�ڱ� �ڽ��� ����!
							//	if (id.contentEquals(newUser.getId()))
							//		continue;
							//	out.println("SETPLAYER " + newUser.getId());
							//}
							
							//"��"��ư �߰��ϵ��� ���ֱ�
							for(String id : ids_chat) {
								if(id.contains(newUser.getId())) continue;
								out.println("SETPLAYER " + id);
							}
							
							//"����"��ư �߰��ϵ��� ���ֱ�
							for (PrintWriter writer : writers.keySet()) {
								String ID = writers.get(writer);
								if (!ids_chat.contains(ID)) { // ***ids_chat�ȿ� �� writer�� ���� ����� �ִٸ�, ***
									continue;
								}
								writer.println("MESSAGE " + newUser.getId() + " has joined");
								writer.println("SETPLAYER " + newUser.getId());
							}
							// writers hash map�� �� client�� writer������ �־��ش�.
							writers.put(out, newUser.getId());
							// check
							System.out.println("�߰� ��!");
						}

						else if (splitMessage[1].toLowerCase().startsWith("getreco")) {
							// input�� " "������ �ɰ���.
							String[] st = splitMessage[1].split(" ");
							// whisper�ϰ��� �ϴ� ����� nickname�� s_id�� �����Ѵ�.
							String s_id = st[1];
							// check message
							System.out.println(newUser.getId() + " read " + s_id + "'s records");

							for (Person r_user : players) {
								if (s_id.contentEquals(r_user.getId())) {
									String records = "READRECO " + r_user.getId() + " " + r_user.getWin() + " "
											+ r_user.getLose();
									out.println(records);
									break;
								}
							}
						}

						else if (splitMessage[1].toLowerCase().startsWith("rqgame")) {
							// input�� ���⸦ �������� split�Ѵ�.
							String st[] = splitMessage[1].split(" ");
							// ������ nickname�� s_id�� �����Ѵ�.
							String s_id = st[1];
							// check message
							System.out.println(newUser.getId() + " requests game to " + s_id);

							for (PrintWriter writer : writers.keySet()) {
								if (s_id.contentEquals(writers.get(writer))) {
									String records = "REQUESTGAME " + newUser.getId();
									writer.println(records);
									break;
								}
							}
						}

						else if (splitMessage[1].toLowerCase().startsWith("replyto")) {
							// input�� ���⸦ �������� split�Ѵ�.
							String st[] = splitMessage[1].split(" ");
							// ������ nickname�� s_id�� �����Ѵ�.
							String s_id = st[1];

							for (PrintWriter writer : writers.keySet()) {
								if (s_id.contentEquals(writers.get(writer))) {
									if (st[2].equalsIgnoreCase("yes")) {
										String records = "REPLY " + newUser.getId() + " yes";
										writer.println(records);
									} else {
										String records = "REPLY " + newUser.getId() + " no";
										writer.println(records);
									}
									break;
								}
							}
						}

						// logout�̸�,
						else if (splitMessage[1].toLowerCase().startsWith("/logout")) {
							return;
						}

						// whisper mode
						else if (splitMessage[1].toLowerCase().startsWith("whisper")) {
							String st[] = splitMessage[1].split(" "); // input�� ���⸦ �������� split�Ѵ�.
							String s_id = st[2]; // whisper�ϰ��� �ϴ� ����� nickname�� s_id�� �����Ѵ�.
							System.out.println(newUser.getId() + " whisper to " + s_id); // check message
							// ���� server�� ���� �� user���� �̸� �� s_id�� ��ġ�ϴ� �̸��� ���ٸ� �� ������ ������ �˷��ش�.
							if (!ids.contains(s_id)) {
								out.println("MESSAGE " + s_id + " is not in this server.");
							} else {
								// writers hash map���� value�� ������ ����� �̸�, s_id�� ���� ������� client���� whisper message��
								// ������.
								for (PrintWriter writer : writers.keySet()) {
									if (s_id.contentEquals(writers.get(writer))
											|| newUser.getId().contentEquals(writers.get(writer))) {
										String p_string = "WHISPER " + newUser.getId() + "(whisper to " + s_id + ") :"; // client����
										for (int i = 4; i < st.length; i++) { // split�� message�� �ϳ��� string���� �����Ѵ�.
											p_string = p_string + " " + st[i];
										}
										writer.println(p_string); // �ش� client���� ������.
									} // close if
								} // close for
							} // close else
						} // close if

						else {
							for (PrintWriter writer : writers.keySet()) {
								writer.println("MESSAGE " + newUser.getId() + ": " + splitMessage[1]);
							} // close for
						} // close else
					}

					// game���� �����ϴ� �����̶��,(���Ӱ���)
					else if (splitMessage[0].equalsIgnoreCase("game")) {

						// ready���!
						if (splitMessage[3].equalsIgnoreCase("ready")) {
							// ready ��Ų�� �����ֱ�.
							for (PrintWriter writer : writers.keySet()) { // receiver�� ���� writerã��,
								if (splitMessage[2].contentEquals(writers.get(writer))) {
									writer.println("READY&" + splitMessage[1]);
									ready++;
									System.out.println("READY = " + ready);
									break;
								}
							}
							// �� �� ready���� Ȯ��.
							if (ready == 2) {
								ready = 0; // init
								// ���� ����! �϶�� �ѿ��� ���!
								System.out.println("Game Start!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
								// �ѿ� ���� writerã�� �����ֱ�. GAMESTART & id1 & id2 (id2 & id1) &
								for (PrintWriter writer : writers.keySet()) {
									// receiver ã��.
									if (splitMessage[1].contentEquals(writers.get(writer))) {
										writer.println(
												"GAMESTART&" + splitMessage[2] + "&" + splitMessage[1] + "&" + num);

									}
									// sender ã��.
									if (splitMessage[2].contentEquals(writers.get(writer))) {
										writer.println(
												"GAMESTART&" + splitMessage[1] + "&" + splitMessage[2] + "&" + num);
									}

								}
								// num �ʱ�ȭ.
								num = manager.makeNewNum();
							}
						}

						// ready Ǭ�Ŷ��!
						else if (splitMessage[3].equalsIgnoreCase("cancel")) {
							// ready Ǯ���� �����ֱ�.
							for (PrintWriter writer : writers.keySet()) { // receiver�� ���� writerã��,
								if (splitMessage[2].contentEquals(writers.get(writer))) {
									writer.println("CANCEL&" + splitMessage[1]);
									ready--;
									break;
								}
							}
						}

						// Game & [sender] & [receiver] & Message & contents
						else if (splitMessage[3].equalsIgnoreCase("message")) {
							// splitMessage[4] (�޼��� ����)�� id2���� �����ֱ�.
							for (PrintWriter writer : writers.keySet()) {
								if (splitMessage[2].contentEquals(writers.get(writer))) {
									// Message & [sender] & content
									writer.println("MESSAGE&" + splitMessage[1] + "&" + splitMessage[4]);
									System.out.println("SENT!");
									break;
								}
							}
						}

						// Game & [sender] & [receiver] & showInfo
						else if (splitMessage[3].equalsIgnoreCase("showinfo")) {
							// sender���� ������� �����ֱ�.
							for (Person r_user : players) {
								if (splitMessage[2].contentEquals(r_user.getId())) {
									String records = "READRECO&" + r_user.getId() + "&" + r_user.getWin() + "&"
											+ r_user.getLose();

									for (PrintWriter writer : writers.keySet()) {
										if (splitMessage[1].contentEquals(writers.get(writer))) {
											// Message & [sender] & content
											writer.println(records);
											System.out.println("SENT!");
											break;
										}
									}
									break;
								}
							}
						}
					} // if(game) close.

					// Gaming���� �����ϴ� �����̶��,
					else if (splitMessage[0].equalsIgnoreCase("gaming")) {
						// Message ���,
						if (splitMessage[3].equalsIgnoreCase("message")) {
							// splitMessage[4] (�޼��� ����)�� id2���� �����ֱ�.
							for (PrintWriter writer : writers.keySet()) {
								if (splitMessage[1].contentEquals(writers.get(writer))) {
									// Message & [sender] & content
									writer.println("MESSAGE&" + splitMessage[1] + "&" + splitMessage[4]);
									System.out.println("SENT!");
									break;
								}
							}
						}
						// GameMessage ���,
						else if (splitMessage[3].equalsIgnoreCase("gamingmessage")) {

							// splitMessage[4] (�޼��� ����)�� id2���� �����ֱ�.
							for (PrintWriter writer : writers.keySet()) {
								// ???????? �� [sender]���� ������ ����� ���°���;
								if (splitMessage[1].contentEquals(writers.get(writer))) {
									// Message & [sender] & content
									// Gaming & [receiver] & [sender] & GamingMessage & Numbers & result
									System.out.println("[receiver]: " + splitMessage[1]);
									// GAMINGMESSAGE & [receiver] & Numbers & result
									writer.println("GAMINGMESSAGE&" + splitMessage[1] + "&" + splitMessage[4] + "&"
											+ splitMessage[5]);
									System.out.println("SENT!");
									System.out.println("NUMS: " + num);
									break;
								}
							}
						}
						// Gaming ��� ���Ÿ޼������,
						// Gaming & [sender] & GameResult & [win/lose]
						else if (splitMessage[2].equalsIgnoreCase("gameresult")) {
							// ���̶��,
							if (splitMessage[3].equalsIgnoreCase("win")) {
								// [sender]�� win �ϳ� �÷��ֱ�.
								for (Person p : players) {
									// players�� �ִ� ID�� ���� ���� ID�� ���ٸ�,
									if (p.getId().equals(splitMessage[1])) {
										// win �ϳ� ����.
										p.setWin(p.getWin() + 1);
									}
								}
							}
							// �ж��,
							else if (splitMessage[3].equalsIgnoreCase("lose")) {
								// [sender]�� lose �ϳ� �÷��ֱ�.
								for (Person p : players) {
									// players�� �ִ� ID�� ���� ���� ID�� ���ٸ�,
									if (p.getId().equals(splitMessage[1])) {
										// lose�ϳ� ����.
										p.setLose(p.getLose() + 1);
									}
								}
							}

							// check
							System.out.println("���� ����Ϸ�!");
						}
					}

				} // close while

				// stream �������� ������ �����,
			} catch (Exception e) {
				// nothing to do
			} finally {
				// ���� out�� null�� �ƴ϶�� 'writers' hash set���� client�� out�� �����.
				if (out != null) {
					writers.remove(out);
				}
				// ���� id�� null�� �ƴ϶�� �ش� client�� �����ٰ� ����ϰ� 'ids' hash set���� client�� �̸��� �����.
				if (newUser.getId() != null) {
					System.out.println(newUser.getId() + " is leaving");
					ids.remove(newUser.getId());
					ids_chat.remove(newUser.getId());
					// �������� client�鿡�� �� client�� �����ٴ� �޼����� ������.
					for (PrintWriter writer : writers.keySet()) {
						writer.println("MESSAGE " + newUser.getId() + " has left");
						writer.println("OUTPLAYER " + newUser.getId());
					}
				} // close if
				try {
					socket.close(); // socket�� �ݾ� �ش� client���� ������ �����Ѵ�.
				} catch (IOException e) {
				} // ������ �߻��Ѵٸ� catch�Ѵ�.
			} // close finally
		}// close run

	}
}