package com.estateguru.minibank.repository;

import com.estateguru.minibank.model.LimitationUserCurrency;
import com.estateguru.minibank.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    Boolean existsByNameAndUserName(String name, String userName);
    Optional<User> findByUserName(String userName);
    Optional<User> findByUserNameAndNameAndCode(String userName,String name,String code);
    @Query("SELECT us.id FROM User AS us WHERE us.userName=:username and us.name=:name and us.code=:code")
    Long getUserIdByUserNameAndNameAndCode(@Param("username") String userName, @Param("name")  String name, @Param("code")  String code);


    @Query("SELECT us FROM LimitationUserCurrency AS us WHERE us.user=:user")
    List<LimitationUserCurrency> existCurrencyForUser (@Param("user") User user);

}
