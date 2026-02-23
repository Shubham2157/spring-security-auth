package com.shubham.authentication.dto;

public class Room {

    private Long id;
    private String assignedTo;

    public Room(Long id, String assignedTo) {
        this.id = id;
        this.assignedTo = assignedTo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }
}
