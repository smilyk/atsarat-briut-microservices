package com.scheduler.services.shedulerService;


import com.scheduler.entity.PlanEntity;
import com.scheduler.enums.LoggerMessages;
import com.scheduler.repository.PlanRepo;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@EnableScheduling
public class Scheduler {
    @Autowired
    PlanRepo planRepo;

    ModelMapper modelMapper = new ModelMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);

//    private String currentDate = LocalDateTime.now().toLocalDate().toString();
//    private LocalDateTime dateNow = LocalDateTime.now();

    //TODO где он должен быть
//    @PostConstruct
//    void setUTCTimezone() {
//        TimeZone.setDefault(TimeZone.getTimeZone("GMT+3"));
//    }
//
//+

    @Scheduled(cron = "0 40 7 ? * SUN-FRI", zone = "Asia/Jerusalem")

    /**
     * checking in 7 hours 40 minutes every day without saturday
     */
//    @Scheduled(fixedDelay = 3600000)
    public void scheduleCleanNotConfirmedEmail() {
        LocalDateTime dateNow = LocalDateTime.now();
        System.err.println("cron first start");
        LOGGER.info(LoggerMessages.SCHEDULER_START + dateNow);
        LOGGER.info(LoggerMessages.CHECKING_PLAN_RECORDS + dateNow);
        checkRecords();
    }

//    @Async
//    @Scheduled(fixedRate = 3600000)
//
//    public  void  x(){
//
//        ZonedDateTime dateNow = LocalDateTime.now().atZone(ZoneId.systemDefault());
//        System.err.println("not cron start " + dateNow);
//        checkDate();
//        checkDayOfWeeks();
//    }

//    @Scheduled(fixedDelay=3600000)
//    public  void  xds(){
////        checkDateTmp();
//        ZonedDateTime dateNow = LocalDateTime.now().atZone(ZoneId.systemDefault());
//        System.err.println("not cron2 start " + dateNow);
////        checkDate();
////        checkDayOfWeeks();
//    }



//+
//    @Scheduled(cron = "0 0 * /2? * *", zone = "Asia/Jerusalem")
//    public  void  xysf(){
//        LocalDateTime dateNow = LocalDateTime.now();
//        System.err.println("zapasnoy cron23 start " + dateNow);
//    }
//    +
//    @Scheduled(cron = "0 0/60 * * * *", zone = "Asia/Jerusalem")
//    public  void  xy(){
//        LocalDateTime dateNow = LocalDateTime.now();
//        System.err.println("zapasnoy cron start " + dateNow);
//    }
//+
//    @Scheduled(cron = "0 0 * * * *", zone = "Asia/Jerusalem")
//    public  void  xyxz(){
//        LocalDateTime dateNow = LocalDateTime.now();
//        System.err.println("zapasnoy2 cron start " + dateNow);
//        checkDateTmp();
//    }
//+
//    @Scheduled(cron = "0 0 0-23 * * *", zone = "Asia/Jerusalem")
//    public  void  xyxsfsz(){
//        LocalDateTime dateNow = LocalDateTime.now();
//        System.err.println("zapasnoy2er cron start " + dateNow);
//        checkDateTmp();
//    }


//+
//    @Scheduled(cron = "0 0 0-23 * * *", zone = "Asia/Jerusalem")
//    public  void  xyfxdz(){
//        LocalDateTime dateNow = LocalDateTime.now();
//        System.err.println("zapasnoy4 cron start " + dateNow);
//        checkDateTmp();
//    }
////+
//    @Scheduled(cron = "0 0 * * * ?", zone = "Asia/Jerusalem")
//    public  void  xyxdz(){
//        LocalDateTime dateNow = LocalDateTime.now();
//        System.err.println("zapasnoy3 cron start " + dateNow);
//        checkDateTmp();
//    }
//
//    private void checkDateTmp() {
//        List<PlanEntity> recordsList = planRepo.findAll();
//        recordsList.stream().forEach(x -> System.err.println(x.getDate() + " test " + x.getUuidPlan()));
//    }

    private void checkRecords() {
        checkDate();
        checkDayOfWeeks();
    }

    private void checkDayOfWeeks() {
        LocalDateTime dateNow = LocalDateTime.now();
        LOGGER.info(dateNow + " check day of weeks");
        List<PlanEntity> recordsList = planRepo.findAll();
        DayOfWeek today = dateNow.getDayOfWeek();
        List<PlanEntity> dateRecordList = new ArrayList<>(recordsList.size());
        switch (today) {
            case MONDAY:
                dateRecordList = recordsList.stream().filter(rec -> !rec.getDeleted())
                        .filter(rec -> rec.getMonday() != null)
//                        .filter(rec -> rec.getMonday_time() >= dateNow.getHour() && rec.getMonday_time() <
//                                dateNow.getHour() + 1)
                                .collect(Collectors.toList());
                if (dateRecordList.size() != 0) {
                    for (PlanEntity record : dateRecordList) {
                        LOGGER.info(" send request to + " + record.getService() + " with UUIDChild = "
                                + record.getUuidChild() + " withDay " + "MONDAY");
                    }
                }
                break;
            case TUESDAY:
                dateRecordList = recordsList.stream().filter(rec -> !rec.getDeleted())
                        .filter(rec -> rec.getTuesday() != null)
//                        .filter(rec -> rec.getTuesday_time() >= dateNow.getHour() && rec.getTuesday_time() <
//                                dateNow.getHour() + 1)
                        .collect(Collectors.toList());
                if (dateRecordList.size() != 0) {
                    for (PlanEntity record : dateRecordList) {
                        LOGGER.info(" send request to + " + record.getService() + " with UUIDChild = "
                                + record.getUuidChild() + " withDay " + "TUESDAY");
                    }
                }
                break;
            case WEDNESDAY:
                dateRecordList = recordsList.stream().filter(rec -> !rec.getDeleted())
                        .filter(rec -> rec.getWednesday() != null)
//                        .filter(rec -> rec.getWednesday_time() >= dateNow.getHour() && rec.getWednesday_time() <
//                                dateNow.getHour() + 1)
                        .collect(Collectors.toList());
                if (dateRecordList.size() != 0) {
                    for (PlanEntity record : dateRecordList) {
                        LOGGER.info(" send request to + " + record.getService() + " with UUIDChild = "
                                + record.getUuidChild() + " withDay " + "WEDNESDAY");
                    }
                }
                break;
            case THURSDAY:
                dateRecordList = recordsList.stream().filter(rec -> !rec.getDeleted())
                        .filter(rec -> rec.getThursday() != null)
//                        .filter(rec -> rec.getThursday_time() >= dateNow.getHour() && rec.getThursday_time() <
//                                dateNow.getHour() + 1)
                        .collect(Collectors.toList());
                if (dateRecordList.size() != 0) {
                    for (PlanEntity record : dateRecordList) {
                        LOGGER.info(" send request to + " + record.getService() + " with UUIDChild = "
                                + record.getUuidChild() + " withDay " + "THURSDAY" + record.getUuidPlan());
                    }
                }
                break;
            case FRIDAY:
                dateRecordList = recordsList.stream().filter(rec -> !rec.getDeleted())
                        .filter(rec -> rec.getFriday() != null)
//                        .filter(rec -> rec.getFriday_time() >= dateNow.getHour() && rec.getFriday_time() <
//                                dateNow.getHour() + 1)
                        .collect(Collectors.toList());
                if (dateRecordList.size() != 0) {
                    for (PlanEntity record : dateRecordList) {
                        LOGGER.info(" send request to + " + record.getService() + " with UUIDChild = "
                                + record.getUuidChild() + " withDay " + "FRIDAY");
                    }
                }
                break;
            case SATURDAY:
                dateRecordList = recordsList.stream().filter(rec -> !rec.getDeleted())
                        .filter(rec -> rec.getSaturday() != null)
//                        .filter(rec -> rec.getSaturday_time() >= dateNow.getHour() && rec.getSaturday_time() <
//                                dateNow.getHour() + 1)
                        .collect(Collectors.toList());
                if (dateRecordList.size() != 0) {
                    for (PlanEntity record : dateRecordList) {
                        LOGGER.info(" send request to + " + record.getService() + " with UUIDChild = "
                                + record.getUuidChild() + " withDay " + "SATURDAY");
                    }
                }
                break;
            case SUNDAY:
                dateRecordList = recordsList.stream().filter(rec -> !rec.getDeleted())
                        .filter(rec -> rec.getSunday() != null)
//                        .filter(rec -> rec.getSunday_time() >= dateNow.getHour() && rec.getSunday_time() <
//                                dateNow.getHour() + 1)
                        .collect(Collectors.toList());
                if (dateRecordList.size() != 0) {
                    for (PlanEntity record : dateRecordList) {
                        LOGGER.info(" send request to + " + record.getService() + " with UUIDChild = "
                                + record.getUuidChild() + " withDay " + "SUNDAY");
                    }
                }
                break;

            default:
                LOGGER.info("Not found records" + dateNow.toLocalDate());
                break;
        }

//        List<PlanEntity> dateRecordList = recordsList.stream().filter(rec -> !rec.getDeleted())
//                .filter(rec -> rec.get)
    }

    private void checkDate() {
        LocalDateTime dateNow = LocalDateTime.now();
        LOGGER.info(dateNow + " check date" );

        List<PlanEntity> recordsList = planRepo.findAll();
        List<PlanEntity> dateRecordList = recordsList.stream().filter(rec -> !rec.getDeleted())
                .filter(rec -> rec.getDate() != null)
                .filter(rec -> rec.getDate().toLocalDate().equals(dateNow.toLocalDate()))
////                .filter(rec -> rec.getDate().getYear() == dateNow.getYear())
////                .filter(rec -> rec.getDate().getMonth() == dateNow.getMonth())
////                .filter(rec -> rec.getDate().getDayOfMonth() == dateNow.getDayOfMonth())
//                .filter(rec -> rec.getDate().getHour() >= dateNow.getHour() && rec.getDate().getHour() < dateNow.getHour() + 1)
                .collect(Collectors.toList());
        if (dateRecordList.size() != 0) {
            for (PlanEntity record : dateRecordList) {
                LOGGER.info("send request to + " + record.getService() + "with UUIDChild = "
                        + record.getUuidChild() + " date " + record.getUuidPlan());
            }
        }
    }

}
