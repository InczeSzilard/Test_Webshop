
package com.mycompany.webshop;

public abstract class BaseEntity implements Identifiable {

    protected String id;

    private static int entityCount = 0;

    public BaseEntity(String id) {
        this.id = id;
        entityCount++;
    }

    public BaseEntity() {
        this("");
    }

    public String getId() {
        return id;
    }

    public abstract String businessKey();

    public static int getEntityCount() {
        return entityCount;
    }

 
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof BaseEntity)) {
            return false;
        }

        BaseEntity other = (BaseEntity) obj;

        return businessKey().equals(other.businessKey());
    }

    public int hashCode() {
        return businessKey().hashCode();
    }

    public String toString() {
        return "BaseEntity{id='" + id + "'}";
    }
}
