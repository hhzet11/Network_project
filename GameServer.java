
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

	// Person들의 정보를 저장할 Set (파일에서 읽어올 것. 모두 읽어오고 서버는 ready가 된다.)
	public static Set<Person> players = new HashSet<>();
	// 파일의 이름.
	public static String fileName = "person.txt";
	// id의 정보를 저장할 Set (파일에서 읽어올 것. 모두 읽어오고 서버는 ready가 된다.)
	public static Set<String> ids = new HashSet<>();

	// 채팅에서 사용할 ids
	public static Set<String> ids_chat = new HashSet<>();

	// speaker와 person_id를 묶어 줌.(key : print writer / value : name)
	public static HashMap<PrintWriter, String> writers = new HashMap<PrintWriter, String>();

	// 현식이 부분
	//
	//
	//
	// 게임 관리자
	public static NumberBaseBall manager = new NumberBaseBall();

	// 게임에 사용될 num(answer)
	public static int num = manager.makeNewNum();
	
	// ready를 누른 사람의 수.(2가되면 게임시작.)
	public static int ready = 0;
	//
	//
	//
	//

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		// check
		System.out.println("The Game server is running...");
		// 파일에 접근해서 읽어올 stream 생성.
		ObjectInputStream inputStream = null;

		// 임시로 Person애들 써주기
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
			// inputStream 생성.
			inputStream = new ObjectInputStream(new FileInputStream(new File(GameServer.fileName)));
		} catch (EOFException e) {
			// nothing
		}

		// Person 읽어서 임시 저장할 객체 생성.
		Person reader = null;
		try {
			// 모든 Person 읽어오기.(값이 없을 때 까지)
			while ((reader = (Person) inputStream.readObject()) != null) {
				// players에 Person 전체 정보 추가.
				players.add(reader);
				// ids에 id만 추가.
				ids.add(reader.getId());
				// ids_chat에 id만 추가.
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

					// 저장되어있는 person들 보여주기
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
					// 저장하고 종료하기
					if (str.equalsIgnoreCase("/stop")) {
						ObjectOutputStream outputStream = null;
						try {
							// 파일에 저장할 outputStream 생성.
							outputStream = new ObjectOutputStream(new FileOutputStream(new File(GameServer.fileName)));
							// players에 저장된 모든 Person 정보 저장.
							for (Person p : players) {
								outputStream.writeObject(p);
							}
							// 완료 확인.
							System.out.println("========================");
							System.out.println("SAVE COMPLETE.");
							System.out.println("========================");
							// 종료.
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

		// Thread 500개 생성.
		ExecutorService pool = Executors.newFixedThreadPool(500);// thread 500개 생성
		// serverSocket 생성(port number : 59001)
		try (ServerSocket listener = new ServerSocket(59001)) {
			// 계속 받는다.
			while (true) {
				// client에서 연결 요청이 오면,
				pool.execute(new Handler(listener.accept())); // 서버마다 소켓설정해주기 with thread
			}

		}

	} // main close.

	private static class Handler implements Runnable {
		private Person newUser; // client의 id를 저장할 String
		
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
			try { // login 받을 것 만들기.

				in = new Scanner(socket.getInputStream()); // Client에게 받을 stream
				out = new PrintWriter(socket.getOutputStream(), true); // Client에게 보낼 stream
				user = new Person("", "", "", 0, 0);

				// check
				System.out.println("Connected: " + socket);

				// 받은 메세지가 있다면,
				while (in.hasNext()) {
					// 문장 받아오기
					String str = in.nextLine();
					// 출력해봅시다.(check)
					// System.out.println(str);

					// 받은 문장 '&'로 나누기
					String[] splitMessage = str.split("&");

					// check
					for (int i = 0; i < splitMessage.length; i++) {
						System.out.println("ACK: " + splitMessage[i]);
					}

					// 게임으로 간다는 요청이 오면,
					if (splitMessage[0].toLowerCase().startsWith("gotogame")) {
						System.out.println("ACK: " + splitMessage[1]);
						// ids_chat에서 그 아이 빼주고 (하면 안돼)
						ids_chat.remove(splitMessage[1]);
						// 모든 클라이언트들에게
						for (PrintWriter writer : writers.keySet()) {
							// 얘 빠졌어! 알려주기
							writer.println("GOTOGAME&" + splitMessage[1]);
						}
						System.out.println(splitMessage[1] + "is gone into game!");
					}

					if (splitMessage[0].equalsIgnoreCase("chat")) {
						// 그냥 enter 방지.
						if (splitMessage.length < 2)
							continue;
						// 두 번째 메세지가 start라면,
						if (splitMessage[1].toLowerCase().startsWith("start")) {

							// Person 객체 생성.
							newUser = new Person(splitMessage[2], splitMessage[3], splitMessage[4],
									Integer.parseInt(splitMessage[5]), Integer.parseInt(splitMessage[6]));

							// user받은거 여기에 추가하기.
							ids.add(newUser.getId());
							ids_chat.add(newUser.getId());
							players.add(newUser);

							// check
							System.out.println("ID : " + newUser.getId() + " NAME : " + newUser.getName() + " PW : "
									+ newUser.getPw());

							// 채팅안에 있는사람들에게 이 아이의 버튼 추가해주기.
							//for (String id : ids_chat) {
							//	//(자기 자신은 제외!
							//	if (id.contentEquals(newUser.getId()))
							//		continue;
							//	out.println("SETPLAYER " + newUser.getId());
							//}
							
							//"나"버튼 추가하도록 해주기
							for(String id : ids_chat) {
								if(id.contains(newUser.getId())) continue;
								out.println("SETPLAYER " + id);
							}
							
							//"남들"버튼 추가하도록 해주기
							for (PrintWriter writer : writers.keySet()) {
								String ID = writers.get(writer);
								if (!ids_chat.contains(ID)) { // ***ids_chat안에 이 writer를 쓰는 사람이 있다면, ***
									continue;
								}
								writer.println("MESSAGE " + newUser.getId() + " has joined");
								writer.println("SETPLAYER " + newUser.getId());
							}
							// writers hash map에 이 client의 writer정보를 넣어준다.
							writers.put(out, newUser.getId());
							// check
							System.out.println("추가 끝!");
						}

						else if (splitMessage[1].toLowerCase().startsWith("getreco")) {
							// input을 " "단위로 쪼갠다.
							String[] st = splitMessage[1].split(" ");
							// whisper하고자 하는 대상의 nickname을 s_id에 저장한다.
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
							// input을 띄어쓰기를 기준으로 split한다.
							String st[] = splitMessage[1].split(" ");
							// 상대방의 nickname을 s_id에 저장한다.
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
							// input을 띄어쓰기를 기준으로 split한다.
							String st[] = splitMessage[1].split(" ");
							// 상대방의 nickname을 s_id에 저장한다.
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

						// logout이면,
						else if (splitMessage[1].toLowerCase().startsWith("/logout")) {
							return;
						}

						// whisper mode
						else if (splitMessage[1].toLowerCase().startsWith("whisper")) {
							String st[] = splitMessage[1].split(" "); // input을 띄어쓰기를 기준으로 split한다.
							String s_id = st[2]; // whisper하고자 하는 대상의 nickname을 s_id에 저장한다.
							System.out.println(newUser.getId() + " whisper to " + s_id); // check message
							// 만약 server에 연결 된 user들의 이름 중 s_id과 일치하는 이름이 없다면 그 유저가 없음을 알려준다.
							if (!ids.contains(s_id)) {
								out.println("MESSAGE " + s_id + " is not in this server.");
							} else {
								// writers hash map에서 value가 보내는 사람의 이름, s_id과 같은 사용자의 client에게 whisper message를
								// 보낸다.
								for (PrintWriter writer : writers.keySet()) {
									if (s_id.contentEquals(writers.get(writer))
											|| newUser.getId().contentEquals(writers.get(writer))) {
										String p_string = "WHISPER " + newUser.getId() + "(whisper to " + s_id + ") :"; // client에게
										for (int i = 4; i < st.length; i++) { // split한 message를 하나의 string으로 저장한다.
											p_string = p_string + " " + st[i];
										}
										writer.println(p_string); // 해당 client에게 보낸다.
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

					// game으로 시작하는 문장이라면,(게임관련)
					else if (splitMessage[0].equalsIgnoreCase("game")) {

						// ready라면!
						if (splitMessage[3].equalsIgnoreCase("ready")) {
							// ready 시킨거 보여주기.
							for (PrintWriter writer : writers.keySet()) { // receiver에 대한 writer찾고,
								if (splitMessage[2].contentEquals(writers.get(writer))) {
									writer.println("READY&" + splitMessage[1]);
									ready++;
									System.out.println("READY = " + ready);
									break;
								}
							}
							// 둘 다 ready인지 확인.
							if (ready == 2) {
								ready = 0; // init
								// 게임 시작! 하라고 둘에게 명령!
								System.out.println("Game Start!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
								// 둘에 대한 writer찾고 보내주기. GAMESTART & id1 & id2 (id2 & id1) &
								for (PrintWriter writer : writers.keySet()) {
									// receiver 찾기.
									if (splitMessage[1].contentEquals(writers.get(writer))) {
										writer.println(
												"GAMESTART&" + splitMessage[2] + "&" + splitMessage[1] + "&" + num);

									}
									// sender 찾기.
									if (splitMessage[2].contentEquals(writers.get(writer))) {
										writer.println(
												"GAMESTART&" + splitMessage[1] + "&" + splitMessage[2] + "&" + num);
									}

								}
								// num 초기화.
								num = manager.makeNewNum();
							}
						}

						// ready 푼거라면!
						else if (splitMessage[3].equalsIgnoreCase("cancel")) {
							// ready 풀린거 보여주기.
							for (PrintWriter writer : writers.keySet()) { // receiver에 대한 writer찾고,
								if (splitMessage[2].contentEquals(writers.get(writer))) {
									writer.println("CANCEL&" + splitMessage[1]);
									ready--;
									break;
								}
							}
						}

						// Game & [sender] & [receiver] & Message & contents
						else if (splitMessage[3].equalsIgnoreCase("message")) {
							// splitMessage[4] (메세지 내용)을 id2에게 보내주기.
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
							// sender에게 상대정보 보내주기.
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

					// Gaming으로 시작하는 문장이라면,
					else if (splitMessage[0].equalsIgnoreCase("gaming")) {
						// Message 라면,
						if (splitMessage[3].equalsIgnoreCase("message")) {
							// splitMessage[4] (메세지 내용)을 id2에게 보내주기.
							for (PrintWriter writer : writers.keySet()) {
								if (splitMessage[1].contentEquals(writers.get(writer))) {
									// Message & [sender] & content
									writer.println("MESSAGE&" + splitMessage[1] + "&" + splitMessage[4]);
									System.out.println("SENT!");
									break;
								}
							}
						}
						// GameMessage 라면,
						else if (splitMessage[3].equalsIgnoreCase("gamingmessage")) {

							// splitMessage[4] (메세지 내용)을 id2에게 보내주기.
							for (PrintWriter writer : writers.keySet()) {
								// ???????? 왜 [sender]한테 보내야 제대로 가는거지;
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
						// Gaming 결과 갱신메세지라면,
						// Gaming & [sender] & GameResult & [win/lose]
						else if (splitMessage[2].equalsIgnoreCase("gameresult")) {
							// 승이라면,
							if (splitMessage[3].equalsIgnoreCase("win")) {
								// [sender]의 win 하나 올려주기.
								for (Person p : players) {
									// players에 있는 ID와 지금 받은 ID가 같다면,
									if (p.getId().equals(splitMessage[1])) {
										// win 하나 증가.
										p.setWin(p.getWin() + 1);
									}
								}
							}
							// 패라면,
							else if (splitMessage[3].equalsIgnoreCase("lose")) {
								// [sender]의 lose 하나 올려주기.
								for (Person p : players) {
									// players에 있는 ID와 지금 받은 ID가 같다면,
									if (p.getId().equals(splitMessage[1])) {
										// lose하나 증가.
										p.setLose(p.getLose() + 1);
									}
								}
							}

							// check
							System.out.println("승패 적용완료!");
						}
					}

				} // close while

				// stream 생성에서 문제가 생기면,
			} catch (Exception e) {
				// nothing to do
			} finally {
				// 만약 out이 null이 아니라면 'writers' hash set에서 client의 out을 지운다.
				if (out != null) {
					writers.remove(out);
				}
				// 만약 id이 null이 아니라면 해당 client가 떠났다고 출력하고 'ids' hash set에서 client의 이름을 지운다.
				if (newUser.getId() != null) {
					System.out.println(newUser.getId() + " is leaving");
					ids.remove(newUser.getId());
					ids_chat.remove(newUser.getId());
					// 실행중인 client들에게 이 client가 떠났다는 메세지를 보낸다.
					for (PrintWriter writer : writers.keySet()) {
						writer.println("MESSAGE " + newUser.getId() + " has left");
						writer.println("OUTPLAYER " + newUser.getId());
					}
				} // close if
				try {
					socket.close(); // socket을 닫아 해당 client와의 연결을 종료한다.
				} catch (IOException e) {
				} // 오류가 발생한다면 catch한다.
			} // close finally
		}// close run

	}
}