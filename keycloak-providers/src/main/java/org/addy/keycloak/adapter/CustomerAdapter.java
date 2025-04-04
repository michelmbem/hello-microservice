package org.addy.keycloak.adapter;

import org.addy.keycloak.model.Customer;
import org.addy.keycloak.repository.CustomerRepository;
import org.addy.keycloak.util.NamePair;
import org.jboss.logging.Logger;
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class CustomerAdapter extends AbstractUserAdapterFederatedStorage {
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String ADDRESS = "address";
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String POSTAL_CODE = "postalCode";
    public static final String ROLE_NAME = "customer";
    private static final Logger log = Logger.getLogger(CustomerAdapter.class);

    private final Customer customer;
    private final CustomerRepository repository;
    private final String keycloakId;

    public CustomerAdapter(KeycloakSession session, RealmModel realm, ComponentModel model,
                           Customer customer, CustomerRepository repository) {

        super(session, realm, model);
        this.customer = customer;
        this.repository = repository;
        keycloakId = StorageId.keycloakId(model, customer.getId());
        log.info("CustomerAdapter created, keycloakId: " + keycloakId + ", customer: " + this.customer);
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
        return true;
    }

    @Override
    public String getFirstName() {
        return NamePair.of(customer.getName()).first();
    }

    @Override
    public void setFirstName(String firstName) {
        customer.setName(NamePair.of(customer.getName()).withFirst(firstName).toString());
        updateCustomer();
    }

    @Override
    public String getLastName() {
        return NamePair.of(customer.getName()).last();
    }

    @Override
    public void setLastName(String lastName) {
        customer.setName(NamePair.of(customer.getName()).withLast(lastName).toString());
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
            case EMAIL -> getEmail();
            case FIRST_NAME -> getFirstName();
            case LAST_NAME -> getLastName();
            case PHONE_NUMBER -> customer.getPhoneNumber();
            case ADDRESS -> customer.getAddress();
            case CITY -> customer.getCity();
            case STATE -> customer.getState();
            case POSTAL_CODE -> customer.getPostalCode();
            default -> null;
        };
    }

    @Override
    public void setSingleAttribute(String name, String value) {
        switch (name) {
            case EMAIL -> setEmail(value);
            case FIRST_NAME -> setFirstName(value);
            case LAST_NAME -> setLastName(value);
            case PHONE_NUMBER -> {
                customer.setPhoneNumber(value);
                updateCustomer();
            }
            case ADDRESS -> {
                customer.setAddress(value);
                updateCustomer();
            }
            case CITY -> {
                customer.setCity(value);
                updateCustomer();
            }
            case STATE -> {
                customer.setState(value);
                updateCustomer();
            }
            case POSTAL_CODE -> {
                customer.setPostalCode(value);
                updateCustomer();
            }
            default -> log.warn("Unsupported attribute: " + name);
        }
    }

    @Override
    public Map<String, List<String>> getAttributes() {
        var attributes = new MultivaluedHashMap<String, String>();
        attributes.putAll(super.getAttributes());
        attributes.put(EMAIL, Stream.ofNullable(getEmail()).toList());
        attributes.put(FIRST_NAME, Stream.ofNullable(getFirstName()).toList());
        attributes.put(LAST_NAME, Stream.ofNullable(getLastName()).toList());
        attributes.put(PHONE_NUMBER, Stream.ofNullable(customer.getPhoneNumber()).toList());
        attributes.put(ADDRESS, Stream.ofNullable(customer.getAddress()).toList());
        attributes.put(CITY, Stream.ofNullable(customer.getCity()).toList());
        attributes.put(STATE, Stream.ofNullable(customer.getState()).toList());
        attributes.put(POSTAL_CODE, Stream.ofNullable(customer.getPostalCode()).toList());

        return attributes;
    }

    @Override
    public void setAttribute(String name, List<String> values) {
        setSingleAttribute(name, values.isEmpty() ? null : values.get(0));
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

    @Override
    protected Set<RoleModel> getRoleMappingsInternal() {
        RoleModel customerRole = realm.getRole(ROLE_NAME);

        if (customerRole == null) {
            customerRole = realm.addRole(ROLE_NAME);
        }

        return Set.of(customerRole);
    }

    private void updateCustomer() {
        repository.update(customer.getId(), customer);
    }
}
