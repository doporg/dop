//package com.clsaa.dop.server.permission.model.po;
//
//import javax.persistence.*;
//
///**
// * 功能点类，对应功能点表中每条数据
// *
// * @author lzy
// *
// * @param permission_id            功能点ID
// * @param parent_permission_id     父功能点ID
// * @param permission_name     功能点名称
// * @param permission_des     功能点描述
// *
// *
// * since :2019.3.1
// */
//
//@Entity
//@Table(name = "User") //引入@Table注解，name赋值为表名
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long user_id;
//    @ManyToOne
//    private Long department_id;
//    private String user_name;
//    private String mobile;
//
//
//    public User(Long department_id, String user_name, String mobile) {
//        this.department_id = department_id;
//        this.user_name = user_name;
//        this.mobile = mobile;
//    }
//
//    public void setUser_id(Long user_id) {
//        this.user_id = user_id;
//    }
//
//    public void setDepartment_id(Long department_id) {
//        this.department_id = department_id;
//    }
//
//    public void setUser_name(String user_name) {
//        this.user_name = user_name;
//    }
//
//    public void setMobile(String mobile) {
//        this.mobile = mobile;
//    }
//
//    public Long getUser_id() {
//        return user_id;
//    }
//
//    public Long getDepartment_id() {
//        return department_id;
//    }
//
//    public String getUser_name() {
//        return user_name;
//    }
//
//    public String getMobile() {
//        return mobile;
//    }
//
//
//
//}