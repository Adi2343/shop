package edu.arch.shop.customer.entity.repository;

import edu.arch.shop.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Order, Long> {
}
