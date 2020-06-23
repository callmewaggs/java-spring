package com.github.callmewaggs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Application {

  public static void main(String[] args) throws SQLException {
    String url = "jdbc:postgresql://localhost:5432/springdata";
    String usernane = "waggs";
    String password = "pass";

    // try-with-resource 문법 : 해당 블록이 끝날 때 자동으로 자원을 정리하는 메서드를 호출해 줌.
    try(Connection conn = DriverManager.getConnection(url, usernane, password)){
      System.out.println("Connection created : " + conn); // connection 확인
      // 테이블 생성 쿼리
//      String sql = "CREATE TABLE ACCOUNT (id int, username varchar(255), password varchar(255))";
      // DML 로 아이템을 넣어보는 쿼리
      String sql = "INSERT INTO ACCOUNT VALUES (0, 'waggs', 'pass')";
      try(PreparedStatement statement = conn.prepareStatement(sql)) {
        statement.execute(); // sql 실
      }
    }
  }
}
