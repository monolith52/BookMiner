package monolith52.bookminer;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import monolith52.bookminer.monitor.BookMonitorService;
import monolith52.bookminer.repository.Book;
import monolith52.bookminer.util.SpringFXMLLoader;

@Controller
public class MainController {

	@Autowired private BookMonitorService service;
	@FXML private MenuBar menuBar;
	@FXML private MenuItem startMenuItem;
	@FXML private MenuItem exitMenuItem;
	@FXML private ScrollPane scrollPane;
	@FXML private VBox bookPaneHolder;
	
	Animation autoScroll;
	
	@FXML
	public void onExitMenuItemAction(ActionEvent e) {
		((Stage)(menuBar.getScene().getWindow())).close();
	}
	
	@FXML
	public void onStartMenuItemAction(ActionEvent e) {
		service.addBookFoundListener(this::addBook);
		new Thread(service).start();
	}
	
	private void addBook(Book book) {
		SpringFXMLLoader loader = new SpringFXMLLoader(getClass().getResource("Book.fxml"));
		Pane bookPane = (Pane)loader.load();
		
		TextArea bookDetailArea = (TextArea)loader.getByNamespace("bookDetail");
		ImageView bookImageView = (ImageView)loader.getByNamespace("bookImage");
		bookDetailArea.setText(book.getTitle());
		bookImageView.setImage(book.getThumbnail());
		
		
		Platform.runLater(() -> {
			if (autoScroll != null) autoScroll.stop();
			bookPaneHolder.getChildren().add(bookPane);
			scrollPane.layout();
			autoScroll = getAutoScrollAnimation();
			autoScroll.play();
		});
	}
	
	private Animation getAutoScrollAnimation() {
		Timeline animation = new Timeline();
		Interpolator interpolator = Interpolator.SPLINE(0.0d, 0.5d, 0.5d, 1.0d);
		animation.getKeyFrames().add(new KeyFrame(new Duration(500.0d), 
				new KeyValue(scrollPane.vvalueProperty(), scrollPane.getVmax(), interpolator)));
		return animation;
	}
}
