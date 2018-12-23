package com.badhax.CareerBot.Services;

import com.badhax.CareerBot.Models.Job;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CaribbeanJobsOnlineService {

    private static final Logger logger = LogManager.
            getLogger(CaribbeanJobsOnlineService.class.getName());
    private String[] searchTerms;

    public CaribbeanJobsOnlineService(String args[])
    {
        searchTerms = args;
    }

    public List<Job> Search (String position, String location)
    {
        List<Job> result = new ArrayList<Job>();
        Document html = null;

        String query = "https://www.caribbeanjobs.com/ShowResults.aspx?" +
                "Keywords=" + String.join("+",position.split("\\s+")) +
                "&Location="+ String.join("+",location.split("\\s+")) +
                "&Category=" +
                "&Recruiter=Company" +
                "&Recruiter=Agency" +
                "&btnSubmit=+";

        try
        {
            Document page = Jsoup.connect(query).get();
            result.addAll(Scrape(page));

            //next pages
            Element resultPages = page.getElementById("pagination");
            for(Element next : resultPages.children())
            {
                result.addAll(Scrape(Jsoup.connect(query+"&Page="+next.text()).get()));
            }
        }
        catch (IOException e)
        {
            logger.error("failed to search caribbeanjobsonline",e);
        }

        return  result;
    }

    public List<Job> Scrape(Document html)
    {
        List<Job> result = new ArrayList<Job>();

        Elements jobs = html.getElementsByClass("module-content");
        for(Element job : jobs)
        {
            Job j = new Job();

            j.title = job.selectFirst("h2[itemprop=title]").child(0).text();
            j.company = job.selectFirst("h3[itemprop=name]").child(0).text();
            j.description = job.selectFirst("p[itemprop=description]").child(0).text();
            j.salary = job.selectFirst("li[itemprop=baseSalary]").text();
            j.lastUpdated = job.selectFirst("li[itemprop=datePosted]").text();
            j.location = job.selectFirst("li[itemprop=jobLocation]").text();

            result.add(j);
        }
        return  result;
    }
}
