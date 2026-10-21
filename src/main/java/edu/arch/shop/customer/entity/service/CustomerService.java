package edu.arch.shop.customer.entity.service;

import edu.arch.shop.customer.dto.customerRequest;
import edu.arch.shop.customer.dto.customerResponse;
import edu.arch.shop.customer.entity.customer;
import edu.arch.shop.customer.entity.repository.CustomerRepository;
import edu.arch.shop.customer.repository.customerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)

public class CustomerService {

    private final CustomerRepository CustomerRepository;

    public CustomerService(CustomerRepository CustomerRepository) {
        this.CustomerRepository = CustomerRepository;
    }

    public List<CustomerResponse> findAll() {
        return CustomerRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public customerResponse findById(Long id) {
        return toResponse(findcustomer(id));
    }

    @Transactional
    public customerResponse create(CustomerRequest request) {
        validate(request);
        Customer customer = new Customer(
                request.customerName(),
                request.productName(),
                request.quantity(),
                request.unitPrice()
        );
        return toResponse(CustomerRepository.save(customer));
    }

    @Transactional
    public CustomerResponse update(Long id, CustomerRequest request) {
        validate(request);
        Customer customer = findCustomer(id);
        customer.setCustomerName(request.customerName());
        customer.setProductName(request.productName());
        customer.setQuantity(request.quantity());
        customer.setUnitPrice(request.unitPrice());
        return toResponse(customerRepository.save(customer));
    }

    @Transactional
    public void delete(Long id) {
        Customer customer = findCustomer(id);
        customerRepository.delete(customer);
    }

    private customer findcustomer(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private void validate(CustomerRequest request) {
        if (request.customerName() == null || request.customerName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer name is required");
        }
        if (request.customerName().length() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer name is too long");
        }
        if (request.productName() == null || request.productName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name is required");
        }
        if (request.productName().length() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name is too long");
        }
        if (request.quantity() == null || request.quantity() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be positive");
        }
        if (request.unitPrice() == null || request.unitPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unit price must be positive");
        }
        if (request.unitPrice().scale() > 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unit price can have at most 2 decimal places");
        }
        if (request.unitPrice().compareTo(new BigDecimal("9999999999.99")) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unit price is too high");
        }
    }

    private customerResponse toResponse(customer customer) {
        BigDecimal totalPrice = customer.getUnitPrice()
                .multiply(BigDecimal.valueOf(customer.getQuantity()));

        return new customerResponse(
                customer.getId(),
                customer.getCustomerName(),
                customer.getProductName(),
                customer.getQuantity(),
                customer.getUnitPrice(),
                totalPrice
        );
    }
}
