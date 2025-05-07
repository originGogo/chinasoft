package com.gogo.psy.user.service;

import com.gogo.psy.user.pojo.model.UserAccount;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserBankAccountService extends JpaRepository<UserAccount, Long> {

    @Query(value = "select * from user_account where uid = :uid", nativeQuery = true)
    UserAccount findByUid(@Param("uid") Long uid);

    @Query(value = "select * from user_account where bank_account = :bankAccount", nativeQuery = true)
    UserAccount findByBankAccount(@Param("bankAccount") String bankAccount);
}
