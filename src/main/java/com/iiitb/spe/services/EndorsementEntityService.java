package com.iiitb.spe.services;

import com.iiitb.spe.model.entities.EndorsementEntity;
import com.iiitb.spe.model.entities.UserEntity;
import com.iiitb.spe.repo.EndorsementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.sql.Timestamp;

@Service
public class EndorsementEntityService {
    @Autowired
    private EndorsementRepository endorsementRepository;



    public List<EndorsementEntity> getAllEndorsements(Long giverId){
        List<EndorsementEntity> endorsements = new ArrayList<>();

        endorsementRepository.findAllEndorsementsByGiverId(giverId).forEach(endorsement->endorsements.add(endorsement));
        return endorsements;
    }
    public List<EndorsementEntity> getAllUserEndorsements(Long takerId){
        List<EndorsementEntity> endorsements = new ArrayList<>();

        endorsementRepository.findAllEndorsementsByTakerId(takerId).forEach(endorsement->endorsements.add(endorsement));
        return endorsements;
    }

    public String addEndorsement(Map<String,Object> payload, String token,UserEntity user){
        try{
            EndorsementEntity endorse = new EndorsementEntity();
            endorse.setGiverId(user.getId());
            endorse.setTakerId(Long.valueOf((String) payload.get("userId")));
            Date date = new Date();
            endorse.setEndorsedOn(new Timestamp(date.getTime()));
            endorse.setMessage((String)(payload.get("message")));
            int coins = Integer.valueOf((String) payload.get("coins"));

            endorse.setCoinsEndorsed(coins);
            endorse.setTagId(Long.valueOf((String) payload.get("tagId")));
            EndorsementEntity newEndorse = endorsementRepository.save(endorse);
            System.out.println(newEndorse.getId());
            return "Added Successfully";
        }

        catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
