package com.tsofim.servicers.hystrix.child;


import com.tsofim.dto.Response;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ChildrenServiceClientFallback implements ChildServiceClient {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private final Throwable cause;

	public ChildrenServiceClientFallback(Throwable cause) {
		this.cause = cause;
	}


//
//Need if there is token
// 	@Override
//	public ChildHystrixDto getChildByChildUuid(String childUuid, String req) {
//		if (cause instanceof FeignException && ((FeignException) cause).status() == 404) {
//			logger.error("404 error took place when getAllChildren was called with childUuid: " + childUuid + ". Error message: "
//					+ cause.getLocalizedMessage());
//		} else {
//			logger.error("Other error took place: " + cause.getLocalizedMessage());
//		}
//		return new ChildHystrixDto();
//	}

	@Override
	public Response getChildByChildUuid(String uuidChild) {
		if (cause instanceof FeignException && ((FeignException) cause).status() == 404) {
			logger.error("404 error took place when getAllChildren was called with childUuid: " + uuidChild + ". Error message: "
					+ cause.getLocalizedMessage());
		} else {
			logger.error("Other error took place: " + cause.getLocalizedMessage());
		}
		return new Response();
	}
}
