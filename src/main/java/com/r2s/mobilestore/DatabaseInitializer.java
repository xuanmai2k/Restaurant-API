package com.r2s.mobilestore;

import lombok.NonNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


/**
 * This class to init database
 *
 * @author kyle
 * @since 2023-10-02
 */
@Component
public class DatabaseInitializer implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {

    }

}
