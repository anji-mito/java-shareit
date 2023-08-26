import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("ci,test")
public class TestConfig {
    @Bean
    public ModelMapper testModelMapper() {
        return new ModelMapper();
    }
}