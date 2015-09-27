package com.expresso.model.UserProfileModel;

/**
 * Created by rohit on 25/6/15.
 */
public class VisitedPlaces {
    String visited_place_name,date_of_visit,month_of_visit,year_of_visit;

    public VisitedPlaces()
    {}


    public void setVisited_place_name(String visited_place_name)
    {
        this.visited_place_name=visited_place_name;
    }
    public String getVisited_place_name()
    {
        return visited_place_name;
    }



    public void setDate_of_visit(String date_of_visit)
    {
        this.date_of_visit=date_of_visit;
    }

    public String getDate_of_visit() {
        return date_of_visit;
    }




    public void setMonth_of_visit(String month_of_visit)
    {
        this.month_of_visit=month_of_visit;

    }
    public String getMonth_of_visit(){return month_of_visit;}


    public void setYear_of_visit(String year_of_visit)
    {this.year_of_visit=year_of_visit;}
    public String getYear_of_visit(){return  year_of_visit;}
}
