package com.lineying.entity;

import lombok.Data;

@Data
public class CommonUpdateEntity {
    private String where;
    private String set;
    private String table;
}
