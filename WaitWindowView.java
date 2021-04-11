import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class WaitWindowView {
	PrintWriter out;
	Scanner in;
	String who = "all";
	Person user;
	int p_index = 0; // player index
	String[] requestType = { "Request Game", "Show Record" };

	JFrame frame = new JFrame("Waiting Room");
	private JTextField textField;
	private JTextField txtWaitingRoom;
	private JTextField txtPlayerList;
	JPanel panel = new JPanel();
	JPanel panel_2 = new JPanel();
	JPanel panel_3 = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	JButton btnExit = new JButton("EXIT");
	JTextArea messageArea = new JTextArea();
	JButton whisper = new JButton("To all");

	JButton players[] = new JButton[500]; // player button array

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 * 
	 * @throws IOException
	 */
	// constructor
	public WaitWindowView(Person user, PrintWriter out, Scanner in) throws IOException {
		this.user = user;
		this.out = out;
		this.in = in;

		frame.setSize(792, 591); // 792,591
		frame.setResizable(false);
		frame.setTitle(user.getId() + "'s Waiting Room");
		frame.getContentPane().setBackground(new Color(147, 112, 219));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setBounds(100, 100, 792, 591);
		panel.setBounds(0, 483, 786, 73);

		panel.setBackground(new Color(221, 160, 221));

		txtWaitingRoom = new JTextField();
		txtWaitingRoom.setBounds(14, 13, 236, 36);
		txtWaitingRoom.setFont(new Font("Microsoft Tai Le", Font.BOLD, 23));
		txtWaitingRoom.setForeground(new Color(255, 255, 255));
		txtWaitingRoom.setBackground(new Color(221, 160, 221));
		txtWaitingRoom.setHorizontalAlignment(SwingConstants.CENTER);
		txtWaitingRoom.setEditable(false);
		txtWaitingRoom.setText("< Waiting Room >");
		txtWaitingRoom.setColumns(10);
		panel_3.setBounds(600, 56, 157, 409);

		panel_3.setBackground(new Color(255, 255, 255));

		txtPlayerList = new JTextField();
		txtPlayerList.setBounds(600, 15, 157, 34);
		txtPlayerList.setEditable(false);
		txtPlayerList.setForeground(new Color(255, 255, 255));
		txtPlayerList.setFont(new Font("Microsoft Tai Le", Font.BOLD, 21));
		txtPlayerList.setBackground(new Color(221, 160, 221));
		txtPlayerList.setHorizontalAlignment(SwingConstants.CENTER);
		txtPlayerList.setText("< Player List >");
		txtPlayerList.setColumns(10);
		panel_3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING).addComponent(scrollPane,
				GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE));
		gl_panel_2.setVerticalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING).addComponent(scrollPane,
				Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE));
		messageArea.setEditable(false);

		scrollPane.setViewportView(messageArea);
		panel_2.setBounds(14, 56, 568, 409);
		panel_2.setLayout(gl_panel_2);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(221, 160, 221));

		whisper.setFont(new Font("Microsoft Tai Le", Font.BOLD, 16));
		whisper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		whisper.setBackground(new Color(204, 204, 255));
		whisper.setForeground(new Color(153, 50, 204));
		textField = new JTextField();
		textField.setColumns(10);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.TRAILING).addGroup(Alignment.LEADING,
				gl_panel.createSequentialGroup().addContainerGap()
						.addComponent(whisper, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, 456, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE).addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup().addGap(22)
										.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(whisper)
												.addComponent(textField, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
								.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		btnExit.setFont(new Font("Microsoft Tai Le", Font.BOLD, 17));
		btnExit.setForeground(new Color(153, 50, 204));
		btnExit.setBackground(new Color(204, 204, 255));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_1
				.createSequentialGroup().addContainerGap().addComponent(btnExit).addContainerGap(87, Short.MAX_VALUE)));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_1
				.createSequentialGroup().addGap(22).addComponent(btnExit).addContainerGap(30, Short.MAX_VALUE)));
		panel_1.setLayout(gl_panel_1);
		panel.setLayout(gl_panel);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(txtWaitingRoom);
		frame.getContentPane().add(panel_2);
		frame.getContentPane().add(panel_3);
		frame.getContentPane().add(txtPlayerList);
		frame.getContentPane().add(panel);
		/* gui �� */
		
		
		
		

		textField.addActionListener(new ActionListener() {
			// textField�� action�� �߰��Ѵ�. textField���� enter�� ������
			public void actionPerformed(ActionEvent e) {
				out.println("CHAT&" + textField.getText()); // textField���� text�� �о�� server�� ������.
				if (who.equals("all"))
					textField.setText(""); // textField�� clear���ش�.(prepare for next message)
				else
					textField.setText("whisper to " + who + " : "); // textField�� whisper�� �� �� �ִ� ��ɾ �������ش�.

			} // close ActionListener
		});
		// add action when user press 'whisper' button
		whisper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = whisper.getText();
				switch (str) {
				case "Whisper": // public mode�� ������,
					who = "all";
					textField.setText(""); // textField�� clear���ش�.(prepare for next message)
					whisper.setText("To all"); // label update
					break;
				case "To all": // whisper mode�� ������,
					who = JOptionPane.showInputDialog( // JOptionPane�� ���� whisper�ϰ���� ����� nickname�� �Է¹޴´�.
							frame, "Who do you want to whisper :", "Screen name selection", JOptionPane.PLAIN_MESSAGE);
					textField.setText("whisper to " + who + " : "); // textField�� whisper�� �� �� �ִ� ��ɾ �������ش�.
					whisper.setText("Whisper"); // label update
					break;
				}

			} // close actionPerformed
		}); // close addActionListener
		// add action when user press 'Exit' button
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		}); // close addActionListener
		// run();

	} // close constructor

	// request game
	private void requestGame(String r_id) { // ���ӿ�û �޼ҵ�
		out.println("CHAT&RQGAME " + r_id); // request game
	}

	// request (another user's) information
	private void requestInfo(String r_id) { // ���� ���� �޼ҵ�
		out.println("CHAT&GETRECO " + r_id);
	}

	public void run() throws IOException {
		
		///�ϴ� ����ش�.
		frame.setVisible(true);
		
		try {
			// CHAT & START & [name] & [id] & [pw] & [win] & [lose]
			out.println("CHAT&START&" + user.getName() + "&" + user.getId() + "&" + user.getPw() + "&" + user.getWin()
					+ "&" + user.getLose()); // ä�� ������ �˸�
			while (in.hasNext()) { // server���� �� �޼����� �ִٸ�

				String line = in.nextLine(); // �޼��� ����

				// ������ �缳�� ��û�� ����,
				if (line.startsWith("GOTOGAME&")) {
					String[] goneId = line.split("&");
					// �缳���ϱ�.
					// �� ����� ��ư ��Ȱ��ȭ�ϰ�,
					for (int i = 0; i < p_index; i++) {
						if (players[i].getText().equals(goneId[1])) {
							players[i].setVisible(false);
							panel_3.remove(players[i]);
							// System.out.println("ACK");
							break;
						}
					}
					// ��ư ��ġ �缳��!
					frame.setVisible(true);
					// System.out.println("CHECK");
				}

				if (line.startsWith("SETPLAYER")) {
					int isExist = 0; // check to the player's button is exist. exist : 1 / non exist : 0
					String otherID = line.substring(10);

					System.out.println("OTHER ID: " + otherID);

					if (otherID.contentEquals(user.getId()))
						isExist = 1;

					for (int i = 0; i < p_index; i++) {
						if (players[i].getText().equalsIgnoreCase(otherID)) {
							panel_3.remove(players[i]);
							isExist = 0;
							break;
						}

					}

					if (isExist == 0) {
						players[p_index] = new JButton(otherID);
						players[p_index].setSize(50, 50);
						players[p_index].setForeground(new Color(255, 255, 255));
						players[p_index].setBackground(new Color(153, 50, 204));
						panel_3.add(players[p_index]);

						players[p_index].addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								// �� ����� ���̵� ���� ��ư�� ������ �˾�â�� ����� ���ӽ�û / �������� �� ������ �� �ֵ��� �Ѵ�.
								int choose = JOptionPane.showOptionDialog(frame, "What do you want to do?",
										"To " + otherID, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
										requestType, null);
								if (choose == 0)
									requestGame(otherID);
								else if (choose == 1)
									requestInfo(otherID);
							}
						});

						frame.setVisible(true);
						// System.out.println("add button " + otherID);
						// rrn_panel.add(players[p_index]);
						p_index++;
						// System.out.println("p_index : " + p_index);
					}

				} // close else if
					// �޼����� OUTPLAYER�� �����ϸ� �ش� Player�� playerList���� ���Ž����ش�.
				else if (line.startsWith("OUTPLAYER")) {
					String otherID = line.substring(10);
					System.out.println(otherID);

					for (int i = 0; i < p_index; i++) {
						if (players[i].getText().equals(otherID)) {
							players[i].setVisible(false);
							panel_3.remove(players[i]);
							break;
						}
					}

					frame.setVisible(true);
				}
				// ���� �޼����� MESSAGE�� �����Ѵٸ� messageArea�� �޼����� ����Ѵ�.
				else if (line.startsWith("MESSAGE")) {
					messageArea.append(line.substring(8) + "\n");
					messageArea.setCaretPosition(messageArea.getDocument().getLength());
				}
				// ���� �޼����� WHISPER�� �����Ѵٸ� messageArea�� �ڽſ��Ը� �� �޼����� ����Ѵ�.
				else if (line.startsWith("WHISPER")) {
					messageArea.append(line.substring(8) + "\n");
					messageArea.setCaretPosition(messageArea.getDocument().getLength());
				}
				// ���� READRECO�� �����Ѵٸ� records�� �о�´�.
				else if (line.startsWith("READRECO")) {
					String[] info_arr = line.split(" ");
					JOptionPane.showMessageDialog( // JOptionPane�� ���� whisper�ϰ���� ����� nickname�� �Է¹޴´�.
							frame, "ID : " + info_arr[1] + "\nWins : " + info_arr[2] + "\nLoses : " + info_arr[3],
							info_arr[1] + "'s Records", JOptionPane.INFORMATION_MESSAGE);
				}
				// ���� REQUESTGAME���� �����Ѵٸ� game��û�� ���� ���� ��f��.
				else if (line.startsWith("REQUESTGAME")) {

					String[] rq_arr = line.split(" ");
					int check = JOptionPane.showConfirmDialog( // JOptionPane�� ���� whisper�ϰ���� ����� nickname�� �Է¹޴´�.
							frame, "\"" + rq_arr[1] + "\" requests game to you!\n Do you want to play?",
							rq_arr[1] + "'s Game Request", JOptionPane.YES_NO_OPTION);
					if (check == JOptionPane.YES_OPTION) {
						out.println("CHAT&REPLYTO " + rq_arr[1] + " yes");

						// �� ��� ������ ������ �˷��ֱ�.
						out.println("GOTOGAME&" + user.getId());

						// frame �ݱ�.
						frame.setVisible(false);
						frame.dispose();

						// user 1
						GameWaitingRoom newGame = new GameWaitingRoom(user, rq_arr[1], out, in);
						break;
					} else {
						out.println("CHAT&REPLYTO " + rq_arr[1] + " no");
					}
				} else if (line.startsWith("REPLY")) {
					String[] rq_arr = line.split(" ");
					if (rq_arr[2].equalsIgnoreCase("yes")) {

						// �� ��� ������ ������ �˷��ֱ�.
						out.println("GOTOGAME&" + user.getId());

						// frame �ݱ�.
						frame.setVisible(false);
						frame.dispose();

						// user 2
						GameWaitingRoom newGame = new GameWaitingRoom(user, rq_arr[1], out, in);
						break;
					} else {
						JOptionPane.showMessageDialog(frame, "The request for " + rq_arr[1] + " was rejected.",
								"Reply from " + rq_arr[1], JOptionPane.OK_OPTION);
					}
				}

			}

		} // close try
		finally { // ä���� �����ϰ��� �Ѵٸ�
			frame.setVisible(false); // frameâ�� �ݴ´�.
			frame.dispose(); // �� client�� frame�� �����Ѵ�.
		} // close finally
	}
}