package org.addy.keycloak;

import lombok.RequiredArgsConstructor;
import org.addy.keycloak.adapter.CustomerUserAdapter;
import org.addy.keycloak.model.Customer;
import org.addy.keycloak.repository.CustomerRepository;
import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.*;
import org.keycloak.models.cache.CachedUserModel;
import org.keycloak.models.cache.OnUserCache;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;
import org.keycloak.storage.user.UserRegistrationProvider;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class CustomerStorageProvider implements
        UserStorageProvider,
        UserLookupProvider,
        UserRegistrationProvider,
        UserQueryProvider,
        CredentialInputUpdater,
        CredentialInputValidator,
        OnUserCache {

    private static final String PASSWORD_CACHE_KEY = CustomerUserAdapter.class.getName() + ".password";
    private static final Logger log = Logger.getLogger(CustomerStorageProvider.class);

    private final KeycloakSession session;
    private final ComponentModel model;
    private final CustomerRepository repository;

    @Override
    public void close() {
        repository.dispose();
    }

    @Override
    public boolean supportsCredentialType(String credentialType) {
        log.info("Checking support for credential type " + credentialType);
        return PasswordCredentialModel.TYPE.endsWith(credentialType);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        log.info("Checking configuration for user " + user.getId() + " and credential type " + credentialType);
        return supportsCredentialType(credentialType);
    }

    @Override
    public boolean isValid(RealmModel realm, UserModel user, CredentialInput input) {
        log.info("Checking validity of credential input for user " + user.getId());
        if (!(supportsCredentialType(input.getType()) && (input instanceof UserCredentialModel cred))) {
            return false;
        }

        return Objects.equals(extractPassword(user), cred.getValue());
    }

    @Override
    public boolean updateCredential(RealmModel realm, UserModel user, CredentialInput input) {
        log.info("Updating credentials for user " + user.getId());
        if (!(supportsCredentialType(input.getType()) && (input instanceof UserCredentialModel cred))) {
            return false;
        }

        extractCustomerModel(user).setPassword(cred.getValue());

        return true;
    }

    @Override
    public void disableCredentialType(RealmModel realm, UserModel user, String credentialType) {
        log.info("Disabling credential type " + credentialType + " for user " + user.getId());
        if (!supportsCredentialType(credentialType))  return;
        
        extractCustomerModel(user).setPassword(null);
    }

    @Override
    public Stream<String> getDisableableCredentialTypesStream(RealmModel realm, UserModel user) {
        log.info("Fetching disabled credentials for user " + user.getId());
        return Stream.empty();
    }

    @Override
    public UserModel getUserById(RealmModel realm, String id) {
        log.info("Fetching user for id " + id);
        return repository.findById(StorageId.externalId(id))
                .map(customer -> mapUser(realm, customer))
                .orElse(null);
    }

    @Override
    public UserModel getUserByUsername(RealmModel realm, String username) {
        log.info("Fetching user for username " + username);
        return getUserByEmail(realm, username);
    }

    @Override
    public UserModel getUserByEmail(RealmModel realm, String email) {
        log.info("Fetching user for email " + email);
        return repository.findByEmail(email)
                .map(customer -> mapUser(realm, customer))
                .orElse(null);
    }

    @Override
    public int getUsersCount(RealmModel realm) {
        log.info("Getting user count");
        return (int) repository.count();
    }

    @Override
    public Stream<UserModel> searchForUserStream(RealmModel realm, Map<String, String> params,
                                                 Integer firstResult, Integer maxResults) {

        log.info("Fetching all users in range " + firstResult + " to " + maxResults + " with params " + params);
        Stream<Customer> allCustomers = repository.findAll().stream();

        if (firstResult != null) {
            allCustomers = allCustomers.skip(firstResult);
        }

        if (maxResults != null) {
            allCustomers = allCustomers.limit(maxResults);
        }

        return allCustomers.map(customer -> mapUser(realm, customer));
    }

    @Override
    public Stream<UserModel> getGroupMembersStream(RealmModel realm, GroupModel group,
                                                   Integer firstResult, Integer maxResults) {

        log.info("Fetching members of group " + group.getName());
        return Stream.empty();
    }

    @Override
    public Stream<UserModel> searchForUserByUserAttributeStream(RealmModel realm, String name, String value) {
        log.info("Fetching users for which " + name + " is " + value);
        return Stream.empty();
    }

    @Override
    public UserModel addUser(RealmModel realm, String username) {
        log.info("Adding user with username " + username);
        Customer customer = Customer.builder()
                .email(username)
                .name("Foo Bar")
                .password("changeit")
                .build();

        customer = repository.create(customer);

        return mapUser(realm, customer);
    }

    @Override
    public boolean removeUser(RealmModel realm, UserModel user) {
        log.info("Removing user " + user.getId());
        String customerId = StorageId.externalId(user.getId());

        return repository.findById(customerId)
                .map(customer -> {
                    repository.delete(customerId);
                    return true;
                })
                .orElse(false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCache(RealmModel realm, CachedUserModel cachedUser, UserModel user) {
        log.info("Caching user " + user.getId());
        String password = ((CustomerUserAdapter) user).getPassword();
        if (password != null) {
            cachedUser.getCachedWith().put(PASSWORD_CACHE_KEY, password);
        }
    }

    private static CustomerUserAdapter extractCustomerModel(UserModel user) {
        return user instanceof CachedUserModel cachedUserModel
                ? (CustomerUserAdapter) cachedUserModel.getDelegateForUpdate()
                : (CustomerUserAdapter) user;
    }

    private static String extractPassword(UserModel user) {
        String password = null;

        if (user instanceof CachedUserModel cachedUserModel) {
            password = (String) cachedUserModel.getCachedWith().get(PASSWORD_CACHE_KEY);
        } else if (user instanceof CustomerUserAdapter customerModel) {
            password = customerModel.getPassword();
        }

        return password;
    }

    private UserModel mapUser(RealmModel realm, Customer customer) {
        return new CustomerUserAdapter(session, realm, model, customer, repository);
    }
}
