package org.asuki.model.entity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.asuki.model.converter.CryptoConverter;
import org.asuki.model.listener.PasswordListener;

@Entity
@Table(name = "user")
@SecondaryTable(name = "user_pw", pkJoinColumns = @PrimaryKeyJoinColumn(name = "user_id"))
@EntityListeners(PasswordListener.class)
@Setter
@Getter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column(name = "password", table = "user_pw")
    @Lob
    private String encryptedPassword;

    @Transient
    private transient String password;

    @Column(name = "card_number")
    @Convert(converter = CryptoConverter.class)
    private String cardNumber;

}
