package com.fable.dynamicDataSource.dao;

import com.fable.dynamicDataSource.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator on 2016/12/5 0005.
 */
public interface UserDao extends JpaRepository<User, Integer> {

}
