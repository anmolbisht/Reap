package com.iiitb.spe.controller;


import com.iiitb.spe.model.entities.*;
import com.iiitb.spe.repo.NotificationRepository;
import com.iiitb.spe.repo.QuestionRepository;
import com.iiitb.spe.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserEntityService userEntityService;

    @Autowired
    private EndorsementEntityService endorsementEntityService;

    @Autowired
    private MailService mailService;

    @Autowired
    private QuestionsEntityService questionsEntityService;

    @Autowired
    private NotificationEntityService notificationEntityService;

    @RequestMapping("/getAllEndorsements")
    public ResponseEntity<?> getAllEndorsements(){
        System.out.println("In get all Endorsements api");
        UserEntity user = userEntityService.getDetails();
        List<EndorsementEntity> endorsements = endorsementEntityService.getAllEndorsements(user.getId());
        return ResponseEntity.ok(endorsements);
    }
    @RequestMapping("/getAllUserEndorsements")
    public ResponseEntity<?> getAllUserEndorsements(){
        System.out.println("In get all UserEndorsements api");
        UserEntity user = userEntityService.getDetails();
        List<EndorsementEntity> endorsements = endorsementEntityService.getAllUserEndorsements(user.getId());
        return ResponseEntity.ok(endorsements);
    }

    @RequestMapping(value="/addEndorsement", method = RequestMethod.POST)
    public ResponseEntity<?> addEndorsement(@RequestBody Map<String,Object> payload, @RequestHeader Map<String,String> headers) throws Exception{
        System.out.println("in add Endorsement api");
        UserEntity user = userEntityService.getDetails();
        UserEntity endorsedUser = userEntityService.getUserDetails(Long.valueOf((String) payload.get("userId")));


        //Coins Updation

        String update = userEntityService.updateCoins(payload,user,false);
        String update2 = userEntityService.updateCoins(payload,endorsedUser,true);
        //Endorsement Insertion
        String resEndorse = endorsementEntityService.addEndorsement(payload,headers.get("authorization"),user);
        String resNotify = notificationEntityService.createNotification(payload,user,1,endorsedUser.getFullName());

        //Mail Notification
//        MailEntity mail = new MailEntity();
//        mail.setMailFrom(user.getEmail());
//        mail.setMailTo(user.getEmail());
//        mail.setMailSubject("Hurrah !! New Endorsement For You "+ user.getFullName().toUpperCase());
//        mail.setMailContent("Someone has endrosed you on the platform!!!\n\nLogin Now to see :)");
//        String email = mailService.sendEmail(mail);


        return  ResponseEntity.ok(resEndorse+" "+update);
    }



    @RequestMapping(value = "/getAllNotifications")
    public ResponseEntity<?> getAllNotifications(){
        UserEntity user = userEntityService.getDetails();
        List<NotificationEntity> notif = notificationEntityService.getAllUserNotification(user);
        return ResponseEntity.ok(notif);
    }
}

