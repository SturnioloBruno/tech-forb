package com.techforb.plantsMonitor.user.dto;

import com.techforb.plantsMonitor.role.Role;
import com.techforb.plantsMonitor.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private List<String> roles;

    public static UserProfileResponse fromUser(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .build();
    }
}
