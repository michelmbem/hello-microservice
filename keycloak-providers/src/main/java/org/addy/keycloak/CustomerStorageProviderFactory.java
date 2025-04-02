package org.addy.keycloak;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.addy.keycloak.repository.CustomerRepository;
import org.bson.Document;
import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.component.ComponentValidationException;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.storage.UserStorageProviderFactory;

import java.util.List;

@Getter
public class CustomerStorageProviderFactory implements
        UserStorageProviderFactory<CustomerStorageProvider> {

    private static final String DB_HOST = "db.host";
    private static final String DB_PORT = "db.port";
    private static final String DB_USER = "db.user";
    private static final String DB_PASSWORD = "db.password";
    private static final String DB_NAME = "db.name";
    private static final String DB_COLLECTION = "db.collection";
    private static final Logger log = Logger.getLogger(CustomerStorageProviderFactory.class);

    private final List<ProviderConfigProperty> configProperties;

    public CustomerStorageProviderFactory() {
        configProperties = ProviderConfigurationBuilder.create()
                .property()
                    .name(DB_HOST)
                    .label("Database host")
                    .type(ProviderConfigProperty.STRING_TYPE)
                    .defaultValue("localhost")
                    .helpText("MongoDB server hostname or IP address")
                    .add()
                .property()
                    .name(DB_PORT)
                    .label("Database port")
                    .type(ProviderConfigProperty.INTEGER_TYPE)
                    .defaultValue(27017)
                    .helpText("MongoDB server port")
                    .add()
                .property()
                    .name(DB_USER)
                    .label("Database username")
                    .type(ProviderConfigProperty.STRING_TYPE)
                    .defaultValue("admin")
                    .helpText("Name of a valid user on the MongoDB server")
                    .add()
                .property()
                    .name(DB_PASSWORD)
                    .label("Database password")
                    .type(ProviderConfigProperty.PASSWORD)
                    .helpText("Password of the user that connects to the MongoDB server")
                    .add()
                .property()
                    .name(DB_NAME)
                    .label("Database name")
                    .type(ProviderConfigProperty.STRING_TYPE)
                    .defaultValue("hello_ms_customers")
                    .helpText("Name of the MongoDB database")
                    .add()
                .property()
                    .name(DB_COLLECTION)
                    .label("Collection name")
                    .type(ProviderConfigProperty.STRING_TYPE)
                    .defaultValue("customers")
                    .helpText("Name of the collection to query on")
                    .add()
                .build();
    }

    @Override
    public String getId() {
        return "hello-microservice-customers";
    }

    @Override
    public String getHelpText() {
        return "A User Storage Provider for the Hello Microservice Customers";
    }

    @Override
    public CustomerStorageProvider create(KeycloakSession session, ComponentModel model) {
        return new CustomerStorageProvider(session, model, createCustomerRepository(model));
    }

    @Override
    public void validateConfiguration(KeycloakSession session, RealmModel realm, ComponentModel model)
        throws ComponentValidationException {

        CustomerRepository repository = null;

        try {
            repository = createCustomerRepository(model);
            long customerCount = repository.count();
            log.info("Customer count: " + customerCount + ", the configuration is valid.");
        } catch (Exception e) {
            log.error("Invalid configuration!", e);
            throw new ComponentValidationException("Could not connect to MongoDB", e);
        } finally {
            if (repository != null) {
                repository.dispose();
            }
        }
    }

    private CustomerRepository createCustomerRepository(ComponentModel config) {
        String uri = String.format("mongodb://%s:%s@%s:%s",
                config.get(DB_USER),
                config.get(DB_PASSWORD),
                config.get(DB_HOST),
                config.get(DB_PORT));

        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase(config.get(DB_NAME));
        MongoCollection<Document> customers = database.getCollection(config.get(DB_COLLECTION));

        return new CustomerRepository(mongoClient, customers);
    }
}
