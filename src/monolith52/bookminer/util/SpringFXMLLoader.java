package monolith52.bookminer.util;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.util.Callback;
import monolith52.bookminer.SpringApplicationConfig;

public class SpringFXMLLoader {

	private static final ApplicationContext applicationContext = new AnnotationConfigApplicationContext(
			SpringApplicationConfig.class);

	private FXMLLoader loader;
	
	public SpringFXMLLoader(URL location, ResourceBundle resource) {
		loader = new FXMLLoader(location, resource);
	}
	
	public SpringFXMLLoader(URL location) {
		loader = new FXMLLoader(location, ResourceUtil.getBundle());
	}
	
	public Object getControler() {
		return loader.getController();
	}
	
	public ObservableMap<String, Object> getNamespace() {
		return loader.getNamespace();
	}
	
	public Object getByNamespace(String key) {
		return loader.getNamespace().get(key);
	}
	
	public Object load() {
		try {
			loader.setControllerFactory(new Callback<Class<?>, Object>() {
				@Override
				public Object call(Class<?> clazz) {
					return applicationContext.getBean(clazz);
				}
			});
			return loader.load();
		} catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	public static Object load(URL location) {
		return new SpringFXMLLoader(location).load();
	}

	public static ApplicationContext getContext() {
		return applicationContext;
	}
}
