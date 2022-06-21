package all_chat;
public class mainClass {
	public static void main(String[] args) {
		startApp();
	}
	private static void startApp() {
		new loginScreen().setVisible(true);
	}
}