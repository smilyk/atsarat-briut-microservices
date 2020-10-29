package com.tsofim.servicers.hystrix;


import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
class ChildFallbackFactory implements FallbackFactory<ChildServiceClient> {

	@Override
	public ChildServiceClient create(Throwable cause) {
		return new ChildrenServiceClientFallback(cause);
	}

}

