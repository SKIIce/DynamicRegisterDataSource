package com.fable.dynamicDataSource.domain;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "tenant")
@Data
public class Tenant {
    @Id
    @Column(name="tenantID",length=30)
    @NotEmpty(message = "租户名不能为空")
    private String tenantID;

    @Column(name="jdbc_Url",length=1000)
    @NotEmpty(message = "数据库连接不能为空")
    private String jdbc_Url;

    @Column(name="date",length=30)
    private String date;

    @Column(name="type",length=200)
    private String type;

    @Column(name="username",length=30)
    private String username;

    @Column(name="password",length=30)
    private String password;

}
