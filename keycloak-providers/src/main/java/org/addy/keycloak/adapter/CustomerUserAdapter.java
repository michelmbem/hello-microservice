package org.addy.keycloak.adapter;

import org.addy.keycloak.model.Customer;
import org.addy.keycloak.repository.CustomerRepository;
import org.jboss.logging.Logger;
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomerUserAdapter extends AbstractUserAdapterFederatedStorage {
    private static final Logger log = Logger.getLogger(CustomerUserAdapter.class);

    private final Customer customer;
    private final CustomerRepository repository;
    private final String keycloakId;

    public CustomerUserAdapter(KeycloakSession session, RealmModel realm, ComponentModel model,
                               Customer customer, CustomerRepository repository) {

        super(session, realm, model);
        this.customer = customer;
        this.repository = repository;
        keycloakId = StorageId.keycloakId(model, customer.getId());
        log.info("CustomerUserAdapter created, keycloakId: " + keycloakId);
    }

    public String getPassword() {
        return customer.getPassword();
    }

    public void setPassword(String password) {
        customer.setPassword(password);
        updateCustomer();
    }

    @Override
    public String getId() {
        return keycloakId;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public void setUsername(String username) {
        setEmail(username);
    }

    @Override
    public String getEmail() {
        return customer.getEmail();
    }

    @Override
    public void setEmail(String email) {
        customer.setEmail(email);
        updateCustomer();
    }

    @Override
    public boolean isEmailVerified() {
        return Objects.nonNull(getEmail());
    }

    @Override
    public String getFirstName() {
        return splitName(customer.getName())[0];
    }

    @Override
    public void setFirstName(String firstName) {
        log.info("Setting first name to " + firstName);
        String[] names = splitName(customer.getName());
        log.info("Original name: " + Arrays.toString(names));
        customer.setName(joinNames(firstName, names[1]));
        log.info("New name: " + customer.getName());
        updateCustomer();
    }

    @Override
    public String getLastName() {
        return splitName(customer.getName())[1];
    }

    @Override
    public void setLastName(String lastName) {
        log.info("Setting last name to " + lastName);
        String[] names = splitName(customer.getName());
        log.info("Original name: " + Arrays.toString(names));
        customer.setName(joinNames(names[0], lastName));
        log.info("New name: " + customer.getName());
        updateCustomer();
    }

    @Override
    public Long getCreatedTimestamp() {
        return customer.getCreatedAt();
    }

    @Override
    public void setCreatedTimestamp(Long createdTimestamp) {
        customer.setCreatedAt(createdTimestamp);
        updateCustomer();
    }

    @Override
    public boolean isEnabled() {
        return customer.isActive();
    }

    @Override
    public void setEnabled(boolean enabled) {
        customer.setActive(enabled);
        updateCustomer();
    }

    @Override
    public String getFirstAttribute(String name) {
        return switch (name) {
            case "firstName" -> getFirstName();
            case "lastName" -> getLastName();
            case "email" -> getEmail();
            case "emailVerified" -> String.valueOf(isEmailVerified());
            case "createdTimestamp" -> String.valueOf(getCreatedTimestamp());
            case "enabled" -> String.valueOf(isEnabled());
            case "phoneNumber" -> customer.getPhoneNumber();
            case "address" -> customer.getAddress();
            case "city" -> customer.getCity();
            case "state" -> customer.getState();
            case "postalCode" -> customer.getPostalCode();
            default -> null;
        };
    }

    @Override
    public void setSingleAttribute(String name, String value) {
        switch (name) {
            case "firstName" -> setFirstName(value);
            case "lastName" -> setLastName(value);
            case "email" -> setEmail(value);
            case "createdTimestamp" -> setCreatedTimestamp(Long.valueOf(value));
            case "enabled" -> setEnabled(Boolean.parseBoolean(value));
            case "phoneNumber" -> customer.setPhoneNumber(value);
            case "address" -> customer.setAddress(value);
            case "city" -> customer.setCity(value);
            case "state" -> customer.setState(value);
            case "postalCode" -> customer.setPostalCode(value);
            default -> log.warn("Unmapped attribute: " + name);
        }

        updateCustomer();
    }

    @Override
    public Map<String, List<String>> getAttributes() {
        var attributes = new MultivaluedHashMap<String, String>();
        attributes.putAll(super.getAttributes());
        attributes.put("firstName", listOf(getFirstName()));
        attributes.put("lastName", listOf(getLastName()));
        attributes.put("email", listOf(getEmail()));
        attributes.put("emailVerified", stringListOf(isEmailVerified()));
        attributes.put("createdTimestamp", stringListOf(getCreatedTimestamp()));
        attributes.put("enabled", stringListOf(isEnabled()));
        attributes.put("phoneNumber", listOf(customer.getPhoneNumber()));
        attributes.put("address", listOf(customer.getAddress()));
        attributes.put("city", listOf(customer.getCity()));
        attributes.put("state", listOf(customer.getState()));
        attributes.put("postalCode", listOf(customer.getPostalCode()));

        return attributes;
    }

    @Override
    public void setAttribute(String name, List<String> values) {
        setSingleAttribute(name, values.get(0));
    }

    @Override
    public Stream<String> getAttributeStream(String name) {
        String value = getFirstAttribute(name);
        return value == null ? Stream.empty() : Stream.of(value);
    }

    @Override
    public void removeAttribute(String name) {
        setSingleAttribute(name, null);
    }
    
    @SafeVarargs
    private static <T> List<T> listOf(T... elements) {
        return elements == null || elements.length == 0 || elements[0] == null
                ? List.of()
                : List.of(elements);

    }

    private static List<String> stringListOf(Object... elements) {
        return listOf(elements).stream().map(String::valueOf).toList();
    }

    private static String[] splitName(String name) {
        String[] parts = name.split("\\s+");

        return switch (parts.length) {
            case 0, 1 -> new String[] {parts[0], ""};
            default -> {
                int middle = parts.length - parts.length / 2;
                yield new String[]{
                        Stream.of(parts).limit(middle).collect(Collectors.joining(" ")),
                        Stream.of(parts).skip(middle).collect(Collectors.joining(" "))
                };
            }
        };
    }

    private static String joinNames(String... names) {
        return String.join(" ", names);
    }

    private void updateCustomer() {
        repository.update(customer.getId(), customer);
    }
}
