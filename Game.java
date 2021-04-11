
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextArea;
import java.awt.Color;

public class Game extends JFrame {
	private String id1 = ""; // 나
	private String id2 = ""; // 상대
	private Scanner in;
	private PrintWriter out;
	private static boolean turn;
	private int answer; // 정답

	private JPanel contentPane;
	private JTextField txtNumberBaseball;
	private JTextField txtGame;
	private JTextField textField;
	private JTextField txtChat;
	private JTextField textField_1;

	/**
	 * Create the frame.
	 */
	public Game(String id1, String id2, Scanner in, PrintWriter out, int answer, boolean turn) {
		this.id1 = id1; // 나
		this.id2 = id2; // 상대
		this.in = in; // inputStream
		this.out = out; // outputStream
		this.answer = answer; // 정답
		Game.turn = turn; // turn
		
		System.out.println("TURN: "+turn);
		
		// 타이틀 설정
		this.setTitle(id1 + "'s Game Room");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 755, 457);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(210, 180, 140));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		txtNumberBaseball = new JTextField();
		txtNumberBaseball.setBackground(new Color(255, 239, 213));
		txtNumberBaseball.setEditable(false);
		txtNumberBaseball.setFont(new Font("Microsoft Tai Le", Font.BOLD, 24));
		txtNumberBaseball.setHorizontalAlignment(SwingConstants.CENTER);
		txtNumberBaseball.setText("Number Baseball Game");
		txtNumberBaseball.setColumns(10);

		JButton btnPlayer = new JButton(id1);
		btnPlayer.setForeground(new Color(245, 222, 179));
		btnPlayer.setBackground(new Color(139, 69, 19));
		btnPlayer.setFont(new Font("Microsoft Tai Le", Font.BOLD, 15));

		JButton btnPlayer_1 = new JButton(id2);
		btnPlayer_1.setForeground(new Color(245, 222, 179));
		btnPlayer_1.setBackground(new Color(139, 69, 19));
		btnPlayer_1.setFont(new Font("Microsoft Tai Le", Font.BOLD, 15));

		JScrollPane scrollPane = new JScrollPane();

		JScrollPane scrollPane_1 = new JScrollPane();

		JButton btnSurrender = new JButton("Surrender");
		btnSurrender.setForeground(new Color(245, 222, 179));
		btnSurrender.setBackground(new Color(139, 69, 19));
		btnSurrender.setFont(new Font("Microsoft Tai Le", Font.BOLD, 15));

		txtGame = new JTextField();
		txtGame.setForeground(new Color(139, 69, 19));
		txtGame.setBackground(new Color(245, 222, 179));
		txtGame.setHorizontalAlignment(SwingConstants.CENTER);
		txtGame.setEditable(false);
		txtGame.setFont(new Font("Microsoft Tai Le", Font.BOLD, 15));
		txtGame.setText("Game : ");
		txtGame.setColumns(10);

		textField = new JTextField();
		textField.setColumns(10);

		txtChat = new JTextField();
		txtChat.setForeground(new Color(139, 69, 19));
		txtChat.setBackground(new Color(245, 222, 179));
		txtChat.setHorizontalAlignment(SwingConstants.CENTER);
		txtChat.setEditable(false);
		txtChat.setFont(new Font("Microsoft Tai Le", Font.BOLD, 15));
		txtChat.setText("Chat : ");
		txtChat.setColumns(10);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
						.addComponent(txtNumberBaseball, GroupLayout.DEFAULT_SIZE, 699, Short.MAX_VALUE)
						.addContainerGap())
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup().addGroup(gl_contentPane
						.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup().addGap(16).addComponent(scrollPane,
								GroupLayout.PREFERRED_SIZE, 265, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
								.addComponent(txtGame, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, 196, GroupLayout.PREFERRED_SIZE)))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(btnSurrender, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
								.addComponent(btnPlayer_1, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
								.addComponent(btnPlayer, GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 241, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createSequentialGroup()
										.addComponent(txtChat, GroupLayout.PREFERRED_SIZE, 59,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(textField_1, GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.RELATED)))
						.addGap(13)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup().addContainerGap()
				.addComponent(txtNumberBaseball, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup().addGap(106)
								.addComponent(btnPlayer, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
								.addGap(18)
								.addComponent(btnPlayer_1, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED))
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
								.addPreferredGap(ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
										.addComponent(scrollPane_1, Alignment.TRAILING).addComponent(scrollPane,
												Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE))
								.addGap(9)))
				.addGap(2)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSurrender, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(txtGame, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(txtChat, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addGap(15)));

		JTextArea textArea_1 = new JTextArea();
		textArea_1.setEditable(false);
		scrollPane_1.setViewportView(textArea_1);

		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		contentPane.setLayout(gl_contentPane);

		/* gui 끝 */

		this.setVisible(true);

		// 채팅창 Gaming & [sender] & [receiver] & Message & Contents
		textField_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// enter 방지.
				if (textField_1.getText().equalsIgnoreCase(""))
					return;
				textArea_1.append(id1 + " : " + textField_1.getText() + "\n");
				textArea_1.setCaretPosition(textArea_1.getDocument().getLength());
				out.println("Gaming&" + id1 + "&" + id2 + "&Message&" + textField_1.getText()); // text 입력한 것 Server로 전송
				textField_1.setText("");// text field 빈 문장으로 초기화
			}
		});

		// 게임 채팅창 Gaming & [sender] & [receiver] & GameMessage & Contents
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// enter 방지.
				if (textField.getText().equalsIgnoreCase(""))
					return;
				else {
					//자신의 턴이 맞는지 체크.
					if(Game.turn == true) {
						// 숫자인지 체크.
						if (checkNum(textField.getText())) {
							// 4자리인지 체크.
							if (textField.getText().length() == 4) {
								// 보낸다.
								textArea.append(id1 + " : " + textField.getText() + "\n");
								textArea.setCaretPosition(textArea.getDocument().getLength());
								// 답 체크.
								String str = checkResult(textField.getText());
								// 답 찍어주기.
								textArea.append(id1 + "'s result : " + str + "\n");
								textArea.setCaretPosition(textArea.getDocument().getLength());
								out.println(
										"Gaming&" + id1 + "&" + id2 + "&GamingMessage&" + textField.getText() + "&" + str); // text
								// 전송
								textField.setText("");
								//
								//
								//
								//게임이 종료되었다면!(내가 입력한 게 정답이라면)
								if(str.startsWith("strike: 4")) {
									JOptionPane.showMessageDialog(Game.this, "YOU WON!");
									//승 하나 올려달라하고
									out.println("Gaming&"+id1+"&GameResult&Win");
									//메인창으로 넘어가자.
									//Game.this.setVisible(false);
								}
								//
								//
								//
								
								//turn 반대로 바꿔주기.
								Game.turn = !Game.turn;
							} else {
								textArea.append("Invalid value. try again." + "\n");
								textArea.setCaretPosition(textArea.getDocument().getLength());
								textField.setText("");
							}
						}

						// 잘못 입력했다면,
						else {
							textArea.append("Invalid value. try again." + "\n");
							textArea.setCaretPosition(textArea.getDocument().getLength());
							textField.setText("");
						}
					}
					//자신의 턴이 아니라면, 
					else {
						textArea.append("NOT YOUR TURN. PLEASE WAIT" + "\n");
						textArea.setCaretPosition(textArea.getDocument().getLength());
						textField.setText("");
					}
					
				}
			}
		});

		/**
		 * chat 기본 틀 게임에서, 내가 상대에게 메세지를 전할거야. Game&[Sender]&[Receiver]&[Message]
		 * <<message>>
		 * 
		 * game 기본 틀 게임에서, 내가 상대에게 이 값을 추측했다고 알려줘.
		 * Game&[sender]&[receiver]&Guess&/[Value] <</[value]>>
		 * 
		 * surrender 버튼 (항복 시, 채팅창으로 넘어감.) 게임에서, 내가 항복했다고 상대에게 알려줘.
		 * Game&[sender]&[receiver]&Surrender <<button click>>
		 * 
		 */

		String strIn;
		// 응답이 있으면,
		while (in.hasNext()) {
			// 받아오고
			strIn = in.nextLine();
			// check
			System.out.println(strIn);

			String[] splitMessage = strIn.split("&");

			// message받는 것이라면, Message & [sender] & contents
			if (splitMessage[0].equalsIgnoreCase("message")) {
				// 콘솔창에 출력.
				System.out.println(splitMessage[1] + " : " + splitMessage[2]);
				// textArea에 출력.
				textArea_1.append(splitMessage[1] + " : " + splitMessage[2] + "\n");
				textArea_1.setCaretPosition(textArea_1.getDocument().getLength());
			}

			// game에 대한 message받는 것이라면, GamingMessage & [sender] & Numbers
			else if (splitMessage[0].equalsIgnoreCase("gamingmessage")) {
				System.out.println(splitMessage[1] + " : " + splitMessage[2]);

				textArea.append(splitMessage[1] + " : " + splitMessage[2] + "\n");
				textArea.setCaretPosition(textArea.getDocument().getLength());
				textArea.append(splitMessage[1] + "'s result : " + splitMessage[3] + "\n");
				textArea.setCaretPosition(textArea.getDocument().getLength());
				//
				//
				//게임이 종료되었다면!(상대가 말한 게 정답이라면)
				if(splitMessage[3].startsWith("strike: 4")) {
					JOptionPane.showMessageDialog(Game.this, "YOU LOSE!");
					//패 하나 올려달라하고
					out.println("Gaming&"+id1+"&GameResult&Lose");
					//메인창으로 넘어가자.
					//Game.this.setVisible(false);					
					break;
				}
				//
				//
				//
				
				//턴 바꿔주기.
				Game.turn = !Game.turn;
			}

		}

	}

	public boolean checkNum(String str) {
		try {
			Integer e = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	// str값 : 숫자 4자리. =>strike ball 판별하기.
	public String checkResult(String str) {
		int temp = Integer.parseInt(str);
		int[] numbers = new int[4];
		numbers[0] = temp / 1000;
		temp %= 1000;
		numbers[1] = temp / 100;
		temp %= 100;
		numbers[2] = temp / 10;
		temp %= 10;
		numbers[3] = temp;

		int[] answers = new int[4];
		int temp_ans = answer;
		answers[0] = temp_ans / 1000;
		temp_ans %= 1000;
		answers[1] = temp_ans / 100;
		temp_ans %= 100;
		answers[2] = temp_ans / 10;
		temp_ans %= 10;
		answers[3] = temp_ans;

		System.out.println(numbers[0] + " " + numbers[1] + " " + numbers[2] + " " + numbers[3]);
		System.out.println(answers[0] + " " + answers[1] + " " + answers[2] + " " + answers[3]);

		int strike = 0;
		int ball = 0;

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				// 둘이 같다면,
				if (numbers[i] == answers[j]) {
					// 자리까지 같다면,
					if (i == j) {
						strike++;
					}
					// 자리는 다르다면,
					else {
						ball++;
					}
				}
			}
		}

		// result
		System.out.println("strike: " + strike + ", ball: " + ball);

		String result = "strike: " + strike + ", ball: " + ball;

		return result;

	}

}