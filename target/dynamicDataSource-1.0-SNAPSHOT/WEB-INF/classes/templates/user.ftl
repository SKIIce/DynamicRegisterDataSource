<html>
    <head>
        <title>用户列表</title>
    </head>
    <body>
        所有用户列表：<br/>
        <ul>
            <#list userList as user>
                <li>用户名：${user.username}---密码：${user.password}</li>
            </#list>
        </ul>
    </body>
</html>
