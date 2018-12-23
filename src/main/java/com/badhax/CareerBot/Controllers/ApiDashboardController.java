package com.badhax.CareerBot.Controllers;

import com.badhax.CareerBot.Models.Job;
import com.badhax.CareerBot.Services.CaribbeanJobsOnlineService;
import io.swagger.annotations.SwaggerDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ControllerAdvice
@RestController
@RequestMapping("/api")
@SwaggerDefinition
public class ApiDashboardController
{
    @Autowired
    CaribbeanJobsOnlineService caribbeanJobsOnlineService;

    @RequestMapping(value = "/caribbeanjobsonline", method = RequestMethod.POST)
    public List<Job> QueryCarribeanJobs(@RequestBody String jobTitle, @RequestBody String position)
    {
        return caribbeanJobsOnlineService.Search(jobTitle,position);
    }
}
