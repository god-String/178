package com.baizhi.zjy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class Guru implements Serializable {
    @Id
    private String id;
    private String name;
    private String photo;
    private String status;
    private String nick_name;
}
