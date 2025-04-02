package org.addy.keycloak.repository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import lombok.RequiredArgsConstructor;
import org.addy.keycloak.model.Customer;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

@RequiredArgsConstructor
public class CustomerRepository {
    private static final Logger log = Logger.getLogger(CustomerRepository.class);

    private final MongoClient mongoClient;
    private final MongoCollection<Document> customers;

    public void dispose() {
        log.info("Closing MongoDB connection");
        mongoClient.close();
    }

    public long count() {
        return customers.countDocuments();
    }

    public List<Customer> findAll() {
        var result = new ArrayList<Customer>();

        customers.find()
                .map(Customer::from)
                .into(result);

        return result;
    }

    public Optional<Customer> findById(String id) {
        Customer result = customers.find(eq("_id", new ObjectId(id)))
                .map(Customer::from)
                .first();

        return Optional.ofNullable(result);
    }

    public Optional<Customer> findByName(String name) {
        Customer result = customers.find(eq("name", name))
                .map(Customer::from)
                .first();

        return Optional.ofNullable(result);
    }

    public Optional<Customer> findByEmail(String email) {
        Customer result = customers.find(eq("email", email))
                .map(Customer::from)
                .first();

        return Optional.ofNullable(result);
    }

    public Optional<Customer> findByPhoneNumber(String phoneNumber) {
        Customer result = customers.find(eq("phone_number", phoneNumber))
                .map(Customer::from)
                .first();

        return Optional.ofNullable(result);
    }

    public Customer create(Customer customer) {
        Document insertDocument = customer.toDocument()
                .append("_id", new ObjectId())
                .append("created_at", System.currentTimeMillis())
                .append("active", true);

        InsertOneResult result = customers.insertOne(insertDocument);

        return customers.find(eq("_id", result.getInsertedId()))
                .map(Customer::from)
                .first();
    }

    public void update(String id, Customer customer) {
        customers.updateOne(eq("_id", new ObjectId(id)),
                new Document().append("$set", customer.toDocument()));
    }

    public void delete(String id) {
        customers.deleteOne(eq("_id", new ObjectId(id)));
    }

}
