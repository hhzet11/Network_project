import java.io.Serializable;

public class Person implements Serializable{
	
	private static final long serialVersionUID = -5545523340140992213L;
	
	private String name; // �̸�
	private String Id; // ID
	private String pw; // PassWord
	private int win; // �̱� Ƚ��
	private int lose; // �� Ƚ��
	private boolean inGame; // ���ӹ濡 �ִ��� ����
	
	//constructor
	public Person(String name, String Id, String pw, int win, int lose) {
		this.name = name;
		this.Id = Id;
		this.pw = pw;
		this.win = win;
		this.lose = lose;
		this.inGame = false;
	}
	
	//getter
	public String getName() {
		return name;
	}
	public String getId() {
		return Id;
	}
	public String getPw() {
		return pw;
	}
	public int getWin() {
		return win;
	}
	public int getLose() {
		return lose;
	}
	
	//setter
	public void setName(String name) {
		this.name = name;
	}
	public void setId(String Id) {
		this.Id = Id;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public void setWin(int win) {
		this.win = win;
	}
	public void setLose(int lose) {
		this.lose = lose;
	}
}
