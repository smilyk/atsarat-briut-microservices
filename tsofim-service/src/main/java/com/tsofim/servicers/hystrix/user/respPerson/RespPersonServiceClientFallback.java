package com.tsofim.servicers.hystrix.user.respPerson;


import com.tsofim.dto.Response;
import com.tsofim.servicers.hystrix.child.ChildHystrixDto;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RespPersonServiceClientFallback implements RespPersonServiceClient {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private final Throwable cause;

	public RespPersonServiceClientFallback(Throwable cause) {
		this.cause = cause;
	}


//
//Need if there is token
 	@Override
	public Response getResponsePersonByUserUuid(String childUuid, String req) {
		if (cause instanceof FeignException && ((FeignException) cause).status() == 404) {
			logger.error("404 error took place when getResponsePersonByUserUuid was called with childUuid: " + childUuid + ". Error message: "
					+ cause.getLocalizedMessage());
		} else {
			logger.error("Other error took place: " + cause.getLocalizedMessage());
		}
		return new Response();
	}

//	@Override
//	public Response getResponsePersonByUserUuid(String uuidChild) {
//		if (cause instanceof FeignException && ((FeignException) cause).status() == 404) {
//			logger.error("404 error took place when getAllChildren was called with childUuid: " + uuidChild + ". Error message: "
//					+ cause.getLocalizedMessage());
//		} else {
//			logger.error("Other error took place: " + cause.getLocalizedMessage());
//		}
//		return new Response();
//	}
}
