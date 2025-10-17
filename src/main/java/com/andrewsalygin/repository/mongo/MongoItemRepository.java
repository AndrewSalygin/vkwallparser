package com.andrewsalygin.repository.mongo;

import com.andrewsalygin.domain.mongo.Item;
import com.andrewsalygin.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MongoItemRepository implements ItemRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoItemRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void saveAll(List<Item> items, String collectionName) {
        if (items == null || items.isEmpty()) return;
        mongoTemplate.insert(items, collectionName);
    }
}
