package by.youngliqui.bookstorageservice.client;

import by.youngliqui.bookstorageservice.dto.user.RoleUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "${app.clients.auth-service.name}",
        url = "${app.clients.auth-service.url}",
        path = "${app.clients.auth-service.path}"
)
public interface AuthServiceClient {

    @GetMapping("/user-role/{token}")
    RoleUserDto getUserRole(@PathVariable String token);

}
