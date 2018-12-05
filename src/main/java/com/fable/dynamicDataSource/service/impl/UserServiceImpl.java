package com.fable.dynamicDataSource.service.impl;

import com.fable.dynamicDataSource.dao.UserDao;
import com.fable.dynamicDataSource.domain.User;
import com.fable.dynamicDataSource.dynamicDataSource.DynamicDataSourceContextHolder;
import com.fable.dynamicDataSource.dynamicDataSource.TargetDataSource;
import com.fable.dynamicDataSource.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016/12/5 0005.
 */
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;

	public List<User> findUsersByDefaultDatasourcce() {
		return userDao.findAll();
	}

	public List<User> findUsersByDatasourcce(String tenantID) {
		DynamicDataSourceContextHolder.setDataSource(tenantID);
		return userDao.findAll();
	}


}
