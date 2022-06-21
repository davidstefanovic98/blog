package blog.config;

import blog.specification.GenericSpecificationConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

@Configuration
@EnableJpaAuditing
@RequiredArgsConstructor
@EnableAspectJAutoProxy
public class Config {
    private final ConverterRegistry converterRegistry;

    @PostConstruct
    public void init() {
        registerConverters();
    }

    private void registerConverters() {
        converterRegistry.addConverter(new GenericSpecificationConverter());
    }

    @Bean(name = "passwordEncoder")
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean(name = "errorMessages")
    public PropertiesFactoryBean errorMessages() {
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        bean.setLocation(new ClassPathResource("errors.properties"));
        return bean;
    }
}