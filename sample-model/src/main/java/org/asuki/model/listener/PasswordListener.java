package org.asuki.model.listener;

import static com.google.common.base.Strings.isNullOrEmpty;

import javax.persistence.PostLoad;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.asuki.common.util.Cryptos;
import org.asuki.model.entity.User;

public class PasswordListener {

    @PrePersist
    @PreUpdate
    public void encryptPassword(Object obj) {
        if (!(obj instanceof User)) {
            return;
        }

        User user = (User) obj;
        user.setEncryptedPassword(null);

        if (!isNullOrEmpty(user.getPassword())) {
            user.setEncryptedPassword(Cryptos.encrypt(user.getPassword()));
        }
    }

    @PostLoad
    @PostUpdate
    public void decryptPassword(Object obj) {
        if (!(obj instanceof User)) {
            return;
        }

        User user = (User) obj;
        user.setPassword(null);

        if (!isNullOrEmpty(user.getEncryptedPassword())) {
            user.setPassword(Cryptos.decrypt(user.getEncryptedPassword()));
        }
    }

}
