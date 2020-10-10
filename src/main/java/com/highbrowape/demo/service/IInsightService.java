package com.highbrowape.demo.service;

public interface IInsightService {


   void  objectiveCompletionUpdate(String missionId,String  loggedInEmail);
   void objectiveAddedUpdate(String missionId,String  loggedInEmail);

   void userActionUpdate(String  loggedInEmail,String comment);

}
