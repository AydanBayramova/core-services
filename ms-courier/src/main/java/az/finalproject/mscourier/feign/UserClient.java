package az.finalproject.mscourier.feign;

import az.finalproject.mscourier.feign.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "ms-user", url = "http://localhost:8081/api/v1/users")
public interface UserClient {

    @GetMapping("/{userId}")
    UserResponse getUserById(@PathVariable("userId") UUID id);
}
