package org.addy.keycloak.repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import lombok.RequiredArgsConstructor;
import org.addy.keycloak.model.Customer;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.jboss.logging.Logger;

import java.util.*;
import java.util.stream.Stream;

import static com.mongodb.client.model.Filters.*;

@RequiredArgsConstructor
public class CustomerRepository {
    public static final String SEARCH_KEY = "keycloak.session.realm.users.query.search";
    public static final String EXACT_KEY = "keycloak.session.realm.users.query.exact";
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

    public List<Customer> findAll(Map<String, String> query, Integer firstResult, Integer maxResults) {
        FindIterable<Document> iterable = customers.find();

        if (!(query == null || query.isEmpty())) {
            iterable = iterable.filter(translateQuery(query));
        }

        if (firstResult != null) {
            iterable = iterable.skip(firstResult);
        }

        if (maxResults != null) {
            iterable = iterable.limit(maxResults);
        }

        var result = new ArrayList<Customer>();
        iterable.map(Customer::from).into(result);

        return result;
    }

    public Optional<Customer> findById(String id) {
        Customer result = customers.find(eq("_id", new ObjectId(id)))
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

    private static Bson translateQuery(Map<String, String> query) {
        List<Bson> predicates;

        if (query.containsKey(SEARCH_KEY)) {
            final String search = query.get(SEARCH_KEY);

            if (search == null || search.isBlank() || "*".equals(search)) {
                return null;
            }

            predicates = Stream.of("email", "name")
                    .map(field -> regex(field, search, "i"))
                    .toList();
        } else {
            final Set<String> queryableFields = Set.of("username", "email", "firstName", "lastName");
            final boolean exact = "true".equals(query.get(EXACT_KEY));

            predicates = query.entrySet()
                    .stream()
                    .filter(entry -> queryableFields.contains(entry.getKey()))
                    .map(entry -> buildPredicate(entry.getKey(), entry.getValue(), exact))
                    .toList();
        }

        return or(predicates);
    }

    private static Bson buildPredicate(String attribute, String value, boolean exact) {
        return switch (attribute) {
            case "username", "email" -> exact
                    ? eq("email", value)
                    : regex("email", value, "i");
            case "firstName" -> regex("name", "^" + value, "i");
            case "lastName" -> regex("name", value + "$", "i");
            default -> null;
        };
    }

}
