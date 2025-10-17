package com.andrewsalygin.service.mongo;

import com.andrewsalygin.domain.mongo.Item;
import com.andrewsalygin.repository.mongo.MongoItemRepository;
import com.andrewsalygin.service.VkWallParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VkWallParserServiceToMongoImpl implements VkWallParser {

    private static final int COUNT = 100;

    public static final int PAUSE = 400;

    private static final Logger LOGGER = LoggerFactory.getLogger(VkWallParserServiceToMongoImpl.class);

    private final String accessToken;

    private final String apiVersion;

    private final List<String> groups;

    private final MongoItemRepository mongoItemRepository;

    @Autowired
    public VkWallParserServiceToMongoImpl(
        @Value("${secrets.access-token}") String accessToken, @Value("${api.api-version}") String apiVersion,
        @Qualifier("groups") List<String> groups, MongoItemRepository mongoItemRepository
    ) {
        this.accessToken = accessToken;
        this.apiVersion = apiVersion;
        this.groups = groups;
        this.mongoItemRepository = mongoItemRepository;
    }

    public void parseAllWalls(String filter) {
        try (HttpClient client = HttpClient.newHttpClient()) {
            for (var domain : groups) {
                int offset = 0;
                while (true) {
                    String url = String.format(
                        "https://api.vk.com/method/wall.get?v=%s&access_token=%s&domain=%s&count=%d&offset=%d&filter=%s",
                        apiVersion, accessToken, domain, COUNT, offset, filter
                    );
                    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    JSONObject json = new JSONObject(response.body());

                    if (json.has("error")) {
                        LOGGER.error("VK API Error: {}", json.getJSONObject("error").getString("error_msg"));
                        break;
                    }

                    JSONObject responseObj = json.getJSONObject("response");
                    JSONArray items = responseObj.getJSONArray("items");

                    if (items.isEmpty()) break;

                    List<Item> itemsToSave = new ArrayList<>();
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);

                        Map<String, Object> map = new HashMap<>();
                        for (String key : item.keySet()) {
                            map.put(key, item.get(key));
                        }
                        itemsToSave.add(new Item(map));
                    }
                    mongoItemRepository.saveAll(itemsToSave, domain);
                    LOGGER.info("Скачаны посты группы {} от {} до {} поста", domain, offset, offset + items.length());
                    offset += COUNT;
                    Thread.sleep(PAUSE);
                }
            }
        } catch (InterruptedException | IOException e) {
            LOGGER.error("Произошла ошибка при парсинге", e);
        }

        LOGGER.info("Импорт завершен!");
    }
}

