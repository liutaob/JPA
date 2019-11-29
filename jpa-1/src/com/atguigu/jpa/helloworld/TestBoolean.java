package com.atguigu.jpa.helloworld;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author liutao
 * @create 2019-11-29 9:38
 */
@Table
@Entity
public class TestBoolean {
  @Id
  private Integer id;

  private Boolean gender;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Boolean getGender() {
    return gender;
  }

  public void setGender(Boolean gender) {
    this.gender = gender;
  }
}
