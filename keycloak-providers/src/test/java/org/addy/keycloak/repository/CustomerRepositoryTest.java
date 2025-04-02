package org.addy.keycloak.repository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.addy.keycloak.model.Customer;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerRepositoryTest {
    private CustomerRepository repository;

    @BeforeEach
    void init() {
        String uri = "mongodb://admin:mongoadm@localhost:27017";
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase("hello_ms_customers");
        MongoCollection<Document> customers = database.getCollection("customers");

        repository = new CustomerRepository(mongoClient, customers);
    }

    @AfterEach
    void cleanup() {
        repository.dispose();
    }

    @Test
    void canFetchAllCustomers() {
        List<Customer> customers = repository.findAll();
        assertThat(customers).isNotEmpty();
    }

    @Test
    void canFetchCustomerByEmail() {
        Optional<Customer> customer = repository.findByEmail("ethan.smith@dummy.xyz");
        assertThat(customer).isNotEmpty();
        assertThat(customer.get().getEmail()).isEqualTo("ethan.smith@dummy.xyz");
    }

    //@Test
    void enableAllCustomers() {
        repository.findAll().forEach(customer -> {
            customer.setActive(true);
            if (customer.getCreatedAt() == null) {
                customer.setCreatedAt(System.currentTimeMillis());
            }
            repository.update(customer.getId(), customer);
        });

        assertThat(repository.findAll())
                .isNotEmpty()
                .allSatisfy(customer -> assertThat(customer.isActive()).isTrue());
    }
}
