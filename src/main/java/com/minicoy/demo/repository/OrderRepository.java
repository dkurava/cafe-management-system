package com.minicoy.demo.repository;

import com.minicoy.demo.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Tells Spring: "This is my Data Access Layer"
public interface OrderRepository extends JpaRepository<Order, Long> {
    // That's it! Spring will automatically provide methods like
    // save(), findAll(), deleteById(), etc.
}
