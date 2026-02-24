package com.shubham.authentication.controller;


import com.shubham.authentication.dto.Room;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @PostMapping
    //@PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAuthority('ROOM_ADD')")
    public String addRoom(){
        return "add room";
    }

    @GetMapping("/{id}")
   /* @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'GUEST')")
    @PostAuthorize("returnObject.assignedTo == authentication.name")*/
    @PreAuthorize("hasAuthority('ROOM_VIEW')")
    public Room getRoomById(@PathVariable Long id){
        return new Room(id, "Shubham3");
    }

    @GetMapping
    //@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PreAuthorize("hasAuthority('ROOM_VIEW_ALL')")
    public String getRooms(){
        return "All rooms";
    }
}
