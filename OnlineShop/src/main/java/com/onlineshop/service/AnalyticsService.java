package com.onlineshop.service;

import com.onlineshop.model.ApiAccessEntity;
import com.onlineshop.model.OrderEntity;
import com.onlineshop.model.OrderedItemEntity;
import com.onlineshop.repository.ApiAccessRepository;
import com.onlineshop.repository.OrderRepository;
import com.onlineshop.repository.OrderedItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    @Autowired
    ApiAccessRepository apiAccessRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderedItemRepository orderedItemRepository;

    public ResponseEntity<Object> getSalesLog(int year, int month) {
        List<OrderEntity> orders = orderRepository.findOrderEntitiesByOrderTimeBetween(
                Date.valueOf(String.format("%d-%02d-%02d", year,  month, 1)),
                Date.valueOf(String.format("%d-%02d-%02d", year,  month, 30))
        );

        List<Map> listItems = new ArrayList<>();

        for(var order: orders) {
            var orderedItems = orderedItemRepository.findOrderedItemEntitiesByProductListId(order.getProductListId());
            for(var orderedItem: orderedItems) {
                listItems.add(Map.of(
                        "user_id", order.getUserId(),
                        "product_id", orderedItem.getProductId(),
                        "date", order.getOrderTime()
                ));
            }
        }

        return ResponseEntity.ok(Map.of("report", listItems));
    }


    public ResponseEntity<Object> getAccessLog(int year, int month) {
        List<ApiAccessEntity> accesses = apiAccessRepository.findApiAccessEntitiesByTimeBetween(
                Date.valueOf(String.format("%d-%02d-%02d", year,  month, 1)),
                Date.valueOf(String.format("%d-%02d-%02d", year,  month, 30))
        );
        HashMap<String, Integer> accessMapping = new HashMap<>();

        for(var access: accesses) {
            if(accessMapping.containsKey(access.getPath())) {
                accessMapping.put(access.getPath(), accessMapping.get(access.getPath()) + 1);
            }
            else {
                accessMapping.put(access.getPath(), 1);
            }
        }

        var result = new HashMap<>();

        var resultList = new ArrayList<>();

        for(var key: accessMapping.keySet()) {
            resultList.add(Map.of(
                    "path", key,
                    "visit_count", accessMapping.get(key)
            ));
        }

        result.put("report", resultList);

        return ResponseEntity.ok(result);
    }

    public void logAccess(String url) {
        var apiAccessEntity = new ApiAccessEntity();

        apiAccessEntity.setPath(url);

        apiAccessRepository.save(apiAccessEntity);
    }

}
