package com.github.callmewaggs.demospringdata;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Account {

  @Id // PK 를 매핑해주는 어노테이션
  @GeneratedValue // PK 를 자동생성 되는 값을 사용하겠다고 하는 것.
  private Long id;

  @Column(nullable = false, unique = true) // 기본적으로 @Column 에 디폴트로 설정된 값을 변경하고자 할 때 사용.
  private String username; // username 은 nullable 하면 안되고, unique 해야하니깐 이렇게 설정

  private String password;

  @OneToMany(mappedBy = "owner")
  private Set<Study> studies;

  public Account(String username, String password) {
    this.username = username;
    this.password = password;
    this.studies = new HashSet<>();
  }

  public Long getId() {
    return id;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Set<Study> getStudies() {
    return studies;
  }
}
