package com.swing_boot.reactive.registry;

import com.swing_boot.reactive.registry.components.AppFrame;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.awt.*;

@SpringBootConfiguration
@ComponentScan("com.swing_boot")
public class SwingBootApplication {
    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(SwingBootApplication.class);
        builder.headless(false);
        final ConfigurableApplicationContext context = builder.run(args);

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                AppFrame ex = context.getBean(AppFrame.class);
                ex.setVisible(true);
            }
        });
    }
}
