package com.idione.inoc.tasks;

import org.javalite.activejdbc.Base;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.idione.inoc.services.InocProcessor;

@Component
public class EmailReaderTask {

    private InocProcessor inocProcessor;

    @Autowired
    public void setInocProcessor(InocProcessor inocProcessor) {
        this.inocProcessor = inocProcessor;
    }
    
    @Scheduled(fixedDelay = 60000)
    public void reportCurrentTime() {
        try {
            Base.open();
            inocProcessor.run();
        } finally {
            Base.close();
        }
    }
}
