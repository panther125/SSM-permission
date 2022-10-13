# SSM整合项目

>javaweb项目: SSM整合项目
>
>project by [ydl](!https://ydlclass.com)
>
>authon by [panther](!https://github.com/panther125)

## 项目定位

SSM+VUE项目🧪

## 相应功能

1. mybatis实现简单业务的增删改查
2. RABC权限系统，对应角色会对应不同权限。例如：普通用户只提供查操作，admin用户提供增删改操作
3. 处理xss特殊符号的替换防止xss攻击

## 使用的技术

**前端：**

- vue3.0脚手架+vue-route+vuex
- axios
- element-plus

**后端**

- Spring+SpringMVC+Mybatis
- mysql + redis

## 项目运行



1. 前端搭建

安装依赖

```Bash
npm install
```

运行服务

```Bash
npm run serve
```

2. 后端部署

打包

```java
mvn package
```

部署

将war包放入Tomcat的webapp中既可