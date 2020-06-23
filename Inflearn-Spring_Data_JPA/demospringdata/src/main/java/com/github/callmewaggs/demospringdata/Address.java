package com.github.callmewaggs.demospringdata;

import javax.persistence.Embeddable;

@Embeddable // 엔티티가 아닌 특정 엔티티에 종속적인 Value 타입을 만들 때 사용
public class Address {

  private String street;

  private String city;

  private String state;

  private String zipCode;

}
