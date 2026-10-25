package com.alin.lin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableJpaAuditing
public class LeaveApiApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(LeaveApiApplication.class, args);

    }
}
