package com.juxinli.rabbit;

/**
 * Created by maybo on 17/6/23.
 */
public class Exchange {
    private String name;
    private Schema schema;

    public void setName(String name) {
        this.name = name;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public Schema getSchema() {
        return schema;
    }

    public String getName() {
        return name;
    }
}
