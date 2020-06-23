package com.github.callmewaggs.demospringdata;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Study {
  @Id
  @GeneratedValue
  private Long id;

  private String name;

  @ManyToOne
  private Account owner;

  public Study(String name, Account owner) {
    this.name = name;
    this.owner = owner;
  }
}
