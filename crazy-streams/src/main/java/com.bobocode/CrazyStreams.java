package com.bobocode;

import com.bobocode.exception.EntityNotFoundException;
import com.bobocode.model.Account;
import com.bobocode.model.Sex;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

/**
 * Implement methods using Stream API
 */
public class CrazyStreams {
    private Collection<Account> accounts;

    public static CrazyStreams of(Collection<Account> accounts) {
        return new CrazyStreams(accounts);
    }

    private CrazyStreams(Collection<Account> accounts) {
        this.accounts = accounts;
    }

    /**
     * Returns {@link Optional} that contains an {@link Account} with the max value of balance
     *
     * @return account with max balance wrapped with optional
     */
    public Optional<Account> findRichestPerson() {
        return accounts.stream()
                .max(Comparator.comparing(Account::getBalance));
    }

    /**
     * Returns a {@link List} of {@link Account} that have a birthday month equal to provided.
     *
     * @param birthdayMonth a month of birth
     * @return a list of accounts
     */
    public List<Account> findAccountsByBirthdayMonth(Month birthdayMonth) {
        return accounts.stream()
                .filter(a -> a.getBirthday().getMonth() == birthdayMonth)
                .collect(toUnmodifiableList());
    }

    /**
     * Returns a map that separates all accounts into two lists - male and female. Map has two keys {@code true} indicates
     * male list, and {@code false} indicates female list.
     *
     * @return a map where key is true or false, and value is list of male, and female accounts
     */
    public Map<Boolean, List<Account>> partitionMaleAccounts() {
        return accounts.stream()
                .collect(partitioningBy(a -> a.getSex() == Sex.MALE, toUnmodifiableList()));
    }

    /**
     * Returns a {@link Map} that stores accounts grouped by its email domain. A map key is {@link String} which is an
     * email domain like "gmail.com". And the value is a {@link List} of {@link Account} objects with a specific email domain.
     *
     * @return a map where key is an email domain and value is a list of all account with such email
     */
    public Map<String, List<Account>> groupAccountsByEmailDomain() {
        return accounts.stream()
                .collect(groupingBy(a -> a.getEmail().split("@")[1], toUnmodifiableList()));
    }

    /**
     * Returns a number of letters in all first and last names.
     *
     * @return total number of letters of first and last names of all accounts
     */
    public int getNumOfLettersInFirstAndLastNames() {
        return accounts.stream()
                .mapToInt(a -> a.getFirstName().length() + a.getLastName().length())
                .sum();
    }

    /**
     * Returns a total balance of all accounts.
     *
     * @return total balance of all accounts
     */
    public BigDecimal calculateTotalBalance() {
        return accounts.stream()
                .map(Account::getBalance)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    /**
     * Returns a {@link List} of {@link Account} objects sorted by first and last names.
     *
     * @return list of accounts sorted by first and last names
     */
    public List<Account> sortByFirstAndLastNames() {
        return accounts.stream()
                .sorted(Comparator.comparing(Account::getFirstName)
                                  .thenComparing(Account::getLastName))
                .collect(toUnmodifiableList());
    }

    /**
     * Checks if there is at least one account with provided email domain.
     *
     * @param emailDomain
     * @return true if there is an account that has an email with provided domain
     */
    public boolean containsAccountWithEmailDomain(String emailDomain) {
        return accounts.stream()
                .anyMatch(a -> a.getEmail().endsWith(emailDomain));
    }

    /**
     * Returns account balance by its email. Throws {@link EntityNotFoundException} with message
     * "Cannot find Account by email={email}" if account is not found.
     *
     * @param email account email
     * @return account balance
     */
    public BigDecimal getBalanceByEmail(String email) {
        return accounts.stream()
                .filter(a -> email.equals(a.getEmail()))
                .findFirst()
                .map(Account::getBalance)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Cannot find Account by email=%s", email)));
    }

    /**
     * Collects all existing accounts into a {@link Map} where a key is account id, and the value is {@link Account} instance
     *
     * @return map of accounts by its ids
     */
    public Map<Long, Account> collectAccountsById() {
        return accounts.stream()
                .collect(toUnmodifiableMap(Account::getId, Function.identity()));
    }

    /**
     * Filters accounts by the year when an account was created. Collects account balances by its emails into a {@link Map}.
     * The key is {@link Account#email} and the value is {@link Account#balance}
     *
     * @param year the year of account creation
     * @return map of account by its ids the were created in a particular year
     */
    public Map<String, BigDecimal> collectBalancesByEmailForAccountsCreatedOn(int year) {
        return accounts.stream()
                .filter(a -> a.getCreationDate().getYear() == year)
                .collect(toUnmodifiableMap(Account::getEmail, Account::getBalance));
    }

    /**
     * Returns a {@link Map} where key is {@link Account#lastName} and values is a {@link Set} that contains first names
     * of all accounts with a specific last name.
     *
     * @return a map where key is a last name and value is a set of first names
     */
    public Map<String, Set<String>> groupFirstNamesByLastNames() {
        return accounts.stream()
                .collect(collectingAndThen(groupingBy(Account::getLastName, mapping(Account::getFirstName, toUnmodifiableSet())),
                                           Collections::unmodifiableMap));
    }

    /**
     * Returns a {@link Map} where key is a birthday month, and value is a {@link String} that stores comma and space
     * -separated first names (e.g. "Polly, Dylan, Clark"), of all accounts that have the same birthday month.
     *
     * @return a map where a key is a birthday month and value is comma-separated first names
     */
    public Map<Month, String> groupCommaSeparatedFirstNamesByBirthdayMonth() {
        return accounts.stream()
                .collect(collectingAndThen(groupingBy(a -> a.getBirthday().getMonth(), mapping(Account::getFirstName, joining(", "))),
                                           Collections::unmodifiableMap));
    }

    /**
     * Returns a {@link Map} where key is a {@link Month} of {@link Account#creationDate}, and value is total balance
     * of all accounts that have the same value creation month.
     *
     * @return a map where key is a creation month and value is total balance of all accounts created in that month
     */
    public Map<Month, BigDecimal> groupTotalBalanceByCreationMonth() {
        return accounts.stream()
                .collect(collectingAndThen(groupingBy(a -> a.getCreationDate().getMonth(),
                                                      mapping(Account::getBalance, reducing(BigDecimal.ZERO, BigDecimal::add))),
                                           Collections::unmodifiableMap));
    }

    /**
     * Returns a {@link Map} where key is a letter {@link Character}, and value is a number of its occurrences in
     * {@link Account#firstName}.
     *
     * @return a map where key is a letter and value is its count in all first names
     */
    public Map<Character, Long> getCharacterFrequencyInFirstNames() {
        return accounts.stream()
                .map(Account::getFirstName)
                .flatMapToInt(String::chars)
                .mapToObj(cp -> (char) cp)
                .collect(collectingAndThen(groupingBy(Function.identity(), counting()), Collections::unmodifiableMap));
    }

    /**
     * Returns a {@link Map} where key is a letter {@link Character}, and value is a number of its occurrences ignoring
     * case, in all {@link Account#firstName} and {@link Account#lastName}. All letters should stored in lower case.
     *
     * @return a map where key is a letter and value is its count ignoring case in all first and last names
     */
    public Map<Character, Long> getCharacterFrequencyIgnoreCaseInFirstAndLastNames() {
        return accounts.stream()
                .flatMap(ac -> Stream.of(ac.getFirstName(), ac.getLastName()))
                .flatMapToInt(String::chars)
                .map(Character::toLowerCase)
                .mapToObj(cp -> (char) cp)
                .collect(collectingAndThen(groupingBy(Function.identity(), counting()), Collections::unmodifiableMap));
    }

}

