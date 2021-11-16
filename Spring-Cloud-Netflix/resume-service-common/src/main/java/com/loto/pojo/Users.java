package com.loto.pojo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "oauth_users")
public class Users {
    @Id
    private Long id;
    private String username;
    private String password;
}
