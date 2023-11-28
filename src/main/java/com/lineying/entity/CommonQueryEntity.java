package com.lineying.entity;

import lombok.Data;

import javax.annotation.sql.DataSourceDefinition;

@Data
public class CommonQueryEntity {

    private String table;

    private String column;

    private String where;

    private String sort;

    private String sortColumn;

}
