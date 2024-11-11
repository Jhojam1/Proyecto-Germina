package com.Germina.Domain.Services;

import com.Germina.Api.Config.Exception.Exceptions;
import com.Germina.Domain.Dtos.OrderDTO;
import com.Germina.Domain.Mappers.OrderMapper;
import com.Germina.Persistence.Entities.Dish;
import com.Germina.Persistence.Entities.Order;
import com.Germina.Persistence.Repositories.DishRepository;
import com.Germina.Persistence.Repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private ConfigHrService configHrService;

    /**
     * Obtener todos los Pedidos.
     */
    public List<OrderDTO> getAll() {
        return orderRepository.findAll().stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Guardar o actualizar un Pedido.
     */
    public OrderDTO save(OrderDTO orderDTO) {
        // Obtener la hora límite desde ConfigHrService
        LocalTime cutoffTime = configHrService.getCutoffTime();
        if (LocalTime.now().isAfter(cutoffTime)) {
            throw new Exceptions.CutoffTimeExceededException("No se pueden hacer pedidos después de las " + cutoffTime);
        }

        // Buscar el plato solicitado en el pedido
        Long dishId = orderDTO.getDish().getId();
        Optional<Dish> dishOptional = dishRepository.findById(dishId);

        if (dishOptional.isPresent()) {
            Dish dish = dishOptional.get();

            // Verificar si el plato está disponible
            if (dish.getOrdersToday() < dish.getMaxDailyAmount()) {
                // Incrementar el contador diario de pedidos
                dish.setOrdersToday(dish.getOrdersToday() + 1);

                // Guardar el pedido en la base de datos
                Order order = OrderMapper.toEntity(orderDTO);
                order.setDish(dish); // Asociar el plato al pedido
                orderRepository.save(order);

                // Si alcanzamos el límite diario, desactivar el plato
                if (dish.getOrdersToday() >= dish.getMaxDailyAmount()) {
                    dish.setState("Inactivo");
                }

                // Guardar el estado actualizado del plato
                dishRepository.save(dish);

                return orderDTO;
            } else {
                throw new Exceptions.DailyOrderLimitExceededException("El plato ha alcanzado el límite de pedidos diarios.");
            }
        } else {
            throw new Exceptions.DishNotFoundException("Plato no encontrado.");
        }
    }
}
