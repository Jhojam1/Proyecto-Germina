package com.Germina.Domain.Mappers;


import com.Germina.Domain.Dtos.OrderDTO;
import com.Germina.Persistence.Entities.Order;

public class OrderMapper {

    public static Order toEntity(OrderDTO orderDTO) {
        Order order = new Order();
        order.setId(orderDTO.getId());
        order.setUser(orderDTO.getUser());
        order.setDish(orderDTO.getDish());
        order.setFechaPedido(orderDTO.getFechaPedido());
        return order;
    }

    public static OrderDTO toDto(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setUser(order.getUser());
        orderDTO.setDish(order.getDish());
        orderDTO.setFechaPedido(order.getFechaPedido());
        return orderDTO;
    }
}
