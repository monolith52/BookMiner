package monolith52.bookminer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@ComponentScan({
		"monolith52.bookminer",
		"monolith52.bookminer.monitor"})
public class SpringApplicationConfig {
	  
	  @Bean
	  public DriverManagerDataSource getDataSource() {
		  DriverManagerDataSource source = new DriverManagerDataSource(
//						  "jdbc:mysql://localhost/bookStore", "test", "test");
				  "jdbc:sqlite:C:/project/SpringTest/sqlite3/bookStore.sqlite3");
		  source.setDriverClassName("org.gjt.mm.mysql.Driver");
		  return source;
	  }
	  
	  @Bean
	  public JdbcTemplate getJdbcTemplate() {
		  return new JdbcTemplate(getDataSource());
	  }

}
