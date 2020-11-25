#Atsarat-briut microservices
#
[![CircleCI](https://circleci.com/gh/smilyk/atsarat-briut-microservices.svg?style=svg)](https://circleci.com/gh/smilyk/atsarat-briut-microservices)
#
[![Build Status](https://travis-ci.org/smilyk/atsarat-briut-microservices.svg?branch=master)](https://travis-ci.org/smilyk/atsarat-briut-microservices)
#
PORTS AND NAME:
user-service

    server.port=8081
    spring.application.name=user-service
    
    
children-service

    server.port=8082
    spring.application.name=children-service
    
email-service
    
    server.port=8083
    spring.application.name=email-service
    
scheduler-service

    server.port=8084
    spring.application.name=scheduler-service
    
school-service

    server.port=8085
    spring.application.name=school-service
    
tsofim-service

    server.port=8086
    spring.application.name=tsofim-service
    
gymnast-service

    server.port=8087
    spring.application.name=gymnast-service
    
accept to actuator -> 

    http://localhost:8082/monitor
    good article: https://habr.com/ru/company/otus/blog/452624/
