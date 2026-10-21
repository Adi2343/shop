package edu.arch.shop.customer.entity.controller;

import edu.arch.shop.customer.entity.dto.CustomerRequest;
import edu.arch.shop.customer.entity.dto.CustomerResponse;
import edu.arch.shop.customer.entity.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/customers")

public class CustomerController {
    private final CustomerService CustomerService;

    public CustomerController(CustomerService CustomerService) {
        this.CustomerService = CustomerService;
    }

    @GetMapping
    public List<CustomerResponse> findAll() {
        return CustomerService.findAll();
    }

    @GetMapping("/{id}")
    public customerResponse findById(@PathVariable Long id) {
        return CustomerService.findById(id);
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@RequestBody CustomerRequest request) {
        CustomerResponse response = CustomerService.create(request);
        return ResponseEntity.created(URI.create("/customers/" + response.id())).body(response);
    }

    @PutMapping("/{id}")
    public CustomerResponse update(@PathVariable Long id, @RequestBody CustomerRequest request) {
        return CustomerService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        CustomerService.delete(id);
    }
}
