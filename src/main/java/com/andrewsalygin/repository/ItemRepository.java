package com.andrewsalygin.repository;

import com.andrewsalygin.domain.mongo.Item;

import java.util.List;

public interface ItemRepository {

    void saveAll(List<Item> listToSave, String collectionName);
}
