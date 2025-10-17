package com.andrewsalygin;

import com.andrewsalygin.config.AppConfig;
import com.andrewsalygin.service.mongo.VkWallParserServiceToMongoImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        context.register(VkWallParserServiceToMongoImpl.class);

        VkWallParserServiceToMongoImpl parser = context.getBean(VkWallParserServiceToMongoImpl.class);
        parser.parseAllWalls("all");

        context.close();
    }
}
