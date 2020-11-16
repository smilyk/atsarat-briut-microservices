package com.scheduler.services.hystrix;


import feign.FeignException;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", fallbackFactory = UserFallbackFactory.class)
public interface UserServiceClient {
    @GetMapping("/users/v1/admin")
    public void loginUser(@RequestBody LoginUserHystrixDto user
    );
    @Component
    class UserFallbackFactory implements FallbackFactory<UserServiceClient> {
        @Override
        public UserServiceClient create(Throwable cause) {
            return new UserServiceClientFallback(cause);
        }
    }

    class UserServiceClientFallback implements UserServiceClient {

        Logger logger = LoggerFactory.getLogger(this.getClass());

        private final Throwable cause;

        public UserServiceClientFallback(Throwable cause) {
            this.cause = cause;
        }

        @Override
        public void loginUser(LoginUserHystrixDto user
//                                                   String req) {
        ){
            if (cause instanceof FeignException && ((FeignException) cause).status() == 404) {
                logger.error("404 error took place when login user was called with e-mail: " + user.getMainEmail() + ". Error message: "
                        + cause.getLocalizedMessage());
            } else {
                logger.error("Other error took place: " + cause.getLocalizedMessage());
            }
        }
    }
}
