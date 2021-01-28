package com.highbrowape.demo.service;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;

public interface IInsightService {


   void  objectiveCompletionUpdate(String missionId,String  loggedInEmail,int n);
   void objectiveAddedUpdate(String missionId,String  loggedInEmail,int n);

   void noteAddedUpdate(String missionId,String  loggedInEmail,int n);
   void noteRemovedUpdate(String missionId,String  loggedInEmail,int n);


   void linkAddedUpdate(String missionId,String  loggedInEmail,int n);
   void linkRemovedUpdate(String missionId,String  loggedInEmail,int n);


   void  objectiveNonCompletionUpdate(String missionId,String  loggedInEmail,int n);
   void objectiveRemovedUpdate(String missionId,String  loggedInEmail,int n);

   void userActionUpdate(String  loggedInEmail,String comment);

   void addUserInteraction(String email, int n);
   HashMap<String,Object> getUSerCommentChartData(String email);

}
