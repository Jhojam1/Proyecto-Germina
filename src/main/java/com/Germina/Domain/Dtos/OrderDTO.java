package com.Germina.Domain.Dtos;


import com.Germina.Persistence.Entities.Dish;
import com.Germina.Persistence.Entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private User user;
    private Dish dish; // Agregar referencia al plato
    private Date fechaPedido;
}
