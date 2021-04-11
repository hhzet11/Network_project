import java.io.*;
import java.util.*;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JTextArea;

public class GameWaitingRoom extends JFrame {
	private Person me; // 나의 모든 정보
	private String id1; // 나의 아이디
	private String id2; // 상대
	private Scanner in;
	private PrintWriter out;

	private JPanel contentPane;
	private JTextField txtWaitingRoom;
	private JTextField txtText;
	private JTextField textField;
	private JTextArea textArea;
	
	
	/**
	 * Create the frame.
	 */
	public GameWaitingRoom(Person me, String id2, PrintWriter out, Scanner in) {
		// init
		this.me = me;
		this.id1 = me.getId();
		this.id2 = id2;
		this.out = out;
		this.in = in;

		this.setTitle(id1 + "'s Waiting Room");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 684, 454);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(222, 184, 135));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		txtWaitingRoom = new JTextField();
		txtWaitingRoom.setForeground(new Color(255, 255, 255));
		txtWaitingRoom.setBackground(new Color(139, 69, 19));
		txtWaitingRoom.setEditable(false);
		txtWaitingRoom.setHorizontalAlignment(SwingConstants.CENTER);
		txtWaitingRoom.setFont(new Font("Microsoft Tai Le", Font.BOLD, 24));
		txtWaitingRoom.setText("Waiting Room");
		txtWaitingRoom.setColumns(10);

		JScrollPane scrollPane = new JScrollPane();
		/**
		 * ready 버튼 게임에서, 내가 ready했다고 상대에게 알려줘. Game&[sender]&[receiver]&Ready <<button
		 * click>>
		 */
		JButton btnReady = new JButton("READY");
		btnReady.setBackground(new Color(253, 245, 230));
		btnReady.setForeground(new Color(160, 82, 45));
		btnReady.setFont(new Font("Microsoft Tai Le", Font.BOLD, 15));
		btnReady.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 현재 ready 상태라면
				if (btnReady.getText().equalsIgnoreCase("ready")) {
					String str = "Game&" + id1 + "&" + id2 + "&" + "Ready";
					out.println(str);
					System.out.println(str + " sent.");
					btnReady.setText("CANCEL");
					btnReady.setBackground(new Color(255, 255, 051));
				}
				// 아니라면
				else {
					String str = "Game&" + id1 + "&" + id2 + "&" + "Cancel";
					out.println(str);
					System.out.println(str + " sent.");
					btnReady.setText("READY");
					btnReady.setBackground(new Color(253, 245, 230));
				}

			}
		});
		/**
		 * oppInfo 버튼 게임에서, 내가 상대의 정보를 보여달라고 서버에게 요청 Game&[sender]&[상대id]&showInfo
		 */
		JButton btnOther = new JButton("OTHER");
		btnOther.setBackground(new Color(253, 245, 230));
		btnOther.setForeground(new Color(160, 82, 45));
		btnOther.setFont(new Font("Microsoft Tai Le", Font.BOLD, 15));
		btnOther.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Game & [sender] & [receiver] & showInfo
				String str = "Game&" + id1 + "&" + id2 + "&showInfo";
				out.println(str);
				System.out.println("Request " + id2 + "'s info");
			}
		});
		/**
		 * exit 버튼 게임에서, 내가 나갔다고 상대에게 알려줘. Game&[sender]&[receiver]&exit
		 */
		JButton btnExit = new JButton("  EXIT  ");
		btnExit.setBackground(new Color(253, 245, 230));
		btnExit.setForeground(new Color(160, 82, 45));
		btnExit.setFont(new Font("Microsoft Tai Le", Font.BOLD, 15));
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ??
				try {
					WaitWindowView waitRoom = new WaitWindowView(me, out, in);
					waitRoom.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					waitRoom.run();
				} catch (Exception e2) {
					e2.getStackTrace();
				}
			}
		});

		txtText = new JTextField();
		txtText.setForeground(new Color(255, 255, 255));
		txtText.setBackground(new Color(139, 69, 19));
		txtText.setEditable(false);
		txtText.setFont(new Font("Microsoft Tai Le", Font.BOLD, 15));
		txtText.setHorizontalAlignment(SwingConstants.CENTER);
		txtText.setText("TEXT :");
		txtText.setColumns(10);

		textField = new JTextField();
		textField.setColumns(10);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane
				.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
						.createSequentialGroup().addContainerGap().addGroup(gl_contentPane
								.createParallelGroup(Alignment.LEADING)
								.addComponent(
										txtWaitingRoom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createSequentialGroup().addGroup(gl_contentPane
										.createParallelGroup(Alignment.TRAILING, false)
										.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
												.addComponent(txtText, GroupLayout.PREFERRED_SIZE, 69,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED).addComponent(textField))
										.addComponent(scrollPane, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 516,
												GroupLayout.PREFERRED_SIZE))
										.addGap(18)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
												.addComponent(btnReady).addComponent(btnOther).addComponent(btnExit))))
						.addContainerGap(49, Short.MAX_VALUE)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup()
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup().addGap(25)
								.addComponent(txtWaitingRoom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 266, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup().addGap(119).addComponent(btnReady).addGap(34)
								.addComponent(btnOther).addGap(37).addComponent(btnExit)))
				.addGap(18)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addContainerGap(35, Short.MAX_VALUE)));

		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		contentPane.setLayout(gl_contentPane);

		this.setVisible(true);
		/**
		 * chat 기본 틀 게임에서, 내가 상대에게 메세지를 전할거야. Game&[Sender]&[Receiver]&Message&[Message]
		 * <<message>>
		 * 
		 */
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// enter 방지.
				if (textField.getText().equalsIgnoreCase(""))
					return;
				textArea.append(id1 + " : " + textField.getText() + "\n");
				textArea.setCaretPosition(textArea.getDocument().getLength());
				out.println("Game&" + id1 + "&" + id2 + "&Message&" + textField.getText()); // text 입력한 것 Server로 전송
				textField.setText("");// text field 빈 문장으로 초기화
			}
		});

		/**
		 * ready 버튼 게임에서, 내가 ready했다고 상대에게 알려줘. Game&[sender]&[receiver]&Ready <<button
		 * click>>
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
			// ready라면,
			if (splitMessage[0].equalsIgnoreCase("ready")) {
				// 노란색으로 바꿔주기.
				btnOther.setBackground(new Color(255, 255, 051));
			}
			// cancel하면,
			else if (splitMessage[0].equalsIgnoreCase("cancel")) {
				// 기본 색상으로 바꿔주기.
				btnOther.setBackground(new Color(253, 245, 230));
			}
			// 게임 시작 명령이면,
			else if (splitMessage[0].equalsIgnoreCase("gamestart")) {
				System.out.println("Game Start");
				System.out.println(splitMessage[3]);
				this.setVisible(false);
				//
				//
				//
				boolean turnFirst = true;
				if (splitMessage[1].compareTo(splitMessage[2]) < 0) {
					turnFirst = !turnFirst;
				}
				//
				//
				//
				Game gameStart = new Game(splitMessage[1], splitMessage[2], in, out, Integer.parseInt(splitMessage[3]),
						turnFirst);
			}
			// message받는 것이라면, Message & [sender] & contents
			else if (splitMessage[0].equalsIgnoreCase("message")) {
				// 콘솔창에 출력.
				System.out.println(splitMessage[1] + " : " + splitMessage[2]);
				// textArea에 출력.
				textArea.append(splitMessage[1] + " : " + splitMessage[2] + "\n");
				textArea.setCaretPosition(textArea.getDocument().getLength());
			}
			// READRECO & [id] & [win] & [lose]
			else if (splitMessage[0].equalsIgnoreCase("readreco")) {
				JOptionPane.showMessageDialog(this,
						"ID : " + splitMessage[1] + "\nWins : " + splitMessage[2] + "\nLoses : " + splitMessage[3],
						splitMessage[1] + "'s Records", JOptionPane.INFORMATION_MESSAGE);
			}
		}

	}

}