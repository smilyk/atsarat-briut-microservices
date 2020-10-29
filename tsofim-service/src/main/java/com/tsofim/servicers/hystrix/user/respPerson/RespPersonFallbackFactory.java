package com.tsofim.servicers.hystrix.user.respPerson;


import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
class RespPersonFallbackFactory implements FallbackFactory<RespPersonServiceClient> {

	@Override
	public RespPersonServiceClient create(Throwable cause) {
		return new RespPersonServiceClientFallback(cause);
	}

}

