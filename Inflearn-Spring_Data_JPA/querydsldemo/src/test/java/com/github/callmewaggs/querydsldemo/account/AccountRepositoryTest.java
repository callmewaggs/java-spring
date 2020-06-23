package com.github.callmewaggs.querydsldemo.account;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.querydsl.core.types.Predicate;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryTest {

  @Autowired AccountRepository accountRepository;

  @Test
  public void crud() {
    QAccount account = QAccount.account;
    Predicate predicate =
        account.firstName.containsIgnoreCase("yoonsung").and(account.lastName.startsWith("lee"));

    Optional<Account> one = accountRepository.findOne(predicate);
    assertTrue(one.isEmpty());
  }
}