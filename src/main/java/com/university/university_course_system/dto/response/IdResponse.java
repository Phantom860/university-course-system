package com.university.university_course_system.dto.response;

// ID响应DTO
public class IdResponse {
    private Integer id;
    private String type;

    // 构造方法
    public IdResponse() {}

    public IdResponse(Integer id, String type) {
        this.id = id;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}