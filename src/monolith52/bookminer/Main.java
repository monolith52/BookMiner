package monolith52.bookminer;
	
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import monolith52.bookminer.util.FileLockUtil;
import monolith52.bookminer.util.SpringFXMLLoader;


public class Main extends Application {
	static FileLockUtil fileLock = new FileLockUtil(FileLockUtil.LAUNCH_LOCK_FILE);

	@Override
	public void start(Stage primaryStage) {
		BorderPane root = (BorderPane)SpringFXMLLoader.load(getClass().getResource("Main.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		
		primaryStage.setOnCloseRequest((e) -> {
			fileLock.unlockForLaunch();
			Platform.exit();
			System.exit(0);
		});
		primaryStage.show();
	}
	
	

	public static void main(String[] args) {
		if (!fileLock.tryLockForLaunch()) {
			System.out.println("Application exited due to double launching");
			Platform.exit();
			return;
		}
		
		launch(args);
	}
}
