package com.apress.springrecipes.caching;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MapBasedCustomerRepository implements CustomerRepository {

    private final Map<Long, Customer> repository = new HashMap<>();

    @Override
    @Cacheable(value = "customers", unless="#result == null")
    public Customer find(long customerId) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {}
        return repository.get(customerId);
    }

    @Override
    @CachePut(value="customers", key = "#result.id")
    public Customer create(String name) {
        long id = this.repository.keySet().stream().max(Comparator.naturalOrder()).orElse(0L) + 1;
        Customer customer = new Customer(id);
        customer.setName(name);
        repository.put(id, customer);
        return customer;
    }

    @Override
    @CacheEvict(value="customers", key="#customer.id")
    public void update(Customer customer) {
        repository.put(customer.getId(), customer);
    }

    @Override
    @CacheEvict(value="customers")
    public void remove(long customerId) {
        repository.remove(customerId);
    }
}
