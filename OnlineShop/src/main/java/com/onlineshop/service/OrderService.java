package com.onlineshop.service;

import com.onlineshop.Util;
import com.onlineshop.model.AddressEntity;
import com.onlineshop.model.OrderEntity;
import com.onlineshop.model.OrderedItemEntity;
import com.onlineshop.model.PaymentEntity;
import com.onlineshop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    OrderedItemRepository orderedItemRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<Object> checkOut(String creditCardNumber, String cvc, String expirationTime, String address, List<Object> products, int userId) {
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setCreditCardNumber(creditCardNumber);
        paymentEntity.setCvc(cvc);
        paymentEntity.setExpirationTime(expirationTime);
        paymentEntity.setUserId(userId);

        paymentEntity = paymentRepository.save(paymentEntity);


        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setAddress(address);
        addressEntity.setUserId(userId);

        addressEntity = addressRepository.save(addressEntity);

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUserId(userId);
        orderEntity.setProductListId(0);
        orderEntity.setAddressId(addressEntity.getId());
        orderEntity.setPaymentId(paymentEntity.getId());

        orderEntity = orderRepository.save(orderEntity);
        orderEntity.setProductListId(orderEntity.getId());
        orderRepository.save(orderEntity);

        for (var productId: products)
        {
            OrderedItemEntity orderedItemEntity = new OrderedItemEntity();
            orderedItemEntity.setProductListId(orderEntity.getProductListId());
            orderedItemEntity.setUserId(userId);
            orderedItemEntity.setProductId(numberConvert(productId));

            orderedItemRepository.save(orderedItemEntity);
        }

        return ResponseEntity.ok(Util.buildGenericResponse(0, "Success, should clear cart."));
    }

    public ResponseEntity<Object> listOrders(int userId, int page) {
        List<OrderEntity> orderEntities = orderRepository.findOrderEntitiesByUserId(userId, PageRequest.of(page, 10));

        return ResponseEntity.ok(Map.of(
                "orders", convertItems(orderEntities)
        ));
    }

    public ResponseEntity<Object> getProductsOnList(int productListId, int userId) {
        List<OrderedItemEntity> orderedItemEntities = orderedItemRepository.findOrderedItemEntitiesByProductListId(productListId);

        List<Integer> products = new ArrayList<>();

        for (var orderedItemEntity: orderedItemEntities) {
            products.add(orderedItemEntity.getProductId());
        }

        return ResponseEntity.ok(Map.of(
                "products", products
        ));
    }


    List<Map> convertItems(List<OrderEntity> orderEntities) {
        List<Map> result = new ArrayList<>();

        for (var entity: orderEntities) {
            result.add(Map.of(
                    "id", entity.getId(),
                    "time", entity.getOrderTime(),
                    "address", entity.getAddressId(),
                    "product_list_id", entity.getProductListId()
            ));
        }

        return result;
    }

    int numberConvert(Object o) {
        if(o instanceof Integer) {
            return (int)o;
        }
        else {
            return Integer.parseInt((String)o);
        }
    }

}
